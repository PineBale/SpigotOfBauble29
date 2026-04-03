package dev.pinebale.minecraft.spigotofbauble29.function;

import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * An Iterator that shuffles and provides integers from a specified range (inclusive)
 * using a Fisher-Yates-like shuffle strategy without pre-generating the full list.
 * <p>
 * This implementation is memory-efficient for large ranges.
 */
@SuppressWarnings("unused")
public class RangeShuffleIterator implements PrimitiveIterator.OfInt {

    private final IntSet remaining;
    private int remainingCount;

    /**
     * Creates a RangeShuffleIterator for the range [start, end] inclusive.
     *
     * @param start lower bound (inclusive)
     * @param end   upper bound (inclusive)
     * @throws IllegalArgumentException if start > end
     */
    public RangeShuffleIterator(int start, int end) {
        Preconditions.checkArgument(start <= end, "start must be <= end");
        this.remaining = new IntSet(start, end);
        this.remainingCount = end - start + 1;
    }

    /**
     * Returns up to {@code maxAmount} remaining integers in random order.
     * If fewer than {@code maxAmount} elements remain, returns all remaining.
     *
     * @param maxAmount maximum number of integers to return
     * @return array containing the requested (or remaining) integers
     */
    public int[] nextInts(int maxAmount) {
        int amount = Math.min(remainingCount, maxAmount);
        int[] result = new int[amount];

        for (int i = 0; i < amount; i++) {
            result[i] = nextInt();
        }
        return result;
    }

    @Override
    public int nextInt() {
        if (!hasNext()) {
            throw new NoSuchElementException("No remaining elements");
        }

        Random random = new Random();
        int selectedPosition = random.nextInt(remainingCount);

        Iterator<Map.Entry<Integer, Integer>> it = remaining.ranges().iterator();

        int selected;
        while (true) {
            Map.Entry<Integer, Integer> range = it.next();
            int lower = range.getKey();
            int upper = range.getValue();
            int span = upper - lower;

            if (span < selectedPosition) {
                selectedPosition -= span + 1;
            } else {
                selected = lower + selectedPosition;
                break;
            }
        }

        remaining.remove(selected);
        remainingCount--;

        return selected;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasNext() {
        return remainingCount > 0;
    }

    /**
     * Memory-efficient set of integers represented as merged ranges.
     * Only supports construction from a single range and single-element removal.
     */
    private static class IntSet {

        private final NavigableMap<Integer, Integer> ranges = new TreeMap<>();

        IntSet(int start, int end) {
            ranges.put(start, end);
        }

        Set<Map.Entry<Integer, Integer>> ranges() {
            return ranges.entrySet();
        }

        /**
         * Removes a single value from the set (if present).
         *
         * @param value the integer to remove
         */
        void remove(int value) {
            Map.Entry<Integer, Integer> range = ranges.floorEntry(value);
            if (range == null || range.getValue() < value) {
                return;
            }

            int lower = range.getKey();
            int upper = range.getValue();

            ranges.remove(lower);

            if (value > lower) {
                reinsert(lower, value - 1);
            }
            if (value < upper) {
                reinsert(value + 1, upper);
            }
        }

        private void reinsert(int start, int end) {
            if (end < start) {
                return;
            }
            ranges.put(start, end);
        }
    }
}
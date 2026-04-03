package dev.pinebale.minecraft.spigotofbauble29;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilBlockBase {
    public static List<Block> getSurrounding(Block block, boolean diagonals) {
        List<Block> surrounding = new ArrayList<>();
        if (diagonals) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = 1; y >= -1; y--) {
                        if (x == 0 && y == 0 && z == 0) {
                            continue;
                        }
                        surrounding.add(block.getRelative(x, y, z));
                    }
                }
            }
        } else {
            surrounding.add(block.getRelative(BlockFace.UP));
            surrounding.add(block.getRelative(BlockFace.NORTH));
            surrounding.add(block.getRelative(BlockFace.SOUTH));
            surrounding.add(block.getRelative(BlockFace.EAST));
            surrounding.add(block.getRelative(BlockFace.WEST));
            surrounding.add(block.getRelative(BlockFace.DOWN));
        }
        return surrounding;
    }
}

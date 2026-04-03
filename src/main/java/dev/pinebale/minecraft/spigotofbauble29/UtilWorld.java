package dev.pinebale.minecraft.spigotofbauble29;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilWorld {

    /**
     * Reminder: Use relative path!
     * Refer to PandaSpigot Server for more information!
     *
     * @param world World name, or path relative to Bukkit.getWorldContainer to a world
     * @return World object, or null
     * @see Bukkit#getWorldContainer()
     */
    public static World getWorld(String world) {
        return Bukkit.getServer().getWorld(world);
    }

    public static boolean isInChunk(Location location, Chunk chunk) {
        return location.getChunk().getX() == chunk.getX() && location.getChunk().getZ() == chunk.getZ() && chunk.getWorld().equals(location.getChunk().getWorld());
    }

    public static boolean areChunksEqual(Location first, Location second) {
        return (first.getBlockX() >> 4) == (second.getBlockX() >> 4) && (first.getBlockZ() >> 4) == (second.getBlockZ() >> 4);
    }

    /**
     * @param chunk Chunk object
     * @return "x,z" or empty string if chunk is null
     */
    public static String chunkToStrClean(Chunk chunk) {
        if (chunk == null) {
            return "";
        }
        return chunk.getX() + "," + chunk.getZ();
    }

    /**
     * @param block Block object
     * @return "x,y,z" or empty string if block is null
     */
    public static String blockToStrClean(Block block) {
        if (block == null) {
            return "";
        }
        return block.getX() + "," + block.getY() + "," + block.getZ();
    }

    /**
     * @param loc Location object
     * @return "blockX,blockY,blockZ" or "Null" if loc is null
     */
    public static String locToStrClean(Location loc) {
        if (loc == null) {
            return "Null";
        }
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    /**
     * @param vec Vector object
     * @return "x,y,z" or "Null" if vec is null
     */
    public static String vecToStrClean(Vector vec) {
        if (vec == null) {
            return "Null";
        }
        return vec.getX() + "," + vec.getY() + "," + vec.getZ();
    }

    public static String envToStr(World.Environment env) {
        switch (env) {
            case NORMAL:
                return "Overworld";
            case NETHER:
                return "Nether";
            case THE_END:
                return "The End";
            default:
                return "Unknown";
        }
    }

    public static Location averageLocation(Collection<Location> locs) {
        if (locs == null || locs.isEmpty()) {
            return null;
        }

        Vector vec = new Vector(0, 0, 0);
        double count = 0;
        World world = null;

        for (Location spawn : locs) {
            count++;
            vec.add(spawn.toVector());
            world = spawn.getWorld();
        }

        vec.multiply(1.0 / count);

        return vec.toLocation(world);
    }

    public static List<Block> branch(Location origin) {
        return Lists.newArrayList(
                origin.getBlock(),
                origin.getBlock().getRelative(BlockFace.DOWN),
                origin.getBlock().getRelative(BlockFace.UP),
                origin.getBlock().getRelative(BlockFace.NORTH),
                origin.getBlock().getRelative(BlockFace.EAST),
                origin.getBlock().getRelative(BlockFace.SOUTH),
                origin.getBlock().getRelative(BlockFace.WEST)
        );
    }
}

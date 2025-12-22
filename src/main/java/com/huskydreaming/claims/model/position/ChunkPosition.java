package com.huskydreaming.claims.model.position;

/**
 * Chunk column position (x,z). Represents a full-height column.
 */
public record ChunkPosition(int x, int z) {

    public static final int CHUNK_SIZE = 32;

    public static ChunkPosition fromBlock(BlockPosition blockPosition) {
        var cx = Math.floorDiv(blockPosition.x(), CHUNK_SIZE);
        var cz = Math.floorDiv(blockPosition.z(), CHUNK_SIZE);
        return new ChunkPosition(cx, cz);
    }

    public int minBlockX() {
        return x * CHUNK_SIZE;
    }

    public int minBlockZ() {
        return z * CHUNK_SIZE;
    }

    public int maxBlockX() {
        return minBlockX() + CHUNK_SIZE - 1;
    }

    public int maxBlockZ() {
        return minBlockZ() + CHUNK_SIZE - 1;
    }

    /**
     * Returns true if the block is inside this column in X/Z (Y ignored).
     *
     * @param block block position
     * @return true if the block's X/Z lies within this chunk column
     */
    public boolean contains(BlockPosition block) {
        return block.x() >= minBlockX() && block.x() <= maxBlockX()
                && block.z() >= minBlockZ() && block.z() <= maxBlockZ();
    }
}

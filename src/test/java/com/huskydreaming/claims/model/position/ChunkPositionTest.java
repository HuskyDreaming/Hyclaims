package com.huskydreaming.claims.model.position;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChunkPositionTest {

    @Test
    void fromBlockBoundariesPositive() {
        assertChunkFromBlock(0, 0, 0, 0);
        assertChunkFromBlock(31, 31, 0, 0);
        assertChunkFromBlock(32, 0, 1, 0);
        assertChunkFromBlock(0, 32, 0, 1);
        assertChunkFromBlock(32, 32, 1, 1);
        assertChunkFromBlock(63, 63, 1, 1);
        assertChunkFromBlock(64, 64, 2, 2);
    }

    @Test
    void fromBlockBoundariesNegative() {
        assertChunkFromBlock(-1, -1, -1, -1);
        assertChunkFromBlock(-32, -32, -1, -1);
        assertChunkFromBlock(-33, -33, -2, -2);

        assertChunkFromBlock(-1, 0, -1, 0);
        assertChunkFromBlock(0, -1, 0, -1);
        assertChunkFromBlock(-32, 0, -1, 0);
        assertChunkFromBlock(0, -32, 0, -1);
    }

    @Test
    void containsIgnoresYAndMatchesXZColumn() {
        var chunk = new ChunkPosition(0, 0);

        assertTrue(chunk.contains(new BlockPosition(0, -999, 0)));
        assertTrue(chunk.contains(new BlockPosition(31, 999, 31)));
        assertTrue(chunk.contains(new BlockPosition(15, 0, 20)));

        assertFalse(chunk.contains(new BlockPosition(-1, 0, 0)));
        assertFalse(chunk.contains(new BlockPosition(32, 0, 0)));

        assertFalse(chunk.contains(new BlockPosition(0, 0, -1)));
        assertFalse(chunk.contains(new BlockPosition(0, 0, 32)));
    }

    private static void assertChunkFromBlock(int blockX, int blockZ, int expectedChunkX, int expectedChunkZ) {
        var block = new BlockPosition(blockX, 0, blockZ);
        var chunk = ChunkPosition.fromBlock(block);

        assertEquals(expectedChunkX, chunk.x(), "chunkX");
        assertEquals(expectedChunkZ, chunk.z(), "chunkZ");
    }
}

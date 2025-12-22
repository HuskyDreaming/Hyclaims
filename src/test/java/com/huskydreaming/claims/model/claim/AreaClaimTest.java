package com.huskydreaming.claims.model.claim;

import com.huskydreaming.claims.model.helpers.AreaClaimHelper;
import com.huskydreaming.claims.model.position.BlockPosition;
import com.huskydreaming.claims.model.position.ChunkPosition;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AreaClaimTest {

    @Test
    void touchedChunksSpansMultipleChunksIncludingNegative() {
        var claim = AreaClaimHelper.areaClaim(
                0, 0, 0,
                63, 10, 63
        );

        var touched = AreaClaimHelper.toSet(claim.touchedChunks());

        assertTrue(touched.contains(new ChunkPosition(0, 0)));
        assertTrue(touched.contains(new ChunkPosition(1, 0)));
        assertTrue(touched.contains(new ChunkPosition(0, 1)));
        assertTrue(touched.contains(new ChunkPosition(1, 1)));
        assertEquals(4, touched.size(), "Expected 2x2 chunks touched");
    }

    @Test
    void touchedChunksHandlesNegativeCoordinatesCorrectly() {
        var claim = AreaClaimHelper.areaClaim(
                -1, 0, -1,
                0, 10, 0
        );

        var touched = AreaClaimHelper.toSet(claim.touchedChunks());

        assertTrue(touched.contains(new ChunkPosition(-1, -1)));
        assertTrue(touched.contains(new ChunkPosition(0, -1)));
        assertTrue(touched.contains(new ChunkPosition(-1, 0)));
        assertTrue(touched.contains(new ChunkPosition(0, 0)));
        assertEquals(4, touched.size(), "Expected 2x2 chunks touched");
    }

    @Test
    void touchedChunksInclusiveMaxBoundaryCase() {

        var claim = AreaClaimHelper.areaClaim(
                0, 0, 0,
                32, 10, 32
        );

        var touched = AreaClaimHelper.toSet(claim.touchedChunks());

        assertTrue(touched.contains(new ChunkPosition(0, 0)));
        assertTrue(touched.contains(new ChunkPosition(1, 0)));
        assertTrue(touched.contains(new ChunkPosition(0, 1)));
        assertTrue(touched.contains(new ChunkPosition(1, 1)));
        assertEquals(4, touched.size());
    }

    @Test
    void touchedChunksCoversAllSampledBlocksInsideBox() {
        var claim = AreaClaimHelper.areaClaim(
                -70, 5, -10,
                90, 77, 130
        );

        var touched = AreaClaimHelper.toSet(claim.touchedChunks());

        for (var x : new int[]{-70, -69, -33, -32, -31, 0, 31, 32, 33, 89, 90}) {
            for (var z : new int[]{-10, -9, -1, 0, 31, 32, 63, 64, 129, 130}) {
                var p = new BlockPosition(x, 10, z);
                assertTrue(claim.contains(p), "Sample should be inside bounding box: " + p);

                var chunk = ChunkPosition.fromBlock(p);
                assertTrue(touched.contains(chunk), "Missing touched chunk " + chunk + " for block " + p);
            }
        }
    }

    @Test
    void intersectsFalseIfDifferentWorld() {
        var a = AreaClaimHelper.areaClaim(UUID.randomUUID(), 0, 0, 0, 10, 10, 10);
        var b = AreaClaimHelper.areaClaim(UUID.randomUUID(), 0, 0, 0, 10, 10, 10);

        assertFalse(a.intersects(b));
        assertFalse(b.intersects(a));
    }
}
package com.huskydreaming.claims.model.claim;

import com.huskydreaming.claims.enumeration.ClaimFlag;
import com.huskydreaming.claims.model.position.BlockPosition;
import com.huskydreaming.claims.model.position.ChunkPosition;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChunkClaimTest {

    @Test
    void allowsReflectsBitMask() {
        UUID worldId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        int mask = ClaimFlag.BUILD.getBit() | ClaimFlag.INTERACT.getBit();

        BlockPosition blockPosition = new BlockPosition(0, 0, 0);
        ChunkPosition chunkPosition = ChunkPosition.fromBlock(blockPosition);

        ChunkClaim claim = new ChunkClaim(worldId, ownerId, chunkPosition, mask);

        assertTrue(claim.allows(ClaimFlag.BUILD));
        assertTrue(claim.allows(ClaimFlag.INTERACT));

        assertFalse(claim.allows(ClaimFlag.BREAK));
        assertFalse(claim.allows(ClaimFlag.CONTAINERS));
        assertFalse(claim.allows(ClaimFlag.PVP));
        assertFalse(claim.allows(ClaimFlag.EXPLOSIONS));
        assertFalse(claim.allows(ClaimFlag.PROJECTILES));
    }

    @Test
    void canOwnerAlwaysAllowedEvenIfMaskIsZero() {
        UUID worldId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        BlockPosition blockPosition = new BlockPosition(5, 0, -2);
        ChunkPosition chunkPosition = ChunkPosition.fromBlock(blockPosition);

        ChunkClaim claim = new ChunkClaim(worldId, ownerId, chunkPosition, 0);

        for (ClaimFlag flag : ClaimFlag.values()) {
            assertTrue(
                    claim.can(ownerId, flag),
                    "Owner should always be allowed for flag " + flag
            );
        }
    }

    @Test
    void canNonOwnerDependsOnMask() {
        UUID worldId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID nonOwnerId = UUID.randomUUID();

        int mask = ClaimFlag.INTERACT.getBit() | ClaimFlag.CONTAINERS.getBit();

        BlockPosition blockPosition = new BlockPosition(0, 0, 0);
        ChunkPosition chunkPosition = ChunkPosition.fromBlock(blockPosition);

        ChunkClaim claim = new ChunkClaim(worldId, ownerId, chunkPosition, mask);

        assertTrue(claim.can(nonOwnerId, ClaimFlag.INTERACT));
        assertTrue(claim.can(nonOwnerId, ClaimFlag.CONTAINERS));

        assertFalse(claim.can(nonOwnerId, ClaimFlag.BUILD));
        assertFalse(claim.can(nonOwnerId, ClaimFlag.BREAK));
        assertFalse(claim.can(nonOwnerId, ClaimFlag.PVP));
        assertFalse(claim.can(nonOwnerId, ClaimFlag.EXPLOSIONS));
        assertFalse(claim.can(nonOwnerId, ClaimFlag.PROJECTILES));
    }
}

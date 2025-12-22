package com.huskydreaming.claims.model.claim;

import com.huskydreaming.claims.enumeration.ClaimFlag;
import com.huskydreaming.claims.model.owners.PlayerOwner;
import com.huskydreaming.claims.model.position.ChunkPosition;
import com.huskydreaming.claims.model.world.WorldChunk;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ChunkClaimTest {

    @Test
    void allowsReflectsBitMask() {
        var owner = new PlayerOwner(UUID.randomUUID());
        var worldChunk = new WorldChunk(UUID.randomUUID(), new ChunkPosition(0, 0));

        var mask = ClaimFlag.BUILD.getBit() | ClaimFlag.INTERACT.getBit();
        var claim = new ChunkClaim(owner, worldChunk, mask);

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
        var ownerId = UUID.randomUUID();
        var owner = new PlayerOwner(ownerId);

        var worldChunk = new WorldChunk(UUID.randomUUID(), new ChunkPosition(5, -2));
        var claim = new ChunkClaim(owner, worldChunk, 0);

        assertTrue(claim.can(ownerId, ClaimFlag.BUILD));
        assertTrue(claim.can(ownerId, ClaimFlag.BREAK));
        assertTrue(claim.can(ownerId, ClaimFlag.INTERACT));
        assertTrue(claim.can(ownerId, ClaimFlag.CONTAINERS));
        assertTrue(claim.can(ownerId, ClaimFlag.PVP));
        assertTrue(claim.can(ownerId, ClaimFlag.EXPLOSIONS));
        assertTrue(claim.can(ownerId, ClaimFlag.PROJECTILES));
    }

    @Test
    void canNonOwnerDependsOnMask() {
        var owner = new PlayerOwner(UUID.randomUUID());
        var nonOwnerId = UUID.randomUUID();

        var worldChunk = new WorldChunk(UUID.randomUUID(), new ChunkPosition(0, 0));

        var mask = ClaimFlag.INTERACT.getBit() | ClaimFlag.CONTAINERS.getBit();
        var claim = new ChunkClaim(owner, worldChunk, mask);

        assertTrue(claim.can(nonOwnerId, ClaimFlag.INTERACT));
        assertTrue(claim.can(nonOwnerId, ClaimFlag.CONTAINERS));

        assertFalse(claim.can(nonOwnerId, ClaimFlag.BUILD));
        assertFalse(claim.can(nonOwnerId, ClaimFlag.BREAK));
        assertFalse(claim.can(nonOwnerId, ClaimFlag.PVP));
        assertFalse(claim.can(nonOwnerId, ClaimFlag.EXPLOSIONS));
        assertFalse(claim.can(nonOwnerId, ClaimFlag.PROJECTILES));
    }
}

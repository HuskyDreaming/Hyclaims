package com.huskydreaming.claims.model.claim;

import com.huskydreaming.claims.enumeration.ClaimFlag;
import com.huskydreaming.claims.model.position.ChunkPosition;

import java.util.Objects;
import java.util.UUID;

public record ChunkClaim(
        UUID worldId,
        UUID ownerId,
        ChunkPosition chunkPosition,
        int flagMask
) {

    public ChunkClaim {
        Objects.requireNonNull(worldId, "worldId");
        Objects.requireNonNull(ownerId, "ownerId");
    }

    public boolean allows(ClaimFlag flag) {
        Objects.requireNonNull(flag, "flag");
        return (flagMask & flag.getBit()) != 0;
    }

    public boolean can(UUID playerId, ClaimFlag flag) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(flag, "flag");
        return ownerId.equals(playerId) || allows(flag);
    }
}

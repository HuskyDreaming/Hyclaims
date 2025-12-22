package com.huskydreaming.claims.model.claim;

import com.huskydreaming.claims.enumeration.ClaimFlag;
import com.huskydreaming.claims.model.owners.ClaimOwner;
import com.huskydreaming.claims.model.position.BlockPosition;
import com.huskydreaming.claims.model.position.BoundingBox;
import com.huskydreaming.claims.model.position.ChunkPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record AreaClaim(UUID worldId, BoundingBox boundingBox, ClaimOwner claimOwner, int flagMask, int priority) {

    public AreaClaim {
        Objects.requireNonNull(worldId, "worldId");
        Objects.requireNonNull(boundingBox, "boundingBox");
        Objects.requireNonNull(claimOwner, "claimOwner");
    }

    public boolean allows(ClaimFlag flag) {
        return (flagMask & flag.getBit()) != 0;
    }

    public boolean contains(BlockPosition position) {
        return boundingBox.contains(position);
    }

    public boolean intersects(AreaClaim other) {
        if (!worldId.equals(other.worldId())) return false;
        return boundingBox.intersects(other.boundingBox());
    }

    public boolean can(UUID playerId, ClaimFlag flag) {
        if (claimOwner.isOwner(playerId)) return true;
        return allows(flag);
    }

    /**
     * Returns all chunk columns touched by this claim (X/Z only).
     *
     * <p>Assumes {@link BoundingBox} max values are inclusive.</p>
     */
    public Iterable<ChunkPosition> touchedChunks() {
        var minChunkX = Math.floorDiv(boundingBox.minX(), ChunkPosition.CHUNK_SIZE);
        var minChunkZ = Math.floorDiv(boundingBox.minZ(), ChunkPosition.CHUNK_SIZE);

        var maxChunkX = Math.floorDiv(boundingBox.maxX(), ChunkPosition.CHUNK_SIZE);
        var maxChunkZ = Math.floorDiv(boundingBox.maxZ(), ChunkPosition.CHUNK_SIZE);

        if (maxChunkX < minChunkX || maxChunkZ < minChunkZ) {
            return List.of();
        }

        var xCount = (maxChunkX - minChunkX) + 1;
        var zCount = (maxChunkZ - minChunkZ) + 1;

        var out = new ArrayList<ChunkPosition>(xCount * zCount);

        for (var cx = minChunkX; cx <= maxChunkX; cx++) {
            for (var cz = minChunkZ; cz <= maxChunkZ; cz++) {
                out.add(new ChunkPosition(cx, cz));
            }
        }
        return out;
    }
}
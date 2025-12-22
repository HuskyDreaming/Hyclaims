package com.huskydreaming.claims.model.helpers;

import com.huskydreaming.claims.model.claim.AreaClaim;
import com.huskydreaming.claims.model.owners.PlayerOwner;
import com.huskydreaming.claims.model.position.BoundingBox;
import com.huskydreaming.claims.model.position.ChunkPosition;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AreaClaimHelper {

    public static AreaClaim areaClaim(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return areaClaim(UUID.randomUUID(), minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static AreaClaim areaClaim(UUID worldId, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        var owner = new PlayerOwner(UUID.randomUUID());
        var box = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        return new AreaClaim(worldId, box, owner, 0, 0);
    }

    public static Set<ChunkPosition> toSet(Iterable<ChunkPosition> iterable) {
        var out = new HashSet<ChunkPosition>();
        for (var c : iterable) {
            out.add(c);
        }
        return out;
    }
}

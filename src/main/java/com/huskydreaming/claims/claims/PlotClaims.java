package com.huskydreaming.claims.claims;

import com.huskydreaming.claims.helpers.SpatialGrid;
import com.huskydreaming.claims.model.claim.PlotClaim;
import com.huskydreaming.claims.model.position.BlockPosition;
import com.huskydreaming.claims.model.position.BoundingBox;
import com.huskydreaming.claims.model.position.ChunkPosition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PlotClaims {

    private final UUID worldId;

    private final Map<Long, List<PlotClaim>> plotsByChunk = new ConcurrentHashMap<>();

    public PlotClaims(UUID worldId) {
        this.worldId = Objects.requireNonNull(worldId, "worldId");
    }

    public UUID worldId() {
        return worldId;
    }


    public boolean add(PlotClaim plot) {
        Objects.requireNonNull(plot, "plot");

        if (!plot.worldId().equals(worldId)) {
            throw new IllegalArgumentException("Plot belongs to a different world");
        }

        BoundingBox bounds = plot.bounds();
        for (PlotClaim existing : getPotentialOverlaps(bounds)) {
            if (existing.bounds().intersects(bounds)) {
                return false;
            }
        }

        for (ChunkPosition chunkPosition : SpatialGrid.touchedChunks(bounds)) {
            long key = SpatialGrid.chunkKey(chunkPosition);
            plotsByChunk.computeIfAbsent(key, l -> new CopyOnWriteArrayList<>())
                    .add(plot);
        }

        return true;
    }

    public boolean remove(PlotClaim plot) {
        Objects.requireNonNull(plot, "plot");

        boolean removed = false;

        for (ChunkPosition chunkPosition : SpatialGrid.touchedChunks(plot.bounds())) {
            long key = SpatialGrid.chunkKey(chunkPosition);
            List<PlotClaim> list = plotsByChunk.get(key);
            if (list == null) continue;

            removed |= list.remove(plot);
            if (list.isEmpty()) {
                plotsByChunk.remove(key);
            }
        }

        return removed;
    }

    public PlotClaim getAt(BlockPosition position) {
        Objects.requireNonNull(position, "position");

        long key = SpatialGrid.chunkKeyFromBlock(position.x(), position.z());
        List<PlotClaim> candidates = plotsByChunk.get(key);
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        PlotClaim best = null;
        for (PlotClaim plot : candidates) {
            if (!plot.bounds().contains(position)) continue;

            if (best == null || plot.priority() > best.priority()) {
                best = plot;
            }
        }

        return best;
    }

    private Set<PlotClaim> getPotentialOverlaps(BoundingBox bounds) {
        Set<PlotClaim> out = new HashSet<>();

        for (ChunkPosition chunkPosition : SpatialGrid.touchedChunks(bounds)) {
            List<PlotClaim> list = plotsByChunk.get(SpatialGrid.chunkKey(chunkPosition));
            if (list != null) {
                out.addAll(list);
            }
        }

        return out;
    }

    public void clear() {
        plotsByChunk.clear();
    }
}

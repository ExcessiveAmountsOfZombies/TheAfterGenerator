package com.omniverse.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import com.omniverse.aftergenerator.generation.WorldManipulator;
import com.omniverse.aftergenerator.objects.StructureLocation;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import net.minecraft.server.v1_15_R1.ChunkCoordIntPair;
import org.bukkit.Chunk;

import java.util.Arrays;
import java.util.List;

public class AfterStronghold extends AfterStructure {

    private List<ChunkCoordIntPair> pairs = null;

    public AfterStronghold(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.STRONGHOLD);
    }


    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        if (pairs == null) {
            pairs = Arrays.asList(manipulator.getStrongholdLocations(chunk.getWorld()));
        }
        ChunkCoordIntPair pair = new ChunkCoordIntPair(chunk.getX(), chunk.getZ());
        return pairs.contains(pair);
    }
}

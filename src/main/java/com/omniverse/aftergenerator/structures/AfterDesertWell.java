package com.omniverse.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import com.omniverse.aftergenerator.generation.WorldManipulator;
import com.omniverse.aftergenerator.objects.StructureLocation;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterDesertWell extends AfterStructure {

    public AfterDesertWell(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.DESERT_WELL);
    }

    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        return random.nextDouble() < 0.02 && structuresEnum.getBiomesAllowed().contains(getBiome(chunk));
    }
}

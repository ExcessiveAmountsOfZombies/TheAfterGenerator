package com.omniverse.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import com.omniverse.aftergenerator.generation.WorldManipulator;
import com.omniverse.aftergenerator.objects.StructureLocation;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterJungleTemple extends AfterStructure {

    public AfterJungleTemple(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.JUNGLE_TEMPLE);
    }

    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        boolean allowedToGenerate = isAllowedToGenerate(20, 32, 20, 32, chunk, structureLocations, StructuresEnum.JUNGLE_TEMPLE);
        return allowedToGenerate && random.nextDouble() < 0.04 && structuresEnum.getBiomesAllowed().contains(getBiome(chunk));
    }
}

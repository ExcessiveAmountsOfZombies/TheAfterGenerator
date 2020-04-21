package com.omniverse.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import com.omniverse.aftergenerator.generation.WorldManipulator;
import com.omniverse.aftergenerator.objects.StructureLocation;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterPillagerOutpost extends AfterStructure {


    public AfterPillagerOutpost(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.PILLAGER_OUTPOST);
    }


    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        boolean allowedToGenerate = isAllowedToGenerate(6, 32, 6, 32, chunk, structureLocations, StructuresEnum.VILLAGE,
                StructuresEnum.PILLAGER_OUTPOST);
        return allowedToGenerate && random.nextDouble() < 0.02 && structuresEnum.getBiomesAllowed().contains(getBiome(chunk));
    }
}

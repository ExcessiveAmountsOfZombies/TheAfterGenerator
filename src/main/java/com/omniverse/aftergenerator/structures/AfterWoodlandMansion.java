package com.omniverse.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import com.omniverse.aftergenerator.generation.WorldManipulator;
import com.omniverse.aftergenerator.objects.StructureLocation;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterWoodlandMansion extends AfterStructure {


    public AfterWoodlandMansion(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.WOODLAND_MANSION);
    }


    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        boolean allowedToGenerate = isAllowedToGenerate(48, 64, 48, 64, chunk, structureLocations, StructuresEnum.WOODLAND_MANSION);
        return allowedToGenerate && random.nextDouble() < 0.005 && structuresEnum.getBiomesAllowed().contains(getBiome(chunk));
    }
}

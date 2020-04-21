package com.omniverse.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import com.omniverse.aftergenerator.generation.WorldManipulator;
import com.omniverse.aftergenerator.objects.StructureLocation;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterMineshaft extends AfterStructure {

    public AfterMineshaft(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.MINESHAFT);
    }

    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        return random.nextDouble() < 0.006;
    }
}

package com.omniverse.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import com.omniverse.aftergenerator.generation.WorldManipulator;
import com.omniverse.aftergenerator.objects.StructureLocation;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;

import java.util.Random;

public abstract class AfterStructure {
    // Some structures will just be the base generateStructure, they don't need anything done to them because
    // they will already generate so rarely that they can be attempted in every chunk
    // so long as they meet the biome requirement


    WorldManipulator manipulator;
    StructuresEnum structuresEnum;
    Random random;

    public AfterStructure(WorldManipulator manipulator, StructuresEnum structuresEnum) {
        this.manipulator = manipulator;
        this.structuresEnum = structuresEnum;
        this.random = new Random();
    }


    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        // other classes will override this one
        return true;
    }

    public StructuresEnum getStructuresEnum() {
        return structuresEnum;
    }

    public Biome getBiome(Chunk chunk) {
        return chunk.getBlock(7, 64, 7).getBiome();
    }
}

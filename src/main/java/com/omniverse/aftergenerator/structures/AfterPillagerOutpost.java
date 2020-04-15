package com.omniverse.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import com.omniverse.aftergenerator.generation.WorldManipulator;
import com.omniverse.aftergenerator.objects.StructureLocation;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

import java.util.Set;

public class AfterPillagerOutpost extends AfterStructure {


    public AfterPillagerOutpost(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.PILLAGER_OUTPOST);
    }


    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        boolean allowedToGenerate = false;
        Set<StructureLocation> locations = structureLocations.get(StructuresEnum.VILLAGE);
        for (StructureLocation oneLocation : locations) {
            // the distance between the two spaces
            int x = Math.abs(oneLocation.getX() - chunk.getX());
            int z = Math.abs(oneLocation.getZ() - chunk.getZ());

            if ( (x <= 32 && x >= 6) && (z <= 32 && z >= 6) ) {
                allowedToGenerate = true;
            }
        }
        return allowedToGenerate && random.nextDouble() < 0.02 && structuresEnum.getBiomesAllowed().contains(getBiome(chunk));
    }
}

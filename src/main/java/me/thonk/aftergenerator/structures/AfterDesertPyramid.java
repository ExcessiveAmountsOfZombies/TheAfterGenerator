package me.thonk.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import me.thonk.aftergenerator.generation.WorldManipulator;
import me.thonk.aftergenerator.objects.StructureLocation;
import me.thonk.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterDesertPyramid extends AfterStructure {

    public AfterDesertPyramid(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.DESERT_PYRAMID);
    }

    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        boolean canGenerate = isAllowedToGenerate(8, 16, 8, 16, chunk, structureLocations, StructuresEnum.DESERT_PYRAMID);
        return canGenerate && random.nextDouble() < 0.006 && structuresEnum.getBiomesAllowed().contains(getBiome(chunk));
    }
}

package me.thonk.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import me.thonk.aftergenerator.generation.WorldManipulator;
import me.thonk.aftergenerator.objects.StructureLocation;
import me.thonk.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterOceanMonument extends AfterStructure {

    public AfterOceanMonument(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.OCEAN_MONUMENT);
    }

    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {

        boolean allowedToGenerate = isAllowedToGenerate(24, 32, 24, 32, chunk, structureLocations, StructuresEnum.OCEAN_MONUMENT);
        return allowedToGenerate && random.nextDouble() < 0.006 && structuresEnum.getBiomesAllowed().contains(getBiome(chunk));
    }
}

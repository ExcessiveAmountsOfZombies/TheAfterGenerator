package me.thonk.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import me.thonk.aftergenerator.generation.WorldManipulator;
import me.thonk.aftergenerator.objects.StructureLocation;
import me.thonk.aftergenerator.objects.StructuresEnum;
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

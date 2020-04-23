package me.thonk.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import me.thonk.aftergenerator.generation.WorldManipulator;
import me.thonk.aftergenerator.objects.StructureLocation;
import me.thonk.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterSwampHut extends AfterStructure {

    public AfterSwampHut(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.SWAMP_HUT);
    }


    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {

        boolean canGenerate = isAllowedToGenerate(32, 48, 32, 48, chunk, structureLocations, StructuresEnum.SWAMP_HUT);
        return canGenerate && random.nextDouble() < 0.06 && structuresEnum.getBiomesAllowed().contains(getBiome(chunk));
    }
}

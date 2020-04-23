package me.thonk.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import me.thonk.aftergenerator.generation.WorldManipulator;
import me.thonk.aftergenerator.objects.StructureLocation;
import me.thonk.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterVillage extends AfterStructure {

    public AfterVillage(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.VILLAGE);
    }


    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        boolean allowedToGenerate = isAllowedToGenerate(24, 32, 24, 32, chunk, structureLocations, StructuresEnum.VILLAGE,
                StructuresEnum.PILLAGER_OUTPOST);
        return allowedToGenerate && random.nextDouble() < 0.02 && structuresEnum.getBiomesAllowed().contains(getBiome(chunk));
    }
}

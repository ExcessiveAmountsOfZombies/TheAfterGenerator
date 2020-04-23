package me.thonk.aftergenerator.structures;

import com.google.common.collect.HashMultimap;
import me.thonk.aftergenerator.generation.WorldManipulator;
import me.thonk.aftergenerator.objects.StructureLocation;
import me.thonk.aftergenerator.objects.StructuresEnum;
import org.bukkit.Chunk;

public class AfterDungeon extends AfterStructure {


    public AfterDungeon(WorldManipulator manipulator) {
        super(manipulator, StructuresEnum.DUNGEON);
    }


    @Override
    public boolean generateStructure(HashMultimap<StructuresEnum, StructureLocation> structureLocations, Chunk chunk) {
        return super.generateStructure(structureLocations, chunk);
    }
}

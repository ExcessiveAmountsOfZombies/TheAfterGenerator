package me.thonk.aftergenerator;

import me.thonk.aftergenerator.commands.AfterGeneratorCommands;
import me.thonk.aftergenerator.generation.WorldManipulator;
import com.omniverse.aftergenerator.structures.*;
import me.thonk.aftergenerator.structures.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AfterGenerator extends JavaPlugin {


    private AfterGenerator plugin;
    private WorldManipulator manipulator;
    private AfterGeneratorCommands commands;


    private AfterDesertPyramid pyramid;
    private AfterDesertWell well;
    private AfterDungeon dungeon;
    private AfterJungleTemple jungleTemple;
    private AfterMineshaft mineshaft;
    private AfterOceanMonument oceanMonument;
    private AfterPillagerOutpost outpost;
    private AfterStronghold stronghold;
    private AfterSwampHut swampHut;
    private AfterVillage village;
    private AfterWoodlandMansion mansion;

    private List<AfterStructure> structureList;

    @Override
    public void onEnable() {
        plugin = this;

        manipulator = new WorldManipulator();
        commands = new AfterGeneratorCommands(this);
        getCommand("aftergenerator").setExecutor(commands);
        getCommand("aftergenerator").setTabCompleter(commands);


        pyramid = new AfterDesertPyramid(manipulator);
        well = new AfterDesertWell(manipulator);
        dungeon = new AfterDungeon(manipulator);
        jungleTemple = new AfterJungleTemple(manipulator);
        mineshaft = new AfterMineshaft(manipulator);
        oceanMonument = new AfterOceanMonument(manipulator);
        outpost = new AfterPillagerOutpost(manipulator);
        stronghold = new AfterStronghold(manipulator);
        swampHut = new AfterSwampHut(manipulator);
        village = new AfterVillage(manipulator);
        mansion = new AfterWoodlandMansion(manipulator);

        structureList = new ArrayList<>(Arrays.asList(pyramid, well, dungeon, jungleTemple, mineshaft,
                oceanMonument, outpost, stronghold, swampHut, village, mansion));

    }

    @Override
    public void onDisable() {


    }

    public AfterGenerator getPlugin() {
        return plugin;
    }

    public WorldManipulator getManipulator() {
        return manipulator;
    }

    public List<AfterStructure> getStructureList() {
        return structureList;
    }
}

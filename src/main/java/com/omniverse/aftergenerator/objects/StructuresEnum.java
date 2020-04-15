package com.omniverse.aftergenerator.objects;

import net.minecraft.server.v1_15_R1.WorldGenerator;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.bukkit.block.Biome.*;

public enum StructuresEnum {

    PILLAGER_OUTPOST("Pillager_Outpost", WorldGenerator.PILLAGER_OUTPOST, Arrays.asList(PLAINS, DESERT,
            TAIGA, SNOWY_TUNDRA, SNOWY_TAIGA, SUNFLOWER_PLAINS, TAIGA_HILLS, SNOWY_TAIGA_HILLS)),

    MINESHAFT("Mineshaft", WorldGenerator.MINESHAFT, Collections.emptyList()),

    // Todo needs to only generate in dark forests // needs a spacing of 64 chunks?
    WOODLAND_MANSION("Mansion", WorldGenerator.WOODLAND_MANSION, Arrays.asList(DARK_FOREST)),

    JUNGLE_TEMPLE("Jungle_Pyramid", WorldGenerator.JUNGLE_TEMPLE, Arrays.asList(JUNGLE, JUNGLE_HILLS,
            BAMBOO_JUNGLE, BAMBOO_JUNGLE_HILLS)),

    DESERT_PYRAMID("Desert_Pyramid", WorldGenerator.DESERT_PYRAMID, Arrays.asList(DESERT)),

    // TODO fix igloo from spawning at the bottom of the world
    //IGLOO("Igloo", WorldGenerator.IGLOO, Arrays.asList(SNOWY_TUNDRA, SNOWY_TAIGA)),

    // TODO fix shipwreck spawning at the bottom of the world
    /*SHIPWRECK("Shipwreck", WorldGenerator.SHIPWRECK, Arrays.asList(OCEAN, DEEP_OCEAN, FROZEN_OCEAN, DEEP_FROZEN_OCEAN,
            COLD_OCEAN, DEEP_COLD_OCEAN, LUKEWARM_OCEAN, DEEP_LUKEWARM_OCEAN, WARM_OCEAN, DEEP_WARM_OCEAN)),*/

    SWAMP_HUT("Swamp_Hut", WorldGenerator.SWAMP_HUT, Arrays.asList(SWAMP, SWAMP_HILLS)),

    STRONGHOLD("Stronghold", WorldGenerator.STRONGHOLD, Collections.emptyList()),

    OCEAN_MONUMENT("Monument", WorldGenerator.OCEAN_MONUMENT, Arrays.asList(DEEP_COLD_OCEAN, DEEP_FROZEN_OCEAN, DEEP_LUKEWARM_OCEAN, DEEP_OCEAN, DEEP_WARM_OCEAN)),

    // TODO fix ocean_ruin generating at bottom of the world
    /*OCEAN_RUIN("Ocean_Ruin", WorldGenerator.OCEAN_RUIN, Arrays.asList(OCEAN, DEEP_OCEAN, FROZEN_OCEAN, DEEP_FROZEN_OCEAN,
            COLD_OCEAN, DEEP_COLD_OCEAN, LUKEWARM_OCEAN, DEEP_LUKEWARM_OCEAN, WARM_OCEAN, DEEP_WARM_OCEAN)),*/

    // TODO: need to find a way to generate a village with its paths
    VILLAGE("Village", WorldGenerator.VILLAGE, Arrays.asList(PLAINS, DESERT, SAVANNA, TAIGA, SNOWY_TUNDRA)),

    // These Don't have tags on the chunk
    DESERT_WELL("Well", WorldGenerator.DESERT_WELL, Arrays.asList(DESERT)),

    //FOSSIL("Fossil", WorldGenerator.FOSSIL),

    DUNGEON("Dungeon", WorldGenerator.MONSTER_ROOM, Collections.emptyList());

    String actualName;
    WorldGenerator<?> generator;
    List<Biome> biomesAllowed;

    StructuresEnum(String structureName, WorldGenerator<?> generator, List<Biome> biomes) {
        this.actualName = structureName;
        this.generator = generator;
        this.biomesAllowed = biomes;
    }


    public String getActualName() {
        return actualName;
    }

    public WorldGenerator<?> getGenerator() {
        return generator;
    }

    public List<Biome> getBiomesAllowed() {
        return biomesAllowed;
    }
}

package com.omniverse.aftergenerator.generation;

import com.google.common.collect.HashMultimap;
import com.omniverse.aftergenerator.AfterGenerator;
import com.omniverse.aftergenerator.objects.StructureLocation;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import com.omniverse.aftergenerator.structures.AfterStructure;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class StructureRunnable implements Runnable {

    private WorldManipulator manipulator;
    private AfterGenerator plugin;

    private BukkitTask taskID;
    private boolean completed = false;

    private int chunksProcessed = 0;
    private int structuresGenerated = 0;

    private World world;

    private int minChunkX;
    private int minChunkZ;
    private int maxChunkX;
    private int maxChunkZ;

    private int currentX;
    private int currentZ;

    private long timeStarted;

    private boolean currentlyWaiting;

    private HashMultimap<StructuresEnum, StructureLocation> structureLocations = HashMultimap.create();

    // Structure Runnable called from a command
    public StructureRunnable(AfterGenerator plugin, WorldManipulator manipulator, World world,
                             int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ)  {
        this(plugin, manipulator, world);

        this.minChunkX = Math.min(minBlockX, maxBlockX) >> 4;
        this.minChunkZ = Math.min(minBlockZ, maxBlockZ) >> 4;
        this.maxChunkX = Math.max(minBlockX, maxBlockX) >> 4;
        this.maxChunkZ = Math.max(minBlockZ, maxBlockZ) >> 4;
    }

    // Structure Runnable called when the server is restarting
    public StructureRunnable(AfterGenerator plugin, WorldManipulator manipulator, World world) {
        this.plugin = plugin;
        this.manipulator = manipulator;
        this.world = world;

        // if it's starting from a restart point the world will be null and it'll be reset in here
        boolean restarting = restartStructureProcessing();

        if (!restarting) {
            this.timeStarted = Instant.now().toEpochMilli();
            this.currentlyWaiting = false;
        }


    }

    @Override
    public void run() {
        if (currentlyWaiting) {
            currentlyWaiting = (world.getLoadedChunks().length < 1000);
        }

        int chunkCounter = 0;

        while (chunkCounter < 100) {
            if (currentZ > maxChunkZ) {
                plugin.getLogger().info("Structure generation for " + world.getName() + " has finished." + chunksProcessed + " total chunks searched.");
                plugin.getLogger().info("Total structures generated " + structureLocations.values().size());
                for (StructuresEnum structuresEnum : structureLocations.keySet()) {
                    plugin.getLogger().info("Generated: " + structureLocations.get(structuresEnum).size() + " of " + structuresEnum.getActualName());
                }
                long timeTaken = Instant.now().toEpochMilli() - timeStarted;
                plugin.getLogger().info("This process took: " + timeTaken / 1000 + " seconds.");
                deleteConfig();
                taskID.cancel();
                completed = true;
                return;
            }

            if (currentX > maxChunkX) {
                currentX = minChunkX;
                currentZ++;
            }

            Chunk chunk = world.getChunkAt(currentX, currentZ);
            chunk.load();
            // begin determining if structure should be generated in this chunk
            for (AfterStructure structure : plugin.getStructureList()) {
                if (structure.generateStructure(structureLocations, chunk)) {
                    manipulator.spawnStructureInWorld(world, new Location(world, chunk.getX() << 4, 64, chunk.getZ() << 4), structure.getStructuresEnum().name(), 1);
                    structureLocations.put(structure.getStructuresEnum(), new StructureLocation(currentX, currentZ, structure));
                }
            }


            chunk.unload();

            currentX++;
            chunksProcessed++;
            chunkCounter++;
        }



        // Create a checkpoint for ourselves so if the server crashes we have a point back here.
        if (chunksProcessed % 20000 == 0) {
            save();
            taskID.cancel();
            completed = true;
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }


        currentlyWaiting = world.getLoadedChunks().length > 10000;
    }

    public void setTaskID(BukkitTask taskID) {
        this.taskID = taskID;
    }

    public boolean restartStructureProcessing() {
        File file = new File(plugin.getDataFolder(), "process.yml");
        if (!file.exists()) {
            return false;
        }

        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }

        this.world = Bukkit.getWorld(configuration.getString("world"));
        this.currentX = configuration.getInt("currentX");
        this.currentZ = configuration.getInt("currentZ");

        this.minChunkX = configuration.getInt("minChunkX");
        this.minChunkZ = configuration.getInt("minChunkZ");

        this.maxChunkX = configuration.getInt("maxChunkX");
        this.maxChunkZ = configuration.getInt("maxChunkZ");

        this.timeStarted = configuration.getLong("timeStarted");

        plugin.getLogger().info("Restarting structure spawn process on " + world.getName());
        plugin.getLogger().info("at: " + currentX + ", z: " + currentZ);
        plugin.getLogger().info("ending at x: " + maxChunkX + ", z:" + maxChunkZ);
        plugin.getLogger().info("The X and Z are in chunk format.");
        plugin.getLogger().info("Started at " + Date.from(Instant.ofEpochMilli(timeStarted)));


        return true;
    }

    public void save() {
        File file = new File(plugin.getDataFolder(), "process.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        YamlConfiguration configuration = new YamlConfiguration();

        configuration.set("world", world.getName());
        configuration.set("currentX", currentX);
        configuration.set("currentZ", currentZ);
        configuration.set("minChunkX", minChunkX);
        configuration.set("minChunkZ", minChunkZ);
        configuration.set("maxChunkX", maxChunkX);
        configuration.set("maxChunkZ", maxChunkZ);
        configuration.set("timeStarted", timeStarted);
    }

    public void deleteConfig() {
        File file = new File(plugin.getDataFolder(), "process.yml");

        if (file.exists()) {
            file.delete();
        }
    }
}

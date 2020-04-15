package com.omniverse.aftergenerator.generation;

import com.omniverse.aftergenerator.objects.AfterChunkGenerator;
import com.omniverse.aftergenerator.objects.StructuresEnum;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.LongSet;
import org.bukkit.craftbukkit.v1_15_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import java.lang.reflect.Field;
import java.util.*;

public class WorldManipulator {


    /**
     * World Manipulator class for adding structure data to the world in places where there currently is none.
     */
    public WorldManipulator() {

    }

    /**
     *
     * @param craftWorld NMS Craft World
     * @param worldServer NMS World Server
     * @param aGenerator The structure that data that we will be adding to the chunk
     * @param randomPosition Random position so we can get what chunk it is in.
     * @param chunkToModify Currently unused, only the base chunk needs the structure start data.
     * @param baseChunk Base chunk that has the StrucureStart data placed into NBT. Other chunks will receive the long form of the
     *                  chunk int pair so they know where to look for the StructureStart data.
     * @param number
     * @param seed The Seed of the world
     */
    public void addStructureToChunk(CraftWorld craftWorld, WorldServer worldServer, StructureGenerator aGenerator,
                                    BlockPosition randomPosition, IChunkAccess chunkToModify,
                                    IChunkAccess baseChunk, int number, long seed) {


        BiomeManager manager = craftWorld.getHandle().d();
        ChunkGenerator gen = worldServer.getChunkProvider().getChunkGenerator();
        DefinedStructureManager structManage = worldServer.getDataManager().f();
        StructureGenerator thing = aGenerator;

        /*ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(randomPosition);
        ProtoChunkTickList<Block> blocks = new ProtoChunkTickList<>(Objects::isNull, chunkcoordintpair);
        ProtoChunkTickList<FluidType> fluids = new ProtoChunkTickList<>(Objects::isNull, chunkcoordintpair);*/
        /*protoChunk = new ProtoChunk(chunkcoordintpair, ChunkConverter.a, chunk.getSections(), blocks, fluids);*/

        StructureStart structureStart1;

        // Create an instance of the StructureStart
        StructureStart structureStart2 = thing.a()
                .create(thing, baseChunk.getPos().x, baseChunk.getPos().z, StructureBoundingBox.a(), number, seed);

        // Generate the structure's pieces so they can be stored into memory later.
        structureStart2.a(gen, structManage, baseChunk.getPos().x, baseChunk.getPos().z, worldServer.getBiome(randomPosition));
        structureStart1 = structureStart2.e() ? structureStart2 : StructureStart.a;

        // Store the structurestart onto the chunk, in the cache and later to memory when a save happens.
        chunkToModify.a(thing.b(), structureStart1);
    }

    public ChunkCoordIntPair[] getStrongholdLocations(org.bukkit.World world) {
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        StructuresEnum structure = StructuresEnum.STRONGHOLD;
        ChunkGenerator currentGenerator = worldServer.getChunkProvider().getChunkGenerator();
        // block position doesn't matter cause this is just to generate the stronghold coords if they don't exist
        ((WorldGenStronghold) structure.getGenerator()).getNearestGeneratedFeature(worldServer, currentGenerator,
                new BlockPosition(0, 0, 0), 100, false);
        Field pairs = null;
        try {
            pairs = worldServer.getClass().getSuperclass().getDeclaredField("strongholdCoords");
            pairs.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            return (ChunkCoordIntPair[]) pairs.get(worldServer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The method that will generate, and spawn the typed structure into the world, fully complete similar to how it would spawn
     * in vanilla.
     * @param world Bukkit World
     * @param location Bukkit Location
     * @param type The type of structure that will be spawned
     * @param number random number
     */

    public boolean spawnStructureInWorld(org.bukkit.World world, Location location, String type, int number) {
        boolean generatedStructure = false;
        StructuresEnum structure = null;
        try {
            structure = StructuresEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

        WorldServer worldServer = ((CraftWorld) world).getHandle();
        CraftWorld craftWorld = (CraftWorld) world;
        Chunk chunk = ((CraftChunk) location.getChunk()).getHandle();

        ChunkGenerator oldGenerator = worldServer.getChunkProvider().getChunkGenerator();
        ChunkGenerator currentGenerator = null;

        if (structure.equals(StructuresEnum.VILLAGE) && !(oldGenerator instanceof AfterChunkGenerator)) {
            AfterChunkGenerator chunkGenerator = new AfterChunkGenerator(worldServer,
                    worldServer.getChunkProvider().chunkGenerator.getWorldChunkManager(), new GeneratorSettingsOverworld());
            changeChunkGenerator(worldServer, chunkGenerator);
            currentGenerator = chunkGenerator;
        } else {
            currentGenerator = worldServer.getChunkProvider().getChunkGenerator();
        }


        BlockPosition position = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        // Get corners of the bukkit chunk.
        int x = chunk.getBukkitChunk().getX() << 4;
        int z = chunk.getBukkitChunk().getZ() << 4;


        WorldGenFeatureConfiguration configuration = null;
        WorldGenerator aGenerator = structure.getGenerator();


        if (aGenerator instanceof StructureGenerator) {
            addStructureDataToChunk(chunk, structure.getActualName(),
                    ChunkCoordIntPair.pair(chunk.bukkitChunk.getX(), chunk.bukkitChunk.getZ()));
        }

        Set<BlockPosition> accesses = new HashSet<>();
        Set<IChunkAccess> chunkAccesses = new HashSet<>();

        Random random = new Random();
        for (int chancePerChunk = 8; chancePerChunk >= 1; chancePerChunk--) {
            int firstLevel = 255 / chancePerChunk;
            int randomX = random.nextInt(15);
            int randomZ = random.nextInt(15);
            BlockPosition randomPosition;

            x += randomX;
            z += randomZ;
            randomPosition = new BlockPosition(x, firstLevel, z);
            x -= randomX;
            z -= randomZ;
            if (structure.equals(StructuresEnum.VILLAGE)) {
                randomPosition = chunk.getWorld().getHighestBlockYAt(HeightMap.Type.WORLD_SURFACE, randomPosition);
            }

            if (!worldServer.getChunkProvider().getWorld().getWorldData().shouldGenerateMapFeatures()) {
                worldServer.getChunkProvider().getWorld().getWorldData().f(true);
                if (structure.equals(StructuresEnum.STRONGHOLD)) {
                    // need to generate the chunkcoordintpairs that give the location for all the strongholds in the world.
                    ((WorldGenStronghold) structure.getGenerator()).getNearestGeneratedFeature(worldServer, currentGenerator,
                            new BlockPosition(location.getX(), location.getY(), location.getZ()), 100, false);
                }
            }

            // update the heightmaps for the chunks.
            chunk.heightMap.put(HeightMap.Type.OCEAN_FLOOR_WG, new HeightMap(chunk, HeightMap.Type.OCEAN_FLOOR_WG));
            chunk.heightMap.put(HeightMap.Type.WORLD_SURFACE_WG, new HeightMap(chunk, HeightMap.Type.WORLD_SURFACE));

            int coordX = randomPosition.getX() >> 4;
            int coordZ = randomPosition.getZ() >> 4;

            if (aGenerator instanceof StructureGenerator) {
                addStructureToChunk(craftWorld, worldServer,
                        (StructureGenerator) aGenerator, randomPosition, chunk, chunk, number, craftWorld.getSeed());

                StructureStart structureStart = chunk.a(structure.getActualName());

                BlockPosition startPosition = new BlockPosition(structureStart.c().a, structureStart.c().b, structureStart.c().c);
                BlockPosition endPosition = new BlockPosition(structureStart.c().d, structureStart.c().e, structureStart.c().f);

                Chunk startChunk = worldServer.getChunkAtWorldCoords(startPosition);
                Chunk endChunk = worldServer.getChunkAtWorldCoords(endPosition);
            /*System.out.println("Information about the structure ");
            System.out.println("Bounding Box " + structureStart.c());
            System.out.println("Pieces used in structure" + structureStart.d().size());
            System.out.println(startPosition + " Starting Block Position ");
            System.out.println(endPosition + " Ending position ");
            System.out.println("Chunk Origin Start " + startChunk.getPos().x + "  " + startChunk.getPos().z + ".");
            System.out.println("Chunk Origin End   " + endChunk.getPos().x + "  " + endChunk.getPos().z + ".");
            System.out.println("Iterate Through the pieces ");*/


                Set<Chunk> chunksToUpdate = new HashSet<>();
                // What this does is it will iterate through all the pieces of a StructureStart, check their bounding boxes
                // and then add them to the list of chunks that will have their heightmaps updated
                // This might not even be necessary to do.
                for (StructurePiece piece : structureStart.d()) {
                    startPosition = new BlockPosition(piece.g().a, piece.g().b, piece.g().c);
                    endPosition = new BlockPosition(piece.g().d, piece.g().e, piece.g().f);
                    startChunk = worldServer.getChunkAtWorldCoords(startPosition);
                    endChunk = worldServer.getChunkAtWorldCoords(endPosition);

                /*System.out.println(startPosition + " Block Position ");
                System.out.println(endPosition + "   End   Position");*/
                /*System.out.println("Chunk Of Piece str " + startChunk.getPos().x + "  " + startChunk.getPos().z);
                System.out.println("Chunk of piece end " + endChunk.getPos().x +   "  " + endChunk.getPos().z);*/
                    chunksToUpdate.add(startChunk);
                    chunksToUpdate.add(endChunk);
                }


                int baseX = Math.abs(chunk.getPos().x);
                int baseZ = Math.abs(chunk.getPos().z);
                int radius = 0;

                // Next we will update the chunks heightmaps and use a radius of chunks that will be updated
                for (Chunk chunk1 : chunksToUpdate) {


                    chunk1.heightMap.put(HeightMap.Type.OCEAN_FLOOR_WG, new HeightMap(chunk, HeightMap.Type.OCEAN_FLOOR_WG));
                    chunk1.heightMap.put(HeightMap.Type.WORLD_SURFACE_WG, new HeightMap(chunk, HeightMap.Type.WORLD_SURFACE));

                    int absoluteX = Math.abs(chunk1.getPos().x);
                    int absoluteZ = Math.abs(chunk1.getPos().z);

                    int mathedX = Math.abs(baseX - absoluteX);
                    int mathedZ = Math.abs(baseZ - absoluteZ);
                    if (mathedX > radius) {
                        radius = mathedX;
                    } else if (mathedZ > radius) {
                        radius = mathedZ;
                    }


                }
            /*System.out.println("This is our radius: " + radius);
            System.out.println("We need to check " + radius * radius + " chunks");*/

                // now the chunks will be that are within the radius of the center will have the data of the origin chunk
                // added to them so that they can gain access to the StructureStart that is in the origin chunk.
                for (int xx = (chunk.getPos().x - radius); xx <= (chunk.getPos().x + radius); xx++) {
                    for (int zz = (chunk.getPos().z - radius); zz <= (chunk.getPos().z + radius); zz++) {
                        Chunk chunkAtCoords = worldServer.getChunkAt(xx, zz);
                        addStructureDataToChunk(chunkAtCoords, structure.getActualName(), ChunkCoordIntPair.pair(chunk.bukkitChunk.getX(), chunk.bukkitChunk.getZ()));
                        BlockPosition positionToUse = chunkAtCoords.getWorld().getHighestBlockYAt(HeightMap.Type.WORLD_SURFACE,
                                new BlockPosition(chunkAtCoords.getPos().x << 4, 0, chunkAtCoords.getPos().z << 4));
                        accesses.add(positionToUse);
                        chunkAccesses.add(chunkAtCoords);
                        chunkAtCoords.setLoaded(true);
                    }
                }
            } else {
                // this is a WorldGenerator so
                generatedStructure = aGenerator.generate(worldServer, worldServer.getChunkProvider().getChunkGenerator(),
                        new Random(), randomPosition, configuration);
                if (generatedStructure) {
                    break;
                }
            }
        }

        // now we'll actually generate the structure outside of the loop so that they don't generate several times
        // if it's a world generator this won't do anything
        for (BlockPosition position1 : accesses) {
            try {
                generatedStructure = aGenerator.generate(worldServer, worldServer.getChunkProvider().getChunkGenerator(),
                        new Random(), position1, configuration);
            } catch (NullPointerException e) {

            }
        }


        // refresh and save the chunks, refresh probably doesn't do anything
        for (IChunkAccess chunk1 : chunkAccesses) {
            craftWorld.refreshChunk(chunk1.getPos().x, chunk1.getPos().z);
            worldServer.getChunkProvider().playerChunkMap.saveChunk(chunk1);
            worldServer.unloadChunk((Chunk) chunk1);
        }

        // change the generator back to the old one so it doesn't mess with other structures
        changeChunkGenerator(worldServer, oldGenerator);
        return generatedStructure;
    }




    /**
     *
     * @param chunk the chunk that will have it's structure data modified.
     * @param typeToAdd the type of structure that will be added
     * @param baseChunk the "center" chunk the structure will generate around this is the chunk int pair in a long format
     *                  (-15, 0), (13, -5) etc.
     */

    public void addStructureDataToChunk(Chunk chunk, String typeToAdd, long baseChunk) {
        Map<String, LongSet> set = new HashMap<>(chunk.v());
        LongOpenHashSet hashSet = new LongOpenHashSet();

        hashSet.add(baseChunk);

        set.put(typeToAdd, hashSet);
        chunk.b(set);
    }

    /**
     * Method to change the chunk generator to our custom one for villages so that they will not generate inside of the world.
     * @param worldServer
     */
    public ChunkGenerator<?> changeChunkGenerator(WorldServer worldServer, ChunkGenerator newChunkGenerator) {
        ChunkGenerator generator = worldServer.getChunkProvider().chunkGenerator;
        if (!(generator instanceof AfterChunkGenerator)) {
            AfterChunkGenerator chunkGenerator = new AfterChunkGenerator(worldServer,
                    worldServer.getChunkProvider().chunkGenerator.getWorldChunkManager(), new GeneratorSettingsOverworld());
            Field field;
            try {
                field = worldServer.getChunkProvider().getClass().getDeclaredField("chunkGenerator");
                field.setAccessible(true);
                field.set(worldServer.getChunkProvider(), newChunkGenerator);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return generator;
    }

}

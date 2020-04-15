package com.omniverse.aftergenerator.objects;

import net.minecraft.server.v1_15_R1.*;

import java.util.function.Predicate;

public class AfterChunkGenerator extends ChunkProviderGenerate {

    WorldServer server;

    public AfterChunkGenerator(GeneratorAccess generatoraccess, WorldChunkManager worldchunkmanager,
                               GeneratorSettingsOverworld generatorsettingsoverworld) {
        super(generatoraccess, worldchunkmanager, generatorsettingsoverworld);
        this.server = (WorldServer) generatoraccess;
    }

    private Predicate<IBlockData> dataPredicate = (IBlockData::isAir);

    @Override
    public int getBaseHeight(int x, int z, HeightMap.Type heightmap_type) {
        // magic numbers based on ChunkGeneratorAbstract when it is initialized.
        int kInt = 4;
        int mInt = 32;
        int jInt = 10;
        IBlockData data = Blocks.GRASS_BLOCK.getBlockData();

        BlockPosition height = server.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, new BlockPosition(x, 0, z));

        int k = Math.floorDiv(x, kInt);
        int l = Math.floorDiv(z, kInt);
        int i1 = Math.floorMod(x, kInt);
        int j1 = Math.floorMod(z, kInt);
        double d0 = (double)i1 / (double)kInt;
        double d1 = (double)j1 / (double)kInt;
        double[][] adouble = new double[][]{this.b(k, l), this.b(k, l + 1), this.b(k + 1, l), this.b(k + 1, l + 1)};
        int k1 = this.getSeaLevel() + 20;

        for(int l1 = mInt - 1; l1 >= 0; --l1) {
            double d2 = adouble[0][l1];
            double d3 = adouble[1][l1];
            double d4 = adouble[2][l1];
            double d5 = adouble[3][l1];
            double d6 = adouble[0][l1 + 1];
            double d7 = adouble[1][l1 + 1];
            double d8 = adouble[2][l1 + 1];
            double d9 = adouble[3][l1 + 1];

            for(int i2 = jInt - 1; i2 >= 0; --i2) {
                double d10 = (double)i2 / (double)jInt;
                double d11 = MathHelper.a(d10, d0, d1, d2, d6, d4, d8, d3, d7, d5, d9);
                int j2 = l1 * jInt + i2;
                if (d11 > 0.0D || j2 < k1) {
                    IBlockData iblockdata;
                    if (d11 > 0.0D) {
                        iblockdata = this.f;
                    } else {
                        iblockdata = this.g;
                    }

                    if (dataPredicate.test(data)) {
                        return j2 + 1;
                    }
                }
            }
        }
        return height.getY();
    }

    public int b(int i, int j, HeightMap.Type heightmap_type) {
        return this.getBaseHeight(i, j, HeightMap.Type.OCEAN_FLOOR);
    }
}

package com.omniverse.aftergenerator.objects;

import com.omniverse.aftergenerator.structures.AfterStructure;

public class StructureLocation {

    private int x;
    private int z;
    public AfterStructure structureGenerated;

    public StructureLocation(int x, int z, AfterStructure structureGenerated) {
        this.x = x;
        this.z = z;
        this.structureGenerated = structureGenerated;
    }

    public AfterStructure getStructureGenerated() {
        return structureGenerated;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}

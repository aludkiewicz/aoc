package com.aoc;

import java.util.Objects;

public class Vector3D {

    private int x;
    private int y;
    private int z;

    public Vector3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void incrementX() {
        this.x = this.x + 1;
    }

    public void incrementY() {
        this.y = this.y + 1;
    }

    public void incrementZ() {
        this.z = this.z + 1;
    }

    public void decrementX() {
        this.x = this.x - 1;
    }

    public void decrementY() {
        this.y = this.y - 1;
    }

    public void decrementZ() {
        this.z = this.z - 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3D)) return false;
        Vector3D vector3D = (Vector3D) o;
        return getX() == vector3D.getX() &&
                getY() == vector3D.getY() &&
                getZ() == vector3D.getZ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }

    @Override
    public String toString() {
        return "x=" + x +
                ", y=" + y +
                ", z=" + z;
    }
}

package com.aoc;

public enum Direction {

    NORTH,
    WEST,
    SOUTH,
    EAST;

    public static Direction transitionFrom(Direction dir, int turn) {
        switch (dir) {
            case NORTH:
                if(turn == 0) {
                    return WEST;
                } else {
                    return EAST;
                }
            case WEST:
                if(turn == 0) {
                    return SOUTH;
                } else {
                    return NORTH;
                }
            case SOUTH:
                if(turn == 0) {
                    return EAST;
                } else {
                    return WEST;
                }
            case EAST:
                if(turn == 0) {
                    return NORTH;
                } else {
                    return SOUTH;
                }
        }
        throw new RuntimeException("Illegal direction!");
    }

}

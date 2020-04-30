package com.example.memoryblocks;

public enum Difficulty {
    EASY(4),
    MEDIUM(5),
    HARD(6);

    private final int value;

    Difficulty(int value) {
        this.value = value;
    }

    public int getDims() { return value; }

}

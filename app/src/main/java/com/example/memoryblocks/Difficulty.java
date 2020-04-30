package com.example.memoryblocks;

public enum Difficulty {
    BEGINNER(2),
    EASY(3),
    NORMAL(4),
    INTERMEDIATE(5),
    HARD(6),
    EXPERT(7);

    private final int value;

    Difficulty(int value) {
        this.value = value;
    }

    public int getDims() { return value; }

}

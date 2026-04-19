package com.snake.game.model;

import java.awt.Color;

public enum FoodType {
    NORMAL("普通", Color.RED, 10),
    GOLD("金色", Color.YELLOW, 50),
    BOMB("炸弹", Color.BLACK, -20);

    private final String name;
    private final Color color;
    private final int score;

    FoodType(String name, Color color, int score) {
        this.name = name;
        this.color = color;
        this.score = score;
    }

    public String getName() { return name; }
    public Color getColor() { return color; }
    public int getScore() { return score; }
}

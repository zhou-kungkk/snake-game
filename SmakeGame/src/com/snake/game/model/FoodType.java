package com.snake.game.model;

import java.awt.Color;

public enum FoodType {
    // 普通食物
    NORMAL("普通食物", Color.RED, 10, 1.0, false),

    // 金色食物（高分）
    GOLDEN("金色食物", Color.YELLOW, 50, 1.0, false),

    // 加速食物
    SPEED_UP("加速食物", Color.BLUE, 5, 0.7, true),

    // 减速食物
    SPEED_DOWN("减速食物", Color.ORANGE, 5, 1.5, true),

    // 双倍分数食物
    DOUBLE_SCORE("双倍分数", Color.MAGENTA, 0, 1.0, true),

    // 炸弹（会缩短蛇身）
    BOMB("炸弹", Color.BLACK, -20, 1.0, false);

    private final String name;
    private final Color color;
    private final int score;
    private final double speedEffect;  // 速度倍数
    private final boolean temporary;   // 是否是临时效果

    FoodType(String name, Color color, int score, double speedEffect, boolean temporary) {
        this.name = name;
        this.color = color;
        this.score = score;
        this.speedEffect = speedEffect;
        this.temporary = temporary;
    }

    public String getName() { return name; }
    public Color getColor() { return color; }
    public int getScore() { return score; }
    public double getSpeedEffect() { return speedEffect; }
    public boolean isTemporary() { return temporary; }
}

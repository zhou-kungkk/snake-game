package com.snake.game.model;

import java.awt.Color;

public enum SnakeSkin {
    // 默认皮肤
    CLASSIC("经典皮肤",
            Color.GREEN,
            new Color(0, 180, 0),
            Color.BLACK),

    // 霓虹皮肤
    NEON("霓虹皮肤",
            Color.CYAN,
            new Color(0, 255, 255),
            new Color(0, 0, 0, 100)),

    // 火焰皮肤
    FIRE("火焰皮肤",
            Color.RED,
            new Color(255, 165, 0),
            Color.DARK_GRAY),

    // 彩虹皮肤
    RAINBOW("彩虹皮肤",
            Color.YELLOW,
            new Color(255, 0, 255),
            Color.WHITE);

    private final String name;
    private final Color headColor;
    private final Color bodyColor;
    private final Color borderColor;

    SnakeSkin(String name, Color headColor, Color bodyColor, Color borderColor) {
        this.name = name;
        this.headColor = headColor;
        this.bodyColor = bodyColor;
        this.borderColor = borderColor;
    }

    public String getName() { return name; }
    public Color getHeadColor() { return headColor; }
    public Color getBodyColor() { return bodyColor; }
    public Color getBorderColor() { return borderColor; }
}

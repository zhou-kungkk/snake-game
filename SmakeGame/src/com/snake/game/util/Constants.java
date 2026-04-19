package com.snake.game.util;

import java.awt.Color;

public class Constants {
    // 游戏窗口设置
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final String WINDOW_TITLE = "贪吃蛇游戏";

    // 游戏网格设置
    public static final int GRID_SIZE = 20;
    public static final int GRID_WIDTH = 40;
    public static final int GRID_HEIGHT = 30;

    // 颜色设置
    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final Color SNAKE_HEAD_COLOR = Color.GREEN;
    public static final Color SNAKE_BODY_COLOR = new Color(0, 180, 0);
    public static final Color FOOD_COLOR = Color.RED;
    public static final Color GRID_COLOR = new Color(50, 50, 50);

    // 游戏速度
    public static final int GAME_SPEED = 150; // 毫秒
}

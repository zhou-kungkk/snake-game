package com.snake.game.model;

import java.util.Random;
import com.snake.game.util.Constants;

public class Food {
    private Point position;  // 食物位置
    private Random random;   // 随机数生成器

    public Food() {
        random = new Random();
        generateNewPosition();
    }

    /**
     * 随机生成食物位置
     */
    public void generateNewPosition() {
        int x = random.nextInt(Constants.GRID_WIDTH);
        int y = random.nextInt(Constants.GRID_HEIGHT);
        position = new Point(x, y);
    }

    /**
     * 生成不在蛇身上的食物位置
     */
    public void generateNewPosition(Snake snake) {
        boolean onSnake;
        do {
            generateNewPosition();
            onSnake = false;

            // 检查食物是否生成在蛇身上
            for (Point point : snake.getBody()) {
                if (point.getX() == position.getX() &&
                        point.getY() == position.getY()) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake);
    }

    /**
     * 获取食物位置
     */
    public Point getPosition() {
        return position;
    }

    /**
     * 获取食物X坐标
     */
    public int getX() {
        return position.getX();
    }

    /**
     * 获取食物Y坐标
     */
    public int getY() {
        return position.getY();
    }
}

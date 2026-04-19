package com.snake.game.model;

import java.util.Random;
import com.snake.game.util.Constants;

public class Food {
    private Point position;
    private Random random;
    private FoodType type;

    public Food() {
        random = new Random();
        generateRandomFood();
    }

    public void generateRandomFood() {
        int x = random.nextInt(Constants.GRID_WIDTH);
        int y = random.nextInt(Constants.GRID_HEIGHT);
        position = new Point(x, y);

        // 简单概率：70%普通，20%金色，10%炸弹
        double rand = random.nextDouble();
        if (rand < 0.7) {
            type = FoodType.NORMAL;
        } else if (rand < 0.9) {
            type = FoodType.GOLD;
        } else {
            type = FoodType.BOMB;
        }
    }

    public void generateRandomFood(Snake snake) {
        boolean onSnake;
        do {
            generateRandomFood();
            onSnake = false;

            for (Point point : snake.getBody()) {
                if (point.getX() == position.getX() &&
                        point.getY() == position.getY()) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake);
    }

    public Point getPosition() { return position; }
    public int getX() { return position.getX(); }
    public int getY() { return position.getY(); }
    public FoodType getType() { return type; }
}

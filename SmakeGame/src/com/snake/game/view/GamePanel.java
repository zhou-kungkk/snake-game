package com.snake.game.view;

import com.snake.game.model.Point;
import com.snake.game.model.Snake;
import com.snake.game.model.Food;
import com.snake.game.util.Constants;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Snake snake;
    private Food food;

    public GamePanel() {
        initPanel();
    }

    private void initPanel() {
        // 设置面板大小
        int panelWidth = Constants.GRID_WIDTH * Constants.GRID_SIZE;
        int panelHeight = Constants.GRID_HEIGHT * Constants.GRID_SIZE;
        setPreferredSize(new Dimension(panelWidth, panelHeight));

        // 设置面板背景色
        setBackground(Constants.BACKGROUND_COLOR);

        // 设置面板可获取焦点
        setFocusable(true);

        System.out.println("游戏面板创建完成！可聚焦: " + isFocusable());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawSnake(g);
        drawFood(g);
    }

    /**
     * 绘制网格
     */
    private void drawGrid(Graphics g) {
        g.setColor(Constants.GRID_COLOR);

        // 绘制垂直线
        for (int i = 0; i <= Constants.GRID_WIDTH; i++) {
            int x = i * Constants.GRID_SIZE;
            g.drawLine(x, 0, x, Constants.GRID_HEIGHT * Constants.GRID_SIZE);
        }

        // 绘制水平线
        for (int j = 0; j <= Constants.GRID_HEIGHT; j++) {
            int y = j * Constants.GRID_SIZE;
            g.drawLine(0, y, Constants.GRID_WIDTH * Constants.GRID_SIZE, y);
        }
    }

    /**
     * 绘制蛇
     */
    private void drawSnake(Graphics g) {
        if (snake == null) return;

        // 绘制蛇身
        for (int i = 0; i < snake.getBody().size(); i++) {
            Point point = snake.getBody().get(i);
            int x = point.getX() * Constants.GRID_SIZE;
            int y = point.getY() * Constants.GRID_SIZE;

            // 如果是蛇头，用不同颜色
            if (i == 0) {
                g.setColor(Constants.SNAKE_HEAD_COLOR);
            } else {
                g.setColor(Constants.SNAKE_BODY_COLOR);
            }

            g.fillRect(x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);

            // 绘制蛇身格子边框
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);
        }
    }

    /**
     * 绘制食物
     */
    private void drawFood(Graphics g) {
        if (food == null) return;

        g.setColor(Constants.FOOD_COLOR);
        int x = food.getX() * Constants.GRID_SIZE;
        int y = food.getY() * Constants.GRID_SIZE;

        // 绘制圆形食物
        g.fillOval(x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);

        // 绘制食物边框
        g.setColor(Color.WHITE);
        g.drawOval(x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);
    }

    /**
     * 设置蛇（这是GameController调用的关键方法！）
     */
    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    /**
     * 设置食物（这是GameController调用的关键方法！）
     */
    public void setFood(Food food) {
        this.food = food;
    }

    /**
     * 获取蛇对象
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * 获取食物对象
     */
    public Food getFood() {
        return food;
    }
}

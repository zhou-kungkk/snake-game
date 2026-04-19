package com.snake.game.view;

// 1. 精确导入AWT组件（避免使用 import java.awt.*）
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// 2. 明确导入您自定义的模型类
import com.snake.game.model.Point;
import com.snake.game.model.Snake;
import com.snake.game.model.Food;
import com.snake.game.model.FoodType;
import com.snake.game.model.SnakeSkin;
import com.snake.game.model.GameConfig;
import com.snake.game.util.Constants;

public class GamePanel extends JPanel {
    private Snake snake;
    private Food food;
    private SnakeSkin currentSkin; // 存储当前皮肤

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

        // 从全局配置获取当前皮肤
        currentSkin = GameConfig.getInstance().getCurrentSkin();

        System.out.println("游戏面板创建完成！当前皮肤：" + currentSkin.getName());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 是否绘制网格取决于配置
        if (GameConfig.getInstance().isShowGrid()) {
            drawGrid(g);
        }
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
     * 绘制蛇（使用皮肤系统）
     */
    private void drawSnake(Graphics g) {
        if (snake == null) return;

        // 从当前皮肤获取颜色
        Color headColor = currentSkin.getHeadColor();
        Color bodyColor = currentSkin.getBodyColor();
        Color borderColor = currentSkin.getBorderColor();

        // 绘制蛇身
        for (int i = 0; i < snake.getBody().size(); i++) {
            // 现在Point明确指向 com.snake.game.model.Point
            Point point = snake.getBody().get(i);
            int x = point.getX() * Constants.GRID_SIZE;
            int y = point.getY() * Constants.GRID_SIZE;

            // 设置颜色：蛇头用皮肤的头色，蛇身用皮肤的体色
            if (i == 0) {
                g.setColor(headColor);
            } else {
                g.setColor(bodyColor);
            }

            g.fillRect(x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);

            // 绘制蛇身格子边框（使用皮肤的边框色）
            g.setColor(borderColor);
            g.drawRect(x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);
        }
    }

    /**
     * 绘制食物
     */
    private void drawFood(Graphics g) {
        if (food == null) return;

        FoodType type = food.getType();
        int x = food.getX() * Constants.GRID_SIZE;
        int y = food.getY() * Constants.GRID_SIZE;

        // 根据食物类型设置颜色
        g.setColor(type.getColor());

        // 绘制食物
        g.fillOval(x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);

        // 绘制边框
        g.setColor(Color.WHITE);
        g.drawOval(x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);

        // 如果是金色食物，绘制星星
        if (type == FoodType.GOLD) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("★", x + Constants.GRID_SIZE/2 - 4, y + Constants.GRID_SIZE/2 + 4);
        }
        // 如果是炸弹，绘制感叹号
        else if (type == FoodType.BOMB) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("!", x + Constants.GRID_SIZE/2 - 2, y + Constants.GRID_SIZE/2 + 4);
        }
    }

    /**
     * 设置蛇
     */
    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    /**
     * 设置食物
     */
    public void setFood(Food food) {
        this.food = food;
    }

    /**
     * 更新当前皮肤并重绘
     */
    public void updateSkin() {
        // 从配置中重新获取皮肤
        this.currentSkin = GameConfig.getInstance().getCurrentSkin();
        // 请求重绘面板
        this.repaint();
        System.out.println("游戏面板皮肤已更新为：" + currentSkin.getName());
    }

    /**
     * 重新绘制网格（用于开关网格显示）
     */
    public void refreshGrid() {
        this.repaint();
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
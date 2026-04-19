package com.snake.game.controller;

import com.snake.game.model.*;
import com.snake.game.view.*;
import com.snake.game.util.Constants;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    // 游戏模型
    private Snake snake;
    private Food food;

    // 游戏视图
    private GamePanel gamePanel;
    private ScorePanel scorePanel;

    // 游戏控制
    private Timer timer;
    private GameStatus gameStatus;

    // 游戏数据
    private int score;
    private int length;

    public GameController(GamePanel gamePanel, ScorePanel scorePanel) {
        this.gamePanel = gamePanel;
        this.scorePanel = scorePanel;

        // 初始化游戏
        initGame();
    }

    /**
     * 初始化游戏
     */
    private void initGame() {
        // 创建蛇和食物
        snake = new Snake();
        food = new Food();

        // 初始状态
        gameStatus = GameStatus.PAUSED;
        score = 0;
        length = snake.getBody().size();

        // 生成不在蛇身上的食物
        food.generateRandomFood(snake);

        // 设置面板的蛇和食物
        gamePanel.setSnake(snake);
        gamePanel.setFood(food);

        // 创建游戏循环定时器（使用GameConfig中的速度，而不是固定的Constants.GAME_SPEED）
        int initialSpeed = GameConfig.getInstance().getInitialSpeed();
        timer = new Timer(initialSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameLoop();
            }
        });

        // 初始UI更新
        updateUI();

        System.out.println("游戏控制器初始化完成，初始速度: " + initialSpeed + "ms");
    }

    /**
     * 游戏主循环
     */
    private void gameLoop() {
        // 如果游戏不在运行状态，不执行逻辑
        if (gameStatus != GameStatus.RUNNING) {
            return;
        }

        // 移动蛇
        snake.move();

        // 检查是否吃到食物
        if (snake.checkEatFood(food.getPosition())) {
            // 根据食物类型获取分数
            FoodType currentFoodType = food.getType();
            int scoreToAdd = currentFoodType.getScore();

            // 处理食物效果
            if (currentFoodType == FoodType.BOMB) {
                // 炸弹：扣分，并让蛇缩短一节（如果长度大于3）
                score += scoreToAdd; // scoreToAdd 是负数，例如-20
                if (score < 0) score = 0; // 分数不低于0

                if (snake.getBody().size() > 3) {
                    // 移除蛇的最后一节
                    snake.getBody().remove(snake.getBody().size() - 1);
                }
            } else {
                // 普通或金色食物：加分，并让蛇增长
                score += scoreToAdd;
                snake.grow();
            }

            // 生成下一个食物
            food.generateRandomFood(snake);

            // 在状态栏更新吃了什么食物
            scorePanel.updateStatus("吃到: " + currentFoodType.getName() + " (" + scoreToAdd + "分)");

            // 播放音效
            playEatSound();
        }

        // 检查碰撞
        if (snake.checkWallCollision() || snake.checkSelfCollision()) {
            gameOver();
            return;
        }

        // 更新UI
        updateUI();
    }

    /**
     * 开始游戏
     */
    public void startGame() {
        if (gameStatus == GameStatus.PAUSED || gameStatus == GameStatus.GAME_OVER) {
            gameStatus = GameStatus.RUNNING;
            scorePanel.updateStatus("游戏中");
            timer.start();
            System.out.println("游戏开始，当前速度: " + timer.getDelay() + "ms");
        }
    }

    /**
     * 暂停游戏
     */
    public void pauseGame() {
        if (gameStatus == GameStatus.RUNNING) {
            gameStatus = GameStatus.PAUSED;
            scorePanel.updateStatus("已暂停");
            timer.stop();
            System.out.println("游戏暂停");
        } else if (gameStatus == GameStatus.PAUSED) {
            startGame(); // 继续游戏
        }
    }

    /**
     * 重新开始游戏
     */
    public void restartGame() {
        // 停止当前定时器
        timer.stop();

        // 重置游戏状态
        initGame();

        // 更新UI状态
        scorePanel.updateStatus("准备开始");

        System.out.println("游戏重新开始");
    }

    /**
     * 游戏结束（新增结算界面）
     */
    private void gameOver() {
        gameStatus = GameStatus.GAME_OVER;
        timer.stop();
        scorePanel.updateStatus("游戏结束 - 得分: " + score);

        // 播放游戏结束音效
        playGameOverSound();

        System.out.println("游戏结束，最终得分: " + score);

        // === 新增：弹出结算/重新开始对话框 ===
        // 使用 SwingUtilities.invokeLater 确保在事件分发线程中更新UI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 创建自定义的对话框面板
                JPanel panel = new JPanel(new BorderLayout(10, 10));

                // 显示游戏结束信息
                JLabel messageLabel = new JLabel(
                        "<html><div style='text-align: center;'>" +
                                "<h2>游戏结束！</h2>" +
                                "<p>最终得分: <b>" + score + "</b></p>" +
                                "<p>蛇身长度: <b>" + snake.getBody().size() + "</b></p>" +
                                "</div></html>",
                        SwingConstants.CENTER
                );
                panel.add(messageLabel, BorderLayout.CENTER);

                // 创建按钮面板
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                JButton restartButton = new JButton("重新开始 (R)");
                JButton quitButton = new JButton("退出游戏");

                // 重新开始按钮逻辑
                restartButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 获取对话框的顶层窗口并关闭它
                        Window dialog = SwingUtilities.getWindowAncestor((Component)e.getSource());
                        if (dialog != null) {
                            dialog.dispose();
                        }
                        // 调用重新开始游戏的方法
                        restartGame();
                    }
                });

                // 退出按钮逻辑
                quitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Window dialog = SwingUtilities.getWindowAncestor((Component)e.getSource());
                        if (dialog != null) {
                            dialog.dispose();
                        }
                        // 退出游戏
                        System.exit(0);
                    }
                });

                buttonPanel.add(restartButton);
                buttonPanel.add(quitButton);
                panel.add(buttonPanel, BorderLayout.SOUTH);
                panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

                // 显示对话框
                int option = JOptionPane.showOptionDialog(
                        gamePanel, // 父组件
                        panel,     // 消息
                        "游戏结束", // 标题
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        new Object[]{}, // 不显示默认按钮
                        null
                );
                // 即使点击对话框关闭按钮，也默认重新开始
                if (option == JOptionPane.CLOSED_OPTION) {
                    restartGame();
                }
            }
        });
    }

    /**
     * 更新UI显示
     */
    private void updateUI() {
        // 更新分数
        scorePanel.updateScore(score);

        // 更新长度
        length = snake.getBody().size();
        scorePanel.updateLength(length);

        // 重绘游戏面板
        gamePanel.repaint();
    }

    /**
     * 改变蛇的方向
     */
    public void changeDirection(Direction direction) {
        if (gameStatus == GameStatus.RUNNING) {
            snake.changeDirection(direction);
        }
    }

    /**
     * 播放吃食物音效
     */
    private void playEatSound() {
        // 简单的蜂鸣声
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    /**
     * 播放游戏结束音效
     */
    private void playGameOverSound() {
        // 三次蜂鸣声表示游戏结束
        for (int i = 0; i < 3; i++) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取游戏状态
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * 获取当前分数
     */
    public int getScore() {
        return score;
    }

    /**
     * 获取蛇的长度
     */
    public int getLength() {
        return length;
    }

    /**
     * 新增：更新游戏速度（供SettingsPanel调用）
     */
    public void updateGameSpeed(int newSpeed) {
        // 确保速度在合理范围内
        if (newSpeed < 50) newSpeed = 50;
        if (newSpeed > 300) newSpeed = 300;

        // 更新定时器的延迟
        timer.setDelay(newSpeed);
        timer.setInitialDelay(newSpeed);

        // 保存到配置
        GameConfig.getInstance().setInitialSpeed(newSpeed);

        System.out.println("游戏速度已更新为: " + newSpeed + "ms");
    }
}
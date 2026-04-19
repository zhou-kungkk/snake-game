package com.snake.game;

import com.snake.game.controller.GameController;
import com.snake.game.controller.KeyController;
import com.snake.game.view.GameFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        System.out.println("贪吃蛇创新版游戏启动中...");

        // 使用SwingUtilities确保线程安全
        SwingUtilities.invokeLater(() -> {
            try {
                // 创建游戏窗口
                GameFrame gameFrame = new GameFrame();

                // 创建游戏控制器
                GameController gameController = new GameController(
                        gameFrame.getGamePanel(),
                        gameFrame.getScorePanel()
                );

                // 关键：建立窗口与游戏控制器的关联，使设置窗口可暂停游戏
                gameFrame.setGameController(gameController);

                // 创建键盘控制器
                KeyController keyController = new KeyController(gameController);

                // 为窗口设置键盘控制器
                gameFrame.setKeyController(keyController);

                // 添加窗口事件监听器
                gameFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowActivated(WindowEvent e) {
                        gameFrame.refocus();
                    }
                });

                // 显示窗口
                gameFrame.start();

                // 游戏提示信息
                System.out.println("贪吃蛇创新版启动完成！");
                System.out.println("控制说明：方向键/WASD移动，空格暂停，R重新开始");

                // 延迟2秒后自动开始游戏
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        SwingUtilities.invokeLater(() -> {
                            gameController.startGame();
                            System.out.println("游戏已开始！");
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("启动游戏时发生错误: " + e.getMessage());
            }
        });
    }
}
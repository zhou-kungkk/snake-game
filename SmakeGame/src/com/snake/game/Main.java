package com.snake.game;

import com.snake.game.controller.GameController;
import com.snake.game.controller.KeyController;
import com.snake.game.view.GameFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        System.out.println("贪吃蛇游戏项目启动中...");

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

                // 创建键盘控制器
                KeyController keyController = new KeyController(gameController);

                // 为窗口设置键盘控制器
                gameFrame.setKeyController(keyController);

                // 添加窗口事件监听器，当窗口激活时重新获取焦点
                gameFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowActivated(WindowEvent e) {
                        System.out.println("窗口激活，重新获取焦点");
                        gameFrame.refocus();
                    }
                });

                // 显示窗口
                gameFrame.start();

                // 启动游戏
                System.out.println("游戏启动中...");
                System.out.println("控制说明：");
                System.out.println("  ↑/W: 向上移动");
                System.out.println("  ↓/S: 向下移动");
                System.out.println("  ←/A: 向左移动");
                System.out.println("  →/D: 向右移动");
                System.out.println("  空格: 暂停/继续游戏");
                System.out.println("  Enter: 开始游戏");
                System.out.println("  R: 重新开始游戏");
                System.out.println("  ESC: 退出游戏");

                // 延迟3秒后自动开始游戏
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        SwingUtilities.invokeLater(() -> {
                            gameController.startGame();
                            System.out.println("游戏已开始！使用方向键控制蛇的移动。");
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

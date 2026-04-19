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

                // 创建键盘控制器
                KeyController keyController = new KeyController(gameController);

                // 为窗口设置键盘控制器
                gameFrame.setKeyController(keyController);

                // 添加窗口事件监听器
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
                System.out.println("贪吃蛇创新版启动完成！");
                System.out.println("\n===== 游戏特色功能 =====");
                System.out.println("1. 多种皮肤选择（经典、霓虹、火焰、彩虹）");
                System.out.println("2. 多种食物类型：");
                System.out.println("   - 普通食物（红色）：+10分");
                System.out.println("   - 金色食物（金色）：+50分");
                System.out.println("   - 加速食物（蓝色）：加速移动");
                System.out.println("   - 减速食物（橙色）：减速移动");
                System.out.println("   - 炸弹（黑色）：-20分，缩短蛇身");
                System.out.println("3. 连击系统：连续吃食物获得倍数加分");
                System.out.println("4. 设置面板：可调整各种游戏参数");
                System.out.println("\n===== 控制说明 =====");
                System.out.println("  ↑/W: 向上移动");
                System.out.println("  ↓/S: 向下移动");
                System.out.println("  ←/A: 向左移动");
                System.out.println("  →/D: 向右移动");
                System.out.println("  空格: 暂停/继续游戏");
                System.out.println("  Enter: 开始游戏");
                System.out.println("  R: 重新开始游戏");
                System.out.println("  ESC: 退出游戏");
                System.out.println("  F1: 打开设置");

                // 延迟2秒后自动开始游戏
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        SwingUtilities.invokeLater(() -> {
                            gameController.startGame();
                            System.out.println("游戏已开始！尽情享受吧！");
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

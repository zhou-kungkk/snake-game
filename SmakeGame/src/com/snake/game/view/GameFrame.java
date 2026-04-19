package com.snake.game.view;

import com.snake.game.util.Constants;
import com.snake.game.controller.KeyController;
import com.snake.game.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private ScorePanel scorePanel;
    private KeyController keyController;

    public GameFrame() {
        initFrame();
    }

    private void initFrame() {
        // 设置窗口标题
        setTitle(Constants.WINDOW_TITLE);

        // 设置窗口关闭行为
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 禁止调整窗口大小
        setResizable(false);

        // 设置布局
        setLayout(new BorderLayout());

        // 创建游戏面板
        gamePanel = new GamePanel();

        // 创建分数面板
        scorePanel = new ScorePanel();
        scorePanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, 50));

        // 添加面板到窗口
        add(gamePanel, BorderLayout.CENTER);
        add(scorePanel, BorderLayout.SOUTH);

        // 调整窗口大小
        pack();

        // 窗口居中显示
        setLocationRelativeTo(null);

        // 确保窗口可以获得焦点
        setFocusable(true);

        System.out.println("游戏窗口创建完成！");
    }

    /**
     * 启动游戏界面
     */
    public void start() {
        setVisible(true);

        // 请求焦点
        requestFocusInWindow();
        System.out.println("游戏窗口已显示！焦点状态: " + isFocusable());
    }

    /**
     * 获取游戏面板
     */
    public GamePanel getGamePanel() {
        return gamePanel;
    }

    /**
     * 获取分数面板
     */
    public ScorePanel getScorePanel() {
        return scorePanel;
    }

    /**
     * 设置键盘控制器
     */
    public void setKeyController(KeyController keyController) {
        this.keyController = keyController;

        // 为整个窗口添加键盘监听器
        addKeyListener(keyController);

        // 为游戏面板也添加键盘监听器
        gamePanel.addKeyListener(keyController);
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();

        // 确保焦点在正确的组件上
        setFocusTraversalKeysEnabled(false);
    }

    /**
     * 重新获取焦点
     */
    public void refocus() {
        gamePanel.requestFocusInWindow();
        requestFocusInWindow();
    }
}

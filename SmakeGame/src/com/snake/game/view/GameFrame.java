package com.snake.game.view;

import com.snake.game.util.Constants;
import com.snake.game.controller.KeyController;
import com.snake.game.controller.GameController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private ScorePanel scorePanel;
    private KeyController keyController;
    private JButton settingsButton; // 新增：设置按钮

    public GameFrame() {
        initFrame();
    }

    private void initFrame() {
        // 设置窗口标题
        setTitle(Constants.WINDOW_TITLE + " - 创新版");

        // 设置窗口关闭行为
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 禁止调整窗口大小
        setResizable(false);

        // 设置布局
        setLayout(new BorderLayout());

        // 创建控制面板（顶部）
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        settingsButton = new JButton("设置");
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSettingsDialog();
            }
        });
        controlPanel.add(settingsButton);

        // 创建游戏面板
        gamePanel = new GamePanel();

        // 创建分数面板
        scorePanel = new ScorePanel();
        scorePanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, 50));

        // 添加面板到窗口
        add(controlPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        add(scorePanel, BorderLayout.SOUTH);

        // 调整窗口大小
        pack();

        // 窗口居中显示
        setLocationRelativeTo(null);

        // 确保窗口可以获得焦点
        setFocusable(true);

        System.out.println("游戏窗口创建完成！已集成皮肤设置功能。");
    }

    /**
     * 打开设置对话框
     */
    private void openSettingsDialog() {
        // 创建一个模态对话框
        JDialog settingsDialog = new JDialog(this, "游戏设置", true);
        settingsDialog.setLayout(new BorderLayout());

        // 将设置面板添加到对话框中
        SettingsPanel settingsPanel = new SettingsPanel(gamePanel, scorePanel);
        settingsDialog.add(settingsPanel, BorderLayout.CENTER);

        // 添加关闭按钮
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsDialog.dispose();
                // 关闭对话框后将焦点还给游戏面板
                gamePanel.requestFocus();
            }
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        settingsDialog.add(bottomPanel, BorderLayout.SOUTH);

        // 设置对话框属性并显示
        settingsDialog.pack();
        settingsDialog.setLocationRelativeTo(this); // 居中显示
        settingsDialog.setVisible(true);
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
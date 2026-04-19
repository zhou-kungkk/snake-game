package com.snake.game.view;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private JLabel scoreLabel;
    private JLabel lengthLabel;
    private JLabel statusLabel;
    private int score;
    private int length;

    public ScorePanel() {
        initPanel();
    }

    private void initPanel() {
        setLayout(new GridLayout(1, 3)); // 一行三列

        // 创建标签
        scoreLabel = new JLabel("得分: 0", SwingConstants.CENTER);
        lengthLabel = new JLabel("长度: 3", SwingConstants.CENTER);
        statusLabel = new JLabel("状态: 准备开始", SwingConstants.CENTER);

        // 设置字体
        Font labelFont = new Font("微软雅黑", Font.BOLD, 18);
        scoreLabel.setFont(labelFont);
        lengthLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);

        // 设置文字颜色
        scoreLabel.setForeground(Color.WHITE);
        lengthLabel.setForeground(Color.WHITE);
        statusLabel.setForeground(Color.WHITE);

        // 设置背景色
        scoreLabel.setBackground(new Color(60, 60, 60));
        lengthLabel.setBackground(new Color(60, 60, 60));
        statusLabel.setBackground(new Color(60, 60, 60));
        scoreLabel.setOpaque(true);
        lengthLabel.setOpaque(true);
        statusLabel.setOpaque(true);

        // 添加标签到面板
        add(scoreLabel);
        add(lengthLabel);
        add(statusLabel);

        // 设置面板背景色
        setBackground(Color.DARK_GRAY);

        System.out.println("分数面板创建完成！");
    }

    /**
     * 更新得分
     */
    public void updateScore(int score) {
        this.score = score;
        scoreLabel.setText("得分: " + score);
    }

    /**
     * 更新长度
     */
    public void updateLength(int length) {
        this.length = length;
        lengthLabel.setText("长度: " + length);
    }

    /**
     * 更新状态
     */
    public void updateStatus(String status) {
        statusLabel.setText("状态: " + status);
    }

    /**
     * 获取当前得分
     */
    public int getScore() {
        return score;
    }
}

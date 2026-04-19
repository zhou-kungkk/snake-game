package com.snake.game.view;

import com.snake.game.model.GameConfig;
import com.snake.game.model.SnakeSkin;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {
    private GamePanel gamePanel;
    private ScorePanel scorePanel;

    // 皮肤选择组件
    private JComboBox<String> skinComboBox;
    private JCheckBox gridCheckBox;
    private JCheckBox specialFoodCheckBox;
    private JSlider speedSlider;
    private JCheckBox soundCheckBox;
    private JCheckBox effectsCheckBox;

    public SettingsPanel(GamePanel gamePanel, ScorePanel scorePanel) {
        this.gamePanel = gamePanel;
        this.scorePanel = scorePanel;
        initPanel();
    }

    private void initPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 400));

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. 皮肤选择
        JPanel skinPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        skinPanel.add(new JLabel("皮肤选择:"));
        skinComboBox = new JComboBox<>();
        for (SnakeSkin skin : SnakeSkin.values()) {
            skinComboBox.addItem(skin.getName());
        }
        // 设置默认选中项
        skinComboBox.setSelectedItem(GameConfig.getInstance().getCurrentSkin().getName());
        skinComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSkin();
            }
        });
        skinPanel.add(skinComboBox);

        // 2. 网格显示
        gridCheckBox = new JCheckBox("显示网格", GameConfig.getInstance().isShowGrid());
        gridCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleGrid();
            }
        });

        // 3. 特殊食物
        specialFoodCheckBox = new JCheckBox("启用特殊食物", GameConfig.getInstance().isEnableSpecialFood());
        specialFoodCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleSpecialFood();
            }
        });

        // 4. 速度控制
        JPanel speedPanel = new JPanel(new BorderLayout());
        speedPanel.add(new JLabel("游戏速度:"), BorderLayout.WEST);
        // 使用固定值
        speedSlider = new JSlider(JSlider.HORIZONTAL, 50, 300, GameConfig.getInstance().getInitialSpeed());
        speedSlider.setMajorTickSpacing(50);
        speedSlider.setMinorTickSpacing(10);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateSpeed();
            }
        });
        speedPanel.add(speedSlider, BorderLayout.CENTER);

        // 5. 声音效果
        soundCheckBox = new JCheckBox("启用声音", GameConfig.getInstance().isSoundEnabled());
        soundCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleSound();
            }
        });

        // 6. 视觉效果
        effectsCheckBox = new JCheckBox("启用特效", GameConfig.getInstance().isShowEffects());
        effectsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleEffects();
            }
        });

        // 7. 重置按钮
        JButton resetButton = new JButton("重置为默认设置");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSettings();
            }
        });

        // 添加到主面板
        mainPanel.add(skinPanel);
        mainPanel.add(gridCheckBox);
        mainPanel.add(specialFoodCheckBox);
        mainPanel.add(speedPanel);
        mainPanel.add(soundCheckBox);
        mainPanel.add(effectsCheckBox);
        mainPanel.add(resetButton);

        add(mainPanel, BorderLayout.CENTER);

        // 添加标题
        JLabel titleLabel = new JLabel("游戏设置", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
    }

    /**
     * 切换皮肤
     */
    private void changeSkin() {
        int index = skinComboBox.getSelectedIndex();
        SnakeSkin[] skins = SnakeSkin.values();
        if (index >= 0 && index < skins.length) {
            GameConfig.getInstance().setCurrentSkin(skins[index]);
            gamePanel.updateSkin();
            scorePanel.updateStatus("皮肤已切换: " + skins[index].getName());
        }
    }

    /**
     * 切换网格显示
     */
    private void toggleGrid() {
        GameConfig.getInstance().setShowGrid(gridCheckBox.isSelected());
        gamePanel.repaint();
    }

    /**
     * 切换特殊食物
     */
    private void toggleSpecialFood() {
        GameConfig.getInstance().setEnableSpecialFood(specialFoodCheckBox.isSelected());
    }

    /**
     * 更新速度
     */
    private void updateSpeed() {
        int speed = speedSlider.getValue();
        GameConfig.getInstance().setInitialSpeed(speed);
        scorePanel.updateStatus("速度设置将在下次游戏开始时生效: " + speed + "ms");
    }

    /**
     * 切换声音
     */
    private void toggleSound() {
        GameConfig.getInstance().setSoundEnabled(soundCheckBox.isSelected());
    }

    /**
     * 切换特效
     */
    private void toggleEffects() {
        GameConfig.getInstance().setShowEffects(effectsCheckBox.isSelected());
        gamePanel.repaint();
    }

    /**
     * 重置设置
     */
    private void resetSettings() {
        GameConfig.getInstance().resetToDefault();

        // 更新UI组件
        skinComboBox.setSelectedIndex(0);
        gridCheckBox.setSelected(GameConfig.getInstance().isShowGrid());
        specialFoodCheckBox.setSelected(GameConfig.getInstance().isEnableSpecialFood());
        speedSlider.setValue(GameConfig.getInstance().getInitialSpeed());
        soundCheckBox.setSelected(GameConfig.getInstance().isSoundEnabled());
        effectsCheckBox.setSelected(GameConfig.getInstance().isShowEffects());

        // 更新游戏面板
        gamePanel.updateSkin();
        gamePanel.repaint();

        scorePanel.updateStatus("设置已重置为默认");
    }
}
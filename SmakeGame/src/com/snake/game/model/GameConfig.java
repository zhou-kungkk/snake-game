package com.snake.game.model;

public class GameConfig {
    private SnakeSkin currentSkin = SnakeSkin.CLASSIC;
    private boolean showGrid = true;
    private boolean enableSpecialFood = true;
    private int initialSpeed = 100;  // 修改：从150改为100，提高默认帧率
    private boolean soundEnabled = true;
    private boolean showEffects = true;

    // 单例模式
    private static GameConfig instance = new GameConfig();

    private GameConfig() {}

    public static GameConfig getInstance() {
        return instance;
    }

    // Getter 和 Setter
    public SnakeSkin getCurrentSkin() { return currentSkin; }
    public void setCurrentSkin(SnakeSkin skin) { this.currentSkin = skin; }

    public boolean isShowGrid() { return showGrid; }
    public void setShowGrid(boolean showGrid) { this.showGrid = showGrid; }

    public boolean isEnableSpecialFood() { return enableSpecialFood; }
    public void setEnableSpecialFood(boolean enable) { this.enableSpecialFood = enable; }

    public int getInitialSpeed() { return initialSpeed; }
    public void setInitialSpeed(int speed) { this.initialSpeed = speed; }

    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean enabled) { this.soundEnabled = enabled; }

    public boolean isShowEffects() { return showEffects; }
    public void setShowEffects(boolean show) { this.showEffects = show; }

    /**
     * 重置为默认配置
     */
    public void resetToDefault() {
        currentSkin = SnakeSkin.CLASSIC;
        showGrid = true;
        enableSpecialFood = true;
        initialSpeed = 100;  // 修改：从150改为100
        soundEnabled = true;
        showEffects = true;
    }
}
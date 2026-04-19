package com.snake.game.controller;

import com.snake.game.model.*;
import com.snake.game.view.*;
import com.snake.game.util.Constants;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    private Snake snake;
    private Food food;
    private GamePanel gamePanel;
    private ScorePanel scorePanel;
    private Timer timer;
    private GameStatus gameStatus;
    private int score;

    public GameController(GamePanel gamePanel, ScorePanel scorePanel) {
        this.gamePanel = gamePanel;
        this.scorePanel = scorePanel;
        this.snake = new Snake();
        this.food = new Food();
        this.gameStatus = GameStatus.PAUSED;
        this.score = 0;

        // 生成不在蛇身上的食物
        food.generateNewPosition(snake);

        // 设置面板的蛇和食物
        gamePanel.setSnake(snake);
        gamePanel.setFood(food);

        // 创建游戏循环定时器
        timer = new Timer(Constants.GAME_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameLoop();
            }
        });

        updateUI();
    }

    /**
     * 游戏主循环
     */
    private void gameLoop() {
        if (gameStatus != GameStatus.RUNNING) {
            return;
        }

        // 移动蛇
        snake.move();

        // 检查是否吃到食物
        if (snake.checkEatFood(food.getPosition())) {
            snake.grow();
            food.generateNewPosition(snake);
            score += 10;
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
        gameStatus = GameStatus.RUNNING;
        scorePanel.updateStatus("游戏中");
        timer.start();
    }

    /**
     * 暂停游戏
     */
    public void pauseGame() {
        if (gameStatus == GameStatus.RUNNING) {
            gameStatus = GameStatus.PAUSED;
            scorePanel.updateStatus("已暂停");
            timer.stop();
        } else if (gameStatus == GameStatus.PAUSED) {
            startGame();
        }
    }

    /**
     * 重新开始游戏
     */
    public void restartGame() {
        timer.stop();
        snake.reset();
        food.generateNewPosition();
        score = 0;
        gameStatus = GameStatus.PAUSED;
        scorePanel.updateStatus("准备开始");
        updateUI();
    }

    /**
     * 游戏结束
     */
    private void gameOver() {
        gameStatus = GameStatus.GAME_OVER;
        timer.stop();
        scorePanel.updateStatus("游戏结束");
    }

    /**
     * 更新UI显示
     */
    private void updateUI() {
        // 更新分数和长度显示
        scorePanel.updateScore(score);
        scorePanel.updateLength(snake.getBody().size());

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
}

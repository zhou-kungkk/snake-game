package com.snake.game.controller;

import com.snake.game.model.Direction;
import com.snake.game.model.GameStatus;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public class KeyController extends KeyAdapter {
    private GameController gameController;

    public KeyController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("按键按下: " + KeyEvent.getKeyText(keyCode)); // 调试信息

        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                gameController.changeDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                gameController.changeDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                gameController.changeDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                gameController.changeDirection(Direction.RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                // 空格键暂停/继续游戏
                if (gameController.getGameStatus() == GameStatus.RUNNING) {
                    gameController.pauseGame();
                    JOptionPane.showMessageDialog(null, "游戏已暂停\n按空格键继续", "游戏暂停", JOptionPane.INFORMATION_MESSAGE);
                } else if (gameController.getGameStatus() == GameStatus.PAUSED) {
                    gameController.startGame();
                }
                break;
            case KeyEvent.VK_R:
                // R键重新开始游戏
                int result = JOptionPane.showConfirmDialog(null, "确定要重新开始游戏吗？", "重新开始", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    gameController.restartGame();
                }
                break;
            case KeyEvent.VK_ENTER:
                // Enter键开始游戏
                if (gameController.getGameStatus() == GameStatus.PAUSED) {
                    gameController.startGame();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                // ESC键退出游戏
                result = JOptionPane.showConfirmDialog(null, "确定要退出游戏吗？", "退出游戏", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
        }
    }
}

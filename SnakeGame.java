import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

public class SnakeGame extends JFrame {
    // ==================== 游戏常量（可自定义）====================
    private static final int BLOCK_SIZE = 25;
    private static final int MAP_WIDTH = 30;
    private static final int MAP_HEIGHT = 20;
    private static final int INIT_SPEED_EASY = 180;
    private static final int INIT_SPEED_NORMAL = 120;
    private static final int INIT_SPEED_HARD = 70;
    private static final int SPEED_DECREASE = 5;
    private static final int MIN_SPEED = 20;

    // ==================== 游戏变量 ====================
    private LinkedList<Point> snake;
    private Point food;
    private String direction;
    private boolean isRunning;
    private boolean isPaused;
    private int score;
    private int highScore;
    private int currentSpeed;
    private int difficulty; // 0:简单 1:普通 2:困难
    private Clip eatSound, gameOverSound;
    private boolean soundEnabled = true;

    // ==================== UI组件 ====================
    private GamePanel gamePanel;
    private JLabel scoreLabel, highScoreLabel, difficultyLabel;
    private JButton pauseBtn, restartBtn, soundBtn;

    public SnakeGame() {
        // 窗口初始化
        setTitle("豪华版贪吃蛇");
        setSize(MAP_WIDTH * BLOCK_SIZE + 20, MAP_HEIGHT * BLOCK_SIZE + 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // 加载最高分
        loadHighScore();

        // 顶部控制面板
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        controlPanel.setBackground(new Color(40, 44, 52));

        scoreLabel = new JLabel("得分: 0");
        scoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        controlPanel.add(scoreLabel);

        highScoreLabel = new JLabel("最高分: " + highScore);
        highScoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        highScoreLabel.setForeground(Color.WHITE);
        controlPanel.add(highScoreLabel);

        difficultyLabel = new JLabel("难度: 普通");
        difficultyLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        difficultyLabel.setForeground(Color.WHITE);
        controlPanel.add(difficultyLabel);

        pauseBtn = new JButton("暂停");
        pauseBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        pauseBtn.addActionListener(e -> togglePause());
        controlPanel.add(pauseBtn);

        restartBtn = new JButton("重新开始");
        restartBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        restartBtn.addActionListener(e -> initGame());
        controlPanel.add(restartBtn);

        soundBtn = new JButton("🔊 音效开");
        soundBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        soundBtn.addActionListener(e -> toggleSound());
        controlPanel.add(soundBtn);

        add(controlPanel, BorderLayout.NORTH);

        // 游戏面板
        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        // 键盘监听
        addKeyListener(new SnakeKeyListener());
        setFocusable(true);

        // 加载音效
        loadSounds();

        // 选择难度
        selectDifficulty();

        // 初始化游戏
        initGame();

        // 启动游戏线程
        startGameThread();
    }

    // ==================== 难度选择 ====================
    private void selectDifficulty() {
        String[] options = {"简单", "普通", "困难"};
        int choice = JOptionPane.showOptionDialog(this,
                "请选择游戏难度",
                "难度选择",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (choice == -1) System.exit(0);
        difficulty = choice;
        switch (difficulty) {
            case 0:
                currentSpeed = INIT_SPEED_EASY;
                difficultyLabel.setText("难度: 简单");
                break;
            case 1:
                currentSpeed = INIT_SPEED_NORMAL;
                difficultyLabel.setText("难度: 普通");
                break;
            case 2:
                currentSpeed = INIT_SPEED_HARD;
                difficultyLabel.setText("难度: 困难");
                break;
        }
    }

    // ==================== 游戏初始化 ====================
    private void initGame() {
        snake = new LinkedList<>();
        snake.add(new Point(MAP_WIDTH / 2, MAP_HEIGHT / 2));
        snake.add(new Point(MAP_WIDTH / 2 - 1, MAP_HEIGHT / 2));
        snake.add(new Point(MAP_WIDTH / 2 - 2, MAP_HEIGHT / 2));
        direction = "RIGHT";
        isRunning = true;
        isPaused = false;
        score = 0;
        pauseBtn.setText("暂停");
        updateScore();
        generateFood();
        gamePanel.repaint();
    }

    // ==================== 生成食物 ====================
    private void generateFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(MAP_WIDTH);
            y = random.nextInt(MAP_HEIGHT);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    // ==================== 蛇移动逻辑 ====================
    private void moveSnake() {
        if (!isRunning || isPaused) return;

        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case "UP": newHead.y--; break;
            case "DOWN": newHead.y++; break;
            case "LEFT": newHead.x--; break;
            case "RIGHT": newHead.x++; break;
        }

        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            score += 10;
            if (soundEnabled) playSound(eatSound);
            updateScore();
            generateFood();
            if (currentSpeed > MIN_SPEED) {
                currentSpeed -= SPEED_DECREASE;
            }
        } else {
            snake.removeLast();
        }
    }

    // ==================== 碰撞检测 ====================
    private void checkCollision() {
        if (!isRunning) return;

        Point head = snake.getFirst();
        // 撞墙
        if (head.x < 0 || head.x >= MAP_WIDTH || head.y < 0 || head.y >= MAP_HEIGHT) {
            gameOver();
            return;
        }
        // 撞自身
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                return;
            }
        }
    }

    // ==================== 游戏结束 ====================
    private void gameOver() {
        isRunning = false;
        if (soundEnabled) playSound(gameOverSound);
        if (score > highScore) {
            highScore = score;
            saveHighScore();
            highScoreLabel.setText("最高分: " + highScore);
        }

        int choice = JOptionPane.showOptionDialog(this,
                "游戏结束！\n最终得分: " + score + "\n是否重新开始？",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"重新开始", "退出"},
                "重新开始");

        if (choice == 0) {
            selectDifficulty();
            initGame();
        } else {
            System.exit(0);
        }
    }

    // ==================== 游戏线程 ====================
    private void startGameThread() {
        new Thread(() -> {
            while (true) {
                if (isRunning && !isPaused) {
                    moveSnake();
                    checkCollision();
                    gamePanel.repaint();
                }
                try {
                    Thread.sleep(currentSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // ==================== 键盘监听 ====================
    private class SnakeKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) {
                direction = "UP";
            } else if (key == KeyEvent.VK_DOWN && !direction.equals("UP")) {
                direction = "DOWN";
            } else if (key == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) {
                direction = "LEFT";
            } else if (key == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) {
                direction = "RIGHT";
            } else if (key == KeyEvent.VK_SPACE) {
                togglePause();
            } else if (key == KeyEvent.VK_R) {
                initGame();
            }
        }

        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

    // ==================== 游戏面板（渲染）====================
    private class GamePanel extends JPanel {
        public GamePanel() {
            setBackground(new Color(30, 34, 40));
            setDoubleBuffered(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制网格背景
            drawGrid(g2d);

            // 绘制蛇
            drawSnake(g2d);

            // 绘制食物
            drawFood(g2d);

            // 暂停提示
            if (isPaused) {
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 40));
                String pauseText = "游戏暂停";
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(pauseText)) / 2;
                int y = getHeight() / 2;
                g2d.drawString(pauseText, x, y);
            }
        }

        // 绘制网格
        private void drawGrid(Graphics2D g2d) {
            g2d.setColor(new Color(50, 54, 60));
            for (int i = 0; i <= MAP_WIDTH; i++) {
                g2d.drawLine(i * BLOCK_SIZE, 0, i * BLOCK_SIZE, MAP_HEIGHT * BLOCK_SIZE);
            }
            for (int i = 0; i <= MAP_HEIGHT; i++) {
                g2d.drawLine(0, i * BLOCK_SIZE, MAP_WIDTH * BLOCK_SIZE, i * BLOCK_SIZE);
            }
        }

        // 绘制渐变蛇身
        private void drawSnake(Graphics2D g2d) {
            for (int i = 0; i < snake.size(); i++) {
                Point p = snake.get(i);
                int x = p.x * BLOCK_SIZE + 2;
                int y = p.y * BLOCK_SIZE + 2;
                int size = BLOCK_SIZE - 4;

                // 蛇头渐变
                if (i == 0) {
                    GradientPaint headGradient = new GradientPaint(x, y, new Color(255, 80, 80),
                            x + size, y + size, new Color(200, 0, 0));
                    g2d.setPaint(headGradient);
                } else {
                    // 蛇身渐变（越往后越浅）
                    float ratio = (float) (snake.size() - i) / snake.size();
                    int r = (int) (80 + 100 * ratio);
                    int g = (int) (200 + 55 * ratio);
                    int b = (int) (80 + 55 * ratio);
                    g2d.setColor(new Color(r, g, b));
                }

                g2d.fillRoundRect(x, y, size, size, 8, 8);
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(x, y, size, size, 8, 8);
            }
        }

        // 绘制圆角食物
        private void drawFood(Graphics2D g2d) {
            int x = food.x * BLOCK_SIZE + 2;
            int y = food.y * BLOCK_SIZE + 2;
            int size = BLOCK_SIZE - 4;

            GradientPaint foodGradient = new GradientPaint(x, y, new Color(255, 215, 0),
                    x + size, y + size, new Color(255, 140, 0));
            g2d.setPaint(foodGradient);
            g2d.fillOval(x, y, size, size);
            g2d.setColor(new Color(255, 255, 255, 80));
            g2d.drawOval(x, y, size, size);
        }
    }

    // ==================== 辅助方法 ====================
    private void updateScore() {
        scoreLabel.setText("得分: " + score);
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseBtn.setText(isPaused ? "继续" : "暂停");
        gamePanel.repaint();
    }

    private void toggleSound() {
        soundEnabled = !soundEnabled;
        soundBtn.setText(soundEnabled ? "🔊 音效开" : "🔇 音效关");
    }

    // 加载最高分
    private void loadHighScore() {
        try {
            File file = new File("snake_highscore.txt");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                highScore = scanner.nextInt();
                scanner.close();
            } else {
                highScore = 0;
            }
        } catch (Exception e) {
            highScore = 0;
        }
    }

    // 保存最高分
    private void saveHighScore() {
        try {
            FileWriter writer = new FileWriter("snake_highscore.txt");
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加载音效
    private void loadSounds() {
        try {
            // 吃食物音效（可替换为自己的wav文件）
            eatSound = AudioSystem.getClip();
            // 游戏结束音效
            gameOverSound = AudioSystem.getClip();
        } catch (Exception e) {
            soundEnabled = false;
            soundBtn.setText("🔇 音效加载失败");
        }
    }

    // 播放音效
    private void playSound(Clip clip) {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    // ==================== 主方法 ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame().setVisible(true);
        });
    }
}
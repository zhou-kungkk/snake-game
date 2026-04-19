package com.snake.game.test;

import com.snake.game.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * 模型层自动化测试套件
 * 集中测试所有模型类的功能
 */
public class ModelTestSuite {

    // === Point 类测试 ===
    @Test
    void testPointCreationAndEquality() {
        // 测试Point类的构造函数、getter和equals方法
        Point p1 = new Point(5, 10);
        Point p2 = new Point(5, 10);
        Point p3 = new Point(3, 4);

        assertEquals(5, p1.getX());
        assertEquals(10, p1.getY());
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
        assertFalse(p1.equals(null));
        assertFalse(p1.equals("字符串"));
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testPointSetters() {
        // 测试Point类的setter方法
        Point point = new Point(0, 0);
        point.setX(7);
        point.setY(12);
        assertEquals(7, point.getX());
        assertEquals(12, point.getY());
    }

    // === Snake 类测试 ===
    @Test
    void testSnakeInitialization() {
        // 测试Snake类的初始化状态
        Snake snake = new Snake();
        List<Point> body = snake.getBody();

        assertEquals(3, body.size(), "蛇的初始长度应为3");
        assertEquals(Direction.RIGHT, snake.getDirection(), "初始方向应为RIGHT");
        assertEquals(new Point(10, 15), body.get(0), "蛇头初始位置应为(10,15)");
    }

    @Test
    void testSnakeMovement() {
        // 测试蛇的移动功能
        Snake snake = new Snake();
        Point initialHead = snake.getHead();

        snake.move();
        Point newHead = snake.getHead();

        // 验证向右移动后，X坐标+1，Y坐标不变
        assertEquals(initialHead.getX() + 1, newHead.getX());
        assertEquals(initialHead.getY(), newHead.getY());
        assertEquals(3, snake.getBody().size(), "移动后长度应保持不变");
    }

    @Test
    void testSnakeDirectionChange() {
        // 测试蛇的方向改变
        Snake snake = new Snake();

        snake.changeDirection(Direction.DOWN);
        assertEquals(Direction.DOWN, snake.getDirection());

        snake.move();
        Point head = snake.getHead();
        assertEquals(10, head.getX());
        assertEquals(16, head.getY());
    }

    @Test
    void testSnakePreventReverseDirection() {
        // 测试不能直接反向移动（例如从RIGHT到LEFT）
        Snake snake = new Snake();
        assertEquals(Direction.RIGHT, snake.getDirection());

        snake.changeDirection(Direction.LEFT); // 试图反向
        snake.move(); // 应该继续向右移动

        // 方向应该还是RIGHT，且位置向右移动
        assertEquals(Direction.RIGHT, snake.getDirection());
        assertEquals(11, snake.getHead().getX());
    }

    @Test
    void testSnakeGrowth() {
        // 测试蛇的增长功能
        Snake snake = new Snake();
        int initialSize = snake.getBody().size();

        snake.grow();
        snake.move(); // 移动时应该保留增长后的长度

        assertEquals(initialSize + 1, snake.getBody().size());

        // 再次移动，长度应该保持不变
        int sizeAfterGrowth = snake.getBody().size();
        snake.move();
        assertEquals(sizeAfterGrowth, snake.getBody().size());
    }

    @Test
    void testSnakeEatFoodDetection() {
        // 测试吃食物的检测逻辑
        Snake snake = new Snake();
        Point head = snake.getHead();

        // 食物在蛇头位置 - 应该检测到吃到食物
        Point foodAtHead = new Point(head.getX(), head.getY());
        assertTrue(snake.checkEatFood(foodAtHead));

        // 食物不在蛇头位置 - 应该检测到没吃到
        Point foodAway = new Point(20, 20);
        assertFalse(snake.checkEatFood(foodAway));
    }

    @Test
    void testSnakeSelfCollision() {
        // 测试自碰撞检测（初始状态不应该有自碰撞）
        Snake snake = new Snake();
        assertFalse(snake.checkSelfCollision());
    }

    @Test
    void testSnakeWallCollision() {
        // 测试墙碰撞检测（初始位置不应该撞墙）
        Snake snake = new Snake();
        assertFalse(snake.checkWallCollision());
    }

    @Test
    void testSnakeReset() {
        // 测试蛇的重置功能
        Snake snake = new Snake();

        // 先移动和改变方向
        snake.move();
        snake.changeDirection(Direction.DOWN);
        snake.move();

        // 重置蛇
        snake.reset();

        // 验证重置后的状态
        assertEquals(3, snake.getBody().size());
        assertEquals(Direction.RIGHT, snake.getDirection());
        assertEquals(new Point(10, 15), snake.getHead());
    }

    // === Food 类测试 ===
    @Test
    void testFoodGeneration() {
        // 测试食物的生成
        Food food = new Food();

        // 验证食物位置在网格范围内
        assertTrue(food.getX() >= 0);
        assertTrue(food.getX() < 40); // Constants.GRID_WIDTH
        assertTrue(food.getY() >= 0);
        assertTrue(food.getY() < 30); // Constants.GRID_HEIGHT

        // 验证食物类型不为null
        assertNotNull(food.getType());
    }

    @Test
    void testFoodTypeValues() {
        // 测试不同食物类型的属性
        assertEquals("普通", FoodType.NORMAL.getName());
        assertEquals(10, FoodType.NORMAL.getScore());

        assertEquals("金色", FoodType.GOLD.getName());
        assertEquals(50, FoodType.GOLD.getScore());

        assertEquals("炸弹", FoodType.BOMB.getName());
        assertEquals(-20, FoodType.BOMB.getScore());
    }

    @Test
    void testFoodGenerateNotOnSnake() {
        // 测试生成不在蛇身上的食物
        Snake snake = new Snake();
        Food food = new Food();

        // 生成不在蛇身上的食物
        food.generateRandomFood(snake);

        // 验证食物不在蛇身上的任何位置
        boolean onSnake = false;
        for (Point point : snake.getBody()) {
            if (point.getX() == food.getX() && point.getY() == food.getY()) {
                onSnake = true;
                break;
            }
        }

        assertFalse(onSnake, "食物不应该生成在蛇身上");
    }

    // === Direction 枚举测试 ===
    @Test
    void testDirectionEnum() {
        // 测试Direction枚举的所有值
        assertEquals(4, Direction.values().length);
        assertEquals(Direction.UP, Direction.valueOf("UP"));
        assertEquals(Direction.DOWN, Direction.valueOf("DOWN"));
        assertEquals(Direction.LEFT, Direction.valueOf("LEFT"));
        assertEquals(Direction.RIGHT, Direction.valueOf("RIGHT"));
    }

    // === 综合测试 ===
    @Test
    void testSnakeEatDifferentFoodTypes() {
        // 综合测试：蛇吃到不同类型的食物
        Snake snake = new Snake();
        Food food = new Food();

        // 将食物放在蛇头位置
        Point head = snake.getHead();
        // 注意：这里需要直接设置食物的位置，实际游戏中由GameController处理
        // 这是一个简化的测试，主要验证逻辑概念

        // 模拟吃到普通食物
        int initialSize = snake.getBody().size();
        snake.grow(); // 模拟吃到食物后的增长
        snake.move();
        assertEquals(initialSize + 1, snake.getBody().size());
    }
}
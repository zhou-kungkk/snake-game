package com.snake.game.model;

import java.util.LinkedList;
import java.util.List;

public class Snake {
    private List<Point> body;  // 蛇的身体（用链表存储坐标）
    private Direction direction;  // 蛇的移动方向
    private boolean growing;  // 标记蛇是否需要增长

    public Snake() {
        init();
    }

    /**
     * 初始化蛇
     */
    private void init() {
        body = new LinkedList<>();
        // 初始蛇有3节，水平放置
        int startX = 10;
        int startY = 15;
        for (int i = 0; i < 3; i++) {
            body.add(new Point(startX - i, startY));
        }
        direction = Direction.RIGHT;
        growing = false;
    }

    /**
     * 移动蛇
     */
    public void move() {
        // 获取当前头部位置
        Point head = getHead();
        int newX = head.getX();
        int newY = head.getY();

        // 根据方向计算新的头部位置
        switch (direction) {
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
        }

        // 添加新的头部
        Point newHead = new Point(newX, newY);
        body.add(0, newHead);

        // 如果不增长，则移除尾部
        if (!growing) {
            body.remove(body.size() - 1);
        } else {
            growing = false;  // 增长后重置标记
        }
    }

    /**
     * 改变蛇的方向
     */
    public void changeDirection(Direction newDirection) {
        // 防止直接反向移动（比如不能从右直接转向左）
        if ((direction == Direction.UP && newDirection == Direction.DOWN) ||
                (direction == Direction.DOWN && newDirection == Direction.UP) ||
                (direction == Direction.LEFT && newDirection == Direction.RIGHT) ||
                (direction == Direction.RIGHT && newDirection == Direction.LEFT)) {
            return;
        }
        direction = newDirection;
    }

    /**
     * 检查是否吃到食物
     */
    public boolean checkEatFood(Point foodPosition) {
        Point head = getHead();
        return head.getX() == foodPosition.getX() &&
                head.getY() == foodPosition.getY();
    }

    /**
     * 检查是否撞到自己
     */
    public boolean checkSelfCollision() {
        Point head = getHead();
        // 从第二节开始检查（第一节是头）
        for (int i = 1; i < body.size(); i++) {
            if (head.getX() == body.get(i).getX() &&
                    head.getY() == body.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否撞墙
     */
    public boolean checkWallCollision() {
        Point head = getHead();
        int x = head.getX();
        int y = head.getY();

        return x < 0 || x >= com.snake.game.util.Constants.GRID_WIDTH ||
                y < 0 || y >= com.snake.game.util.Constants.GRID_HEIGHT;
    }

    /**
     * 让蛇增长一节
     */
    public void grow() {
        growing = true;
    }

    /**
     * 获取蛇头位置
     */
    public Point getHead() {
        return body.get(0);
    }

    /**
     * 获取蛇身体的所有部分
     */
    public List<Point> getBody() {
        return body;
    }

    /**
     * 获取当前方向
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * 重置蛇
     */
    public void reset() {
        init();
    }
}

package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An implementation of {@code BrickGenerator} that provides bricks in a random sequence.
 * It pre-generates a queue of upcoming bricks to ensure a smooth gameplay experience.
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Constructs a new RandomBrickGenerator.
     * Initializes the list of available brick types and pre-fills the queue
     * with two random bricks.
     */
    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    }

    /**
     * Retrieves the next brick from the queue. If the queue is running low,
     * it generates and adds a new random brick to maintain a buffer.
     *
     * @return The next {@code Brick} to be used in the game.
     */
    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }
        return nextBricks.poll();
    }

    /**
     * Peeks at the next brick in the queue without removing it.
     * Useful for displaying the "next piece" preview to the player.
     *
     * @return The {@code Brick} that will be available next.
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}

package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBrickGenerator();
    }

    @Test
    void constructor_initializes_two_next_bricks() {
        Brick nextBrick = generator.getNextBrick();
        Brick secondBrick = generator.getBrick();
        Brick thirdBrick = generator.getNextBrick();

        assertNotNull(nextBrick);
        assertNotNull(secondBrick);
        assertNotNull(thirdBrick);

        // After one call to getBrick(), the queue should be replenished,
        // so calling getBrick() two more times should succeed.
        assertNotNull(generator.getBrick());
        assertNotNull(generator.getBrick());
    }

    @Test
    void getNextBrick_does_not_consume_brick() {
        Brick firstPeek = generator.getNextBrick();
        Brick secondPeek = generator.getNextBrick();
        assertSame(firstPeek, secondPeek);
    }

    @Test
    void getBrick_consumes_brick_and_replenishes_queue() {
        Brick firstBrick = generator.getBrick();
        Brick nextBrick = generator.getNextBrick();

        assertNotSame(firstBrick, nextBrick);

        Brick subsequentBrick = generator.getBrick();
        Brick finalNextBrick = generator.getNextBrick();

        assertSame(nextBrick, subsequentBrick);
        assertNotSame(firstBrick, subsequentBrick);
        assertNotSame(subsequentBrick, finalNextBrick);
    }

    @Test
    void generated_bricks_are_valid_types() {
        Brick brick = generator.getBrick();
        assertTrue(brick instanceof IBrick || brick instanceof JBrick || brick instanceof LBrick ||
                brick instanceof OBrick || brick instanceof SBrick || brick instanceof TBrick ||
                brick instanceof ZBrick);
    }

    @Test
    void next_brick_is_valid_type() {
        Brick nextBrick = generator.getNextBrick();
        assertTrue(nextBrick instanceof IBrick || nextBrick instanceof JBrick || nextBrick instanceof LBrick ||
                nextBrick instanceof OBrick || nextBrick instanceof SBrick || nextBrick instanceof TBrick ||
                nextBrick instanceof ZBrick);
    }
}
package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.TBrick;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    @Test
    void testTBrickRotation() {
        Brick t = new TBrick();
        BrickRotator rotator = new BrickRotator();
        rotator.setBrick(t);

        int[][] original = rotator.getCurrentShape();
        rotator.setCurrentShape(1);  // rotate once

        int[][] rotated = rotator.getCurrentShape();

        assertNotEquals(original, rotated);  // must be different
    }
}

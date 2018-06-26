package org.spath.query;

import org.spath.SpathMatch;

public class SpathQueryBinary {
    private final SpathMatch left;
    private final SpathMatch right;

    public SpathQueryBinary(SpathMatch left, SpathMatch right) {
        this.left = left;
        this.right = right;
        validateArgs();
    }
    
    private void validateArgs() {
        if (left == null) {
            throw new IllegalArgumentException("Left operand cannot be null.");
        }
        if (right == null) {
            throw new IllegalArgumentException("Right operand cannot be null.");
        }
    }

    public SpathMatch getLeft() {
        return left;
    }

    public SpathMatch getRight() {
        return right;
    }
}
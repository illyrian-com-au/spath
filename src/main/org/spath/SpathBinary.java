package org.spath;

public class SpathBinary {
    private final SpathMatch left;
    private final SpathMatch right;

    public SpathBinary(SpathMatch left, SpathMatch right) {
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
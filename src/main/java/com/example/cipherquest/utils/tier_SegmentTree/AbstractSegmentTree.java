package com.example.cipherquest.utils.tier_SegmentTree;

import java.util.Arrays;

public abstract class AbstractSegmentTree {
    protected int size;
    protected int[] tree;

    public AbstractSegmentTree(int maxScore) {
        size = 1;
        while (size < maxScore + 1) size <<= 1;
        tree = new int[size << 1];
    }

    public void update(int index, int delta) {
        index += size;
        tree[index] += delta;
        while (index > 1) {
            index >>= 1;
            tree[index] = tree[index << 1] + tree[(index << 1) + 1];
        }
    }

    public void reset() {
        Arrays.fill(tree, 0);
    }

    public abstract int query(int left, int right); // 티어별로 쿼리 다르게 구현
}

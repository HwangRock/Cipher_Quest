package com.example.cipherquest.utils.tier_SegmentTree;

public class BronzeSegmentTree extends AbstractSegmentTree {

    public BronzeSegmentTree(int maxScore) {
        super(maxScore);
    }

    @Override
    public int query(int left, int right) {
        return queryInternal(1, 0, size - 1, left, right);
    }

    private int queryInternal(int node, int nodeLeft, int nodeRight, int queryLeft, int queryRight) {
        if (queryRight < nodeLeft || nodeRight < queryLeft) return 0;

        if (queryLeft <= nodeLeft && nodeRight <= queryRight) return tree[node];
        
        int mid = (nodeLeft + nodeRight) / 2;
        return queryInternal(node * 2, nodeLeft, mid, queryLeft, queryRight)
                + queryInternal(node * 2 + 1, mid + 1, nodeRight, queryLeft, queryRight);
    }
}

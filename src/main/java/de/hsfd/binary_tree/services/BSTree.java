package de.hsfd.binary_tree.services;

import de.hsfd.binary_tree.services.exceptions.TreeException;

@SuppressWarnings("rawtypes")
public class BSTree extends BinaryTree {

    @Override
    public void insert(Comparable x) throws TreeException {
        Node newNode = new Node(x);
        insertNode(newNode);
    }

    @Override
    protected Node deleteTarget(Node parentTarget, Node target, CHILD positionOfTarget) throws TreeException {
        // target found
        if(parentTarget == null && target.isLeaf()) { // target is the root and only one
            root = null;
        } else {
            // Case 2 if the target has two children. This case is from the Lecture AlgoDS 24/25 HS Fulda
            // Then take the inorder approach to find the smallest children of the right target's children
            if(target.getRight() != null && target.getLeft() != null) {
                replaceWithTheSmallestOfRightChildren(target);
            } else {
                return deleteTargetWithOneChildOrNone(parentTarget, target, positionOfTarget);
            }
        }
        return null;
    }
}

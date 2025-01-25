package de.hsfd.binary_tree.services;

import de.hsfd.binary_tree.services.exceptions.TreeException;

import static de.hsfd.binary_tree.services.Node.*;
import static de.hsfd.binary_tree.services.Node.COLOR.*;


@SuppressWarnings("rawtypes")
public class RBTree extends BinaryTree {

    @Override
    public void insert(Comparable data) {
        Node newNode = new Node(data, RED);
        insertNode(newNode);
        RBInsertFixup(newNode);
    }

    @Override
    protected Node deleteTarget(Node parentTarget, Node target, CHILD positionOfTarget) throws TreeException {
        // target found
        if(parentTarget == null && target.isLeaf()) {
            // target is the root and only one
            root = null;
        } else {
            Node x, nullNode = new Node(null, BLACK);
            COLOR targetOriginalColor = target.getColor();
            if(target.getRight() != null && target.getLeft() != null) {
                // Case 2 if the target has two children. Based on Lecture AlgoDS 24/25 HS Fulda
                // Then take the inorder approach to find the smallest children of the right target's children
                parentTarget = replaceWithTheSmallestOfRightChildren(target);

                if (target == parentTarget) {
                    // target.getRight() does not have other child.
                    // y.p == z, based on Delete Method the book Introduction to Algorithm
                    if(target.getRight() == null){
                        target.setRight(nullNode);
                        x = nullNode;
                    } else x = target.getRight();
                } else {
                    // the smallest value, children or grandchildren of target.getRight() exist
                    // y.p != z, based on Delete Method the book Introduction to Algorithm
                    if(parentTarget.getRight() == null){
                        parentTarget.setRight(nullNode);
                        x = nullNode;
                    } else x = parentTarget.getRight();
                }
            } else {
                x = deleteTargetWithOneChildOrNone(parentTarget, target, positionOfTarget);
                if(x.getData() == null) nullNode = x;
            }

            if(targetOriginalColor == BLACK && x.getData() != null) {
                this.newRBDeleteFixup(x);
            }
            return nullNode;
        }
        return null;
    }

    /**
     * A fix method of the tree to make sure the tree stays
     * balanced according to Red-Black Tree concept. Bottom-up approach.
     * Inspired from Introduction to Algorithm
     * @param z the inserted node that has been connected to the tree
     */
    private void RBInsertFixup(Node z) {
        while(z.getParent() != null &&
                z.getParent().getParent() != null &&
                z.getParent().getColor() == RED) {

            Node parent = z.getParent();
            Node grandParent = parent.getParent();

            boolean isParentLeftChildOfGrandParent = parent == grandParent.getLeft();
            Node case1 = isParentLeftChildOfGrandParent ? grandParent.getRight() : grandParent.getLeft();
            Node case2 = isParentLeftChildOfGrandParent ? parent.getRight() : parent.getLeft();

            Node uncleY = case1 == null ? new Node(null, BLACK) : case1;
            if(uncleY.getColor() == RED) {
                // Case 1
                parent.setColor(BLACK);
                uncleY.setColor(BLACK);
                grandParent.setColor(RED);
                z = grandParent;
            } else {
                if (z == case2) {
                    // Case 2
                    z = parent;
                    if(isParentLeftChildOfGrandParent) leftRotate(z);
                    else rightRotate(z);
                    parent = parent.getParent();
                }
                // Case 3
                parent.setColor(BLACK);
                grandParent.setColor(RED);
                if(isParentLeftChildOfGrandParent) rightRotate(grandParent);
                else leftRotate(grandParent);
            }
        }
        this.getRoot().setColor(BLACK);
    }

    /**
     * Repairs the Red-Black Tree properties after a node deletion, ensuring the tree maintains its color and
     * structural properties. This method resolves color violations and structure imbalances that may arise
     * during the deletion process.
     * This is an optimised algorithm design of {@link RBTree#RBDeleteFixup(Node)}
     *
     * @param x the node from which to start the fix-up process; this is typically
     *          the node in place of the deleted node or its sibling.
     */
    private void newRBDeleteFixup(Node x) {

        while (x != root && x.getColor() == BLACK ) {

            boolean isLeftChildrenOfParent = (x == x.getParent().getLeft());
            Node w = isLeftChildrenOfParent ? x.getParent().getRight() : x.getParent().getLeft();

            w = w == null ? new Node(null, BLACK) : w;

            if(w.getColor() == RED) {
                // case 1
                w.setColor(BLACK);
                x.getParent().setColor(RED);
                if(isLeftChildrenOfParent) leftRotate(x.getParent());
                else rightRotate(x.getParent());
                w = isLeftChildrenOfParent ? x.getParent().getRight() : x.getParent().getLeft();
            }

            w = w == null ? new Node(null, BLACK) : w;
            Node wLeftChild = w.getLeft() == null ? new Node(null, BLACK) : w.getLeft();
            Node wRightChild = w.getRight() == null ? new Node(null, BLACK) : w.getRight();

            if(wLeftChild.getColor() == BLACK && wRightChild.getColor() == BLACK) {
                // case 2
                w.setColor(RED);
                x = x.getParent();
            } else {
                COLOR wChildrenColor = isLeftChildrenOfParent ? wRightChild.getColor() : wLeftChild.getColor();
                if(wChildrenColor == BLACK) {
                    // case 3
                    // the children of x in the if statement must not be null,
                    // otherwise something totally wrong!
                    if(w.getLeft() != null || w.getRight() != null) {
                        if (isLeftChildrenOfParent) w.getLeft().setColor(BLACK);
                        else w.getRight().setColor(BLACK);
                    }

                    w.setColor(RED);

                    if (isLeftChildrenOfParent) rightRotate(w);
                    else leftRotate(w);

                    w = isLeftChildrenOfParent ?  x.getParent().getRight() : x.getParent().getLeft();
                    w = w == null ? new Node(null, BLACK) : w;
                }
                // case 4
                w.setColor(x.getParent().getColor());
                x.getParent().setColor(BLACK);
                if(isLeftChildrenOfParent) w.getRight().setColor(BLACK);
                else w.getLeft().setColor(BLACK);
                if(isLeftChildrenOfParent) leftRotate(x.getParent());
                else rightRotate(x.getParent());
                x = root;
            }
        }
        x.setColor(BLACK);
    }

    /**
     * Repairs the Red-Black Tree properties after a node deletion, ensuring the tree maintains its color and
     * structural properties. This method resolves color violations and structure imbalances that may arise
     * during the deletion process.
     * This approach is pure code based on the book of Introduction to algorithm
     * @deprecated Because there is another function that is much more concise. Please refer to {@link RBTree#newRBDeleteFixup(Node)}
     * @param x the node to start fixing from, typically the replacement node or the parent's child
     *          after the deletion.
     */
    @Deprecated
    @SuppressWarnings("unused")
    private void RBDeleteFixup(Node x) {
        while (x != root && x.getColor() == BLACK ) {
            if(x == x.getParent().getLeft()) {
                Node w = x.getParent().getRight();
                if(w.getColor() == RED) {
                    w.setColor(BLACK);
                    x.getParent().setColor(RED);
                    leftRotate(x.getParent());
                    w = x.getParent().getRight();
                }
                if(w.getLeft().getColor() == BLACK && w.getRight().getColor() == BLACK) {
                    w.setColor(RED);
                    x = x.getParent();
                } else {
                    if(w.getRight().getColor() == BLACK) {
                        w.getLeft().setColor(BLACK);
                        w.setColor(RED);
                        rightRotate(w);
                        w = x.getParent().getRight();
                    }
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(BLACK);
                    w.getRight().setColor(BLACK);
                    leftRotate(x.getParent());
                    x = root;
                }
            }
            else {
                Node w = x.getParent().getLeft();
                if(w.getColor() == RED) {
                    w.setColor(BLACK);
                    x.getParent().setColor(RED);
                    rightRotate(x.getParent());
                    w = x.getParent().getLeft();
                }
                if(w.getRight().getColor() == BLACK && w.getLeft().getColor() == BLACK) {
                    w.setColor(RED);
                    x = x.getParent();
                } else {
                    if(w.getLeft().getColor() == BLACK) {
                        w.getRight().setColor(BLACK);
                        w.setColor(RED);
                        leftRotate(w);
                        w = x.getParent().getLeft();
                    }
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(BLACK);
                    w.getLeft().setColor(BLACK);
                    rightRotate(x.getParent());
                    x = root;
                }
            }
            x.setColor(BLACK);
        }
    }


}

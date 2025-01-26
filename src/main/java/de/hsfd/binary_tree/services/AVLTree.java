package de.hsfd.binary_tree.services;

import de.hsfd.binary_tree.services.exceptions.TreeException;

@SuppressWarnings({"rawtypes","unchecked"})
public class AVLTree extends BinaryTree {

    @Override
    protected Node deleteTarget(Node parentTarget, Node target, CHILD positionOfTarget) throws TreeException {
        // target found
        if(parentTarget == null && target.isLeaf()) { // target is the root and only one
            root = null;
        } else {
            Node possibleNullNode = null;
            if(target.getRight() != null && target.getLeft() != null) {
                // Case 2 if the target has two children. This case is from the Lecture AlgoDS 24/25 HS Fulda
                // Then take the inorder approach to find the smallest children of the right target's children
                parentTarget = replaceWithTheSmallestOfRightChildren(target);
            } else {
                possibleNullNode =  deleteTargetWithOneChildOrNone(parentTarget, target, positionOfTarget);
            }
            balanceTheTree(parentTarget);
            return possibleNullNode;
        }
        return null;
    }

    @Override
    public void insert(Comparable x) throws TreeException {
        Node newNode = new Node(x);
        insertNode(newNode);
        balanceTheTree(newNode.getParent());
    }

    /**
     * Update the height of the node. This should be executed before calculating the balance factor
     * so that the height is actual.
     *
     * @param parent the node
     */
    private static void updateHeight(Node parent) {
        int leftHeight = height(parent.getLeft());
        int rightHeight = height(parent.getRight());
        parent.setHeight(Math.max(leftHeight,rightHeight) + 1);
    }

    /**
     * Balances the AVL tree using Iterative approach after an insertion or deletion to ensure the tree maintains
     * its AVL property where the height difference between left and right subtrees
     * of any node is at most 1.
     * <p> The method is being used for Insertion and Deletion. 
     * For the deletion, the parent and the newNode should be the same, Otherwise it is for the Insertion. 
     * These approach based on the geeks for geeks implementation to AVL-Tree</p>
     * <a href="https://www.geeksforgeeks.org/insertion-in-an-avl-tree/">Insertion-method Geeks for Geeks</a><br>
     * <a href="https://www.geeksforgeeks.org/deletion-in-an-avl-tree/">Deletion-method Geeks for Geeks</a>
     * 
     *
     * @param parent the parent node from which balancing starts
     * @throws TreeException if a violation of AVL tree properties persists after balancing
     */
    private void balanceTheTree(Node parent) throws TreeException {
        while(parent != null) {
            updateHeight(parent);
            int balance = calculateBalanceFactor(parent);

            if(balance > 1 || balance < -1) {
                if (balance > 0) { //left heavy from the parent
                    if(calculateBalanceFactor(parent.getLeft()) < 0){
                        leftRotate(parent.getLeft());// Left Right Case
                    }
                    rightRotate(parent);
                } else { // (balance < 0) right heavy from the parent
                    if (calculateBalanceFactor(parent.getRight()) > 0 ){
                        rightRotate(parent.getRight());// Right Left Case
                    }
                    leftRotate(parent);
                }
                if(parent.getParent() == null) this.root = parent;

                if (!(calculateBalanceFactor(parent) <= 1 && calculateBalanceFactor(parent) >= -1)) // this node to the leaf should now be balanced
                    throw new TreeException("Violates the AVL tree rule\nNode: " +
                            parent.getData() +"\nHeight: " + parent.getHeight() + "\nTree:\n"
                            + this.getTreePrinter().prettyPrint());
            }

            parent = parent.getParent();
        }
    }

    /**
     * Get Balance factor of node N: left - right
     * <p>
     * <p>> if left is bigger than 1, three is an imbalance on the left side </p>
     * <p>> if right is smaller than -1, there is an imbalance on the right side </p>
     * @param n the root to be checked whether imbalance exists
     * @return the integer result of the balance factor
     */
    static int calculateBalanceFactor(Node n) {
        if (n == null) // it means the node has not been initialised
            return 0;
        return height(n.getLeft()) - height(n.getRight());
    }

    /**
     * A function to balancing the tree of the newNode with bottom up approach <p>
     * inspired from: <a href="https://www.geeksforgeeks.org/insertion-in-an-avl-tree/">Geek For Geeks</a>
     * @deprecated
     *      <p>This method is no longer acceptable because it is not flexible with the implementation. </p>
     *      <p> We want a function that could be also used for the other function like delete, not only for insert</p>
     * @param parent the parent of the newNode
     * @param newNode will be used for the comparison checking.
     */
    @Deprecated
    @SuppressWarnings("unused")
    private void balanceTheTreeOld(Node parent, Node newNode) {
        while(parent != null) {
            int leftHeight = height(parent.getLeft());
            int rightHeight = height(parent.getRight());
            parent.setHeight(Math.max(leftHeight,rightHeight) + 1);

            // this code can only be executed if initialised height for a node is 0!
            if(parent.getHeight() > 1) {
                int balance = calculateBalanceFactor(parent);
                if (balance > 0) { //left heavy from the parent
                    if (newNode.getData().compareTo(parent.getLeft().getData()) > 0) {
                        leftRotate(parent.getLeft()); // Left Right Case
                    }
                    rightRotate(parent);
                } else if (balance < 0) { // right heavy from the parent
                    if (newNode.getData().compareTo(parent.getRight().getData()) < 0) {
                        rightRotate(parent.getRight());// Right Left Case
                    }
                    leftRotate(parent);
                }
                if(parent.getParent() == null) this.root = parent;
                if(parent.getHeight() > 1)
                    throw new TreeException("Violates the AVL tree rule\nNode: " +
                            parent.getData() +"\nHeight: " + parent.getHeight() + "\nTree:\n"
                            + this.getTreePrinter().prettyPrint());
            }

            parent = parent.getParent();
        }
    }
}

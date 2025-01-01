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
                replaceWithTheSmallestOfRightChildren(target);
            } else {
                possibleNullNode =  deleteTargetWithOneChildOrNone(parentTarget, target, positionOfTarget);
            }
            if(target.getParent() == null && target != root) target = parentTarget;
            balanceTheTree(target,target);
            return possibleNullNode;
        }
        return null;
    }

    @Override
    public void insert(Comparable x) throws TreeException {
        Node newNode = new Node(x);
        insertNode(newNode);
        balanceTheTree(newNode.getParent(),newNode);
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
     * @param newNode the newly inserted or affected node requiring balance checks (especially for insert)
     *                otherwise for delete should only be newNode == parent
     * @throws TreeException if a violation of AVL tree properties persists after balancing
     */
    private void balanceTheTree(Node parent, Node newNode) throws TreeException {
        while(parent != null) {
            updateHeight(parent);
            int balance = getBalance(parent);

            if(balance > 1 || balance < -1) {
                if (balance > 0) { //left heavy from the parent
                    if(parent.equals(newNode) && getBalance(parent.getLeft()) < 0 // delete case
                            || !parent.equals(newNode) &&  newNode.getData().compareTo(parent.getLeft().getData()) > 0) { //insert case
                        leftRotate(parent.getLeft());// Left Right Case
                    }
                    rightRotate(parent);
                } else { // (balance < 0) right heavy from the parent
                    if (parent.equals(newNode) && getBalance(parent.getRight()) > 0 // delete case
                            || !parent.equals(newNode) && newNode.getData().compareTo(parent.getRight().getData()) < 0){ //insert case
                        rightRotate(parent.getRight());// Right Left Case
                    }
                    leftRotate(parent);
                }
                if(parent.getParent() == null) this.root = parent;
                if(getBalance(parent) > 1)
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
    static int getBalance(Node n) {
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
                int balance = getBalance(parent);
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

    /**
     * Deletes a target node from the AVL tree following AVL deletion rules.
     * This method is deprecated and is preserved for historical purposes only.
     * The deletion process ensures that the AVL balancing property is preserved.
     *
     * @deprecated this code does not work as it suppose to be. it has too complicated assumption
     * that could make everything more complicated than it should be
     * @param parentTarget the parent node of the target being deleted, or null if the target is the root
     * @param target the node to be deleted
     * @param positionOfTarget the position of the target node relative to its parent (left or right child)
     * @throws TreeException if the deletion leads to a violation of the AVL tree properties
     */
    @Deprecated
    @SuppressWarnings("unused")
    protected void deleteTargetOld(Node parentTarget, Node target, CHILD positionOfTarget) throws TreeException {
        // target found
        if(parentTarget == null && target.isLeaf()) { // target is the root and only one
            root = null;
        } else {
            Node targetRightChild = target.getRight();
            if(target.getRight() != null && target.getLeft() != null) {
                // Case 2 if the target has two children. This case is from the Lecture AlgoDS 24/25 HS Fulda
                // Then take the inorder approach to find the smallest children of the right target's children
                parentTarget = replaceWithTheSmallestOfRightChildren(target);
            } else {
                deleteTargetWithOneChildOrNone(parentTarget, target, positionOfTarget);
            }

            while(parentTarget != null){
                Node node = parentTarget.getRight() != null && parentTarget.getRight() == targetRightChild ? parentTarget.getRight()
//                        : parentTarget.getLeft() != null && parentTarget.getLeft() == targetRightChild ? parentTarget.getLeft()
                        : parentTarget.getLeft();
                if (node == null && parentTarget != root) { // if node is null, find predecessor to balance the tree
                    node = parentTarget;
                    parentTarget = parentTarget.getParent();
                } else if (node != null) {
                    // we assume if there is only one child/subtree in the node, that means
                    // it might not be balanced further up. therefore take child of the node as node
                    if(node.getLeft() == null && node.getRight() != null) node = node.getRight();
                    if(node.getRight() == null && node.getLeft() != null) node = node.getLeft();
                } else {
                    // we assume that there is just one element in the tree
                    node = parentTarget;
                }
                balanceTheTree(parentTarget,node);
                parentTarget = parentTarget == null ? null : parentTarget.getParent();
            }

        }

    }

}

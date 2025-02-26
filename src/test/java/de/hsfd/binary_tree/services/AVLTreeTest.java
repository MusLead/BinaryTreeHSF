package de.hsfd.binary_tree.services;

import de.hsfd.binary_tree.services.exceptions.TreeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static de.hsfd.binary_tree.services.BSTreeTest.isBinarySearchTree;
import static de.hsfd.binary_tree.services.Node.DEFAULT_HEIGHT;
import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {

    @Test
    void insert_singleRotationLeft() {
        AVLTree avlTree = new AVLTree();
        try {
            avlTree.insert(10);
            avlTree.insert(20);
            avlTree.insert(30); // Should trigger left rotation

            assertEquals(20, avlTree.getRoot().getData());
            assertEquals(10, avlTree.getRoot().getLeft().getData());
            assertEquals(30, avlTree.getRoot().getRight().getData());
        } catch (TreeException e) {
            fail("Exception occurred during insertion: " + e.getMessage());
        }
    }

    @Test
    void insert_singleRotationRight() {
        AVLTree avlTree = new AVLTree();
        try {
            avlTree.insert(30);
            avlTree.insert(20);
            avlTree.insert(10); // Should trigger right rotation

            assertEquals(20, avlTree.getRoot().getData());
            assertEquals(10, avlTree.getRoot().getLeft().getData());
            assertEquals(30, avlTree.getRoot().getRight().getData());
        } catch (TreeException e) {
            fail("Exception occurred during insertion: " + e.getMessage());
        }
    }

    @Test
    void insert_doubleRotationLeftRight() {
        AVLTree avlTree = new AVLTree();
        try {
            avlTree.insert(30);
            avlTree.insert(10);
            avlTree.insert(20); // Should trigger left-right rotation

            assertEquals(20, avlTree.getRoot().getData());
            assertEquals(10, avlTree.getRoot().getLeft().getData());
            assertEquals(30, avlTree.getRoot().getRight().getData());
        } catch (TreeException e) {
            fail("Exception occurred during insertion: " + e.getMessage());
        }
    }

    @Test
    void insert_doubleRotationRightLeft() {
        AVLTree avlTree = new AVLTree();
        try {
            avlTree.insert(10);
            avlTree.insert(30);
            avlTree.insert(20); // Should trigger right-left rotation

            assertEquals(20, avlTree.getRoot().getData());
            assertEquals(10, avlTree.getRoot().getLeft().getData());
            assertEquals(30, avlTree.getRoot().getRight().getData());
        } catch (TreeException e) {
            fail("Exception occurred during insertion: " + e.getMessage());
        }
    }

    @Test
    void leftRotate() {
        AVLTree avlTree = new AVLTree();
        try {
            avlTree.insert(10);
            avlTree.insert(20);
            avlTree.insert(30); // Trigger left rotation

            assertEquals(20, avlTree.getRoot().getData());
        } catch (TreeException e) {
            fail("Exception occurred during left rotation: " + e.getMessage());
        }
    }

    @Test
    void rightRotate() {
        AVLTree avlTree = new AVLTree();
        try {
            avlTree.insert(30);
            avlTree.insert(20);
            avlTree.insert(10); // Trigger right rotation

            assertEquals(20, avlTree.getRoot().getData());
        } catch (TreeException e) {
            fail("Exception occurred during right rotation: " + e.getMessage());
        }
    }

    @Test
    void delete_cases() {
        AVLTree avlTree = new AVLTree();
        try {
            // Case 1: Delete a leaf node
            avlTree.insert(20);
            avlTree.insert(10);
            avlTree.insert(30);
            System.out.println("The tree:\n"+avlTree.getTreePrinter().prettyPrint());
            avlTree.delete(10); // Deleting leaf node
            System.out.println("After delete 10:\n"+avlTree.getTreePrinter().prettyPrint());
            assertNull(avlTree.getRoot().getLeft());

            // Case 2: Delete a node with one child
            avlTree.insert(25);
            System.out.println("After insert 25:\n"+avlTree.getTreePrinter().prettyPrint());
            avlTree.delete(30); // Deleting node with one child
            System.out.println("After delete 30:\n"+avlTree.getTreePrinter().prettyPrint());
            assertEquals(25, avlTree.getRoot().getData());

            // Case 3: Delete a node with two children
            avlTree.insert(5);
            System.out.println("After insert 5:\n"+avlTree.getTreePrinter().prettyPrint());
            avlTree.insert(15);
            System.out.println("After insert 15:\n"+avlTree.getTreePrinter().prettyPrint());
            avlTree.delete(20); // Deleting root node with two children
            System.out.println("After delete 20:\n"+avlTree.getTreePrinter().prettyPrint());
            assertEquals(15, avlTree.getRoot().getData());

            // Case 4: Delete root node
            avlTree.delete(15);
            System.out.println("After delete 15:\n"+avlTree.getTreePrinter().prettyPrint());
            assertEquals(25, avlTree.getRoot().getData());

            // Case 5: Delete causing balancing
            avlTree.insert(40);
            avlTree.insert(50);
            System.out.println("After insert 40 and 50:\n"+avlTree.getTreePrinter().prettyPrint());
            avlTree.delete(5); // Should trigger balancing
            System.out.println("After delete 5:\n"+avlTree.getTreePrinter().prettyPrint());
            assertEquals(50, avlTree.getRoot().getRight().getData());

            //case 6:
            avlTree.insert(42);
            System.out.println("After insert 42:\n"+avlTree.getTreePrinter().prettyPrint());
            assertEquals(50, avlTree.getRoot().getRight().getData());
            avlTree.delete(40);
            System.out.println("After delete 40:\n"+avlTree.getTreePrinter().prettyPrint());
            assertEquals(42, avlTree.getRoot().getData());

        } catch (TreeException e) {
            fail("Exception occurred during deletion: " + e.getMessage());
        }
    }

    private AVLTree avl;

    @BeforeEach
    public void setUp() {
        avl = new AVLTree();
    }

    @Test
    public void testInsertion() throws TreeException, IllegalAccessException {
        avl.insert(50);
        avl.insert(30);
        avl.insert(70);
        avl.insert(20);
        avl.insert(40);
        avl.insert(60);
        avl.insert(80);

        // Test AVL tree properties
        assertTrue(isBinarySearchTree(avl.getRoot()));
        assertTrue(isBalanced(avl.getRoot()));
    }

    @Test
    public void testUnbalancedInsertion() throws TreeException, IllegalAccessException {
        avl.insert(50);
        avl.insert(30);
        avl.insert(20);

        // Test AVL tree properties
        assertTrue(isBinarySearchTree(avl.getRoot()));
        assertEquals(30, (int) avl.getRoot().getData());
        assertTrue(isBalanced(avl.getRoot())); // This should be false if the tree is unbalanced.
    }


    @Test
    public void testDeletion() throws TreeException, IllegalAccessException {
        avl.insert(50);
        avl.insert(30);
        avl.insert(70);
        avl.insert(20);
        avl.insert(40);
        avl.insert(60);
        avl.insert(80);

        avl.delete(50);
        avl.delete(30);

        // Test AVL tree properties after deletion
        assertTrue(isBinarySearchTree(avl.getRoot()));
        assertTrue(isBalanced(avl.getRoot()));
    }

    @Test
    void testDeleteOnly11() throws IOException {
        avl.insert(25);
        avl.insert(15);
        avl.insert(55);
        avl.insert(11);
        avl.insert(35);
        avl.insert(77);
        avl.insert(66);
        avl.exportDOT("avlbfr_testDeleteOnly11.dot");

        avl.delete(11);
        avl.exportDOT("avlaft_testDeleteOnly11.dot");

        assertTrue(isBalanced(avl.getRoot()));
        assertEquals(55, avl.getRoot().getData());
    }


    private boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }

        int leftHeight = heightOf(node.getLeft());
        int rightHeight = heightOf(node.getRight());

        return Math.abs(leftHeight - rightHeight) <= 1 &&
                isBalanced(node.getLeft()) &&
                isBalanced(node.getRight());
    }

    private int heightOf(Node node) {
        if (node == null) {
            return DEFAULT_HEIGHT - 1; // -1 or 0,  depending on your height definition
        }
        return node.getHeight();
    }
}

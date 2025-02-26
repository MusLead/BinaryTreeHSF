package de.hsfd.binary_tree.services;

import static org.junit.jupiter.api.Assertions.*;

import de.hsfd.binary_tree.services.exceptions.TreeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RBTreeTest {

    @Test
    public void testInsertSingleNode() {
        RBTree rbTree = new RBTree();
        rbTree.insert(10);
        assertNotNull(rbTree.getRoot());
        assertEquals(10, rbTree.getRoot().getData());
        assertEquals(Node.COLOR.BLACK, rbTree.getRoot().getColor());
    }

    @Test
    public void testInsertNoDuplicateValues() {
        RBTree rbTree = new RBTree();
        rbTree.insert(10);
        rbTree.insert(20);
        assertThrows(TreeException.class, () -> rbTree.insert(10), "Adding the duplicate values should be prohibited."); // Attempt to insert duplicate value

        assertNotNull(rbTree.getRoot());
        assertEquals(10, rbTree.getRoot().getData());
        assertNotNull(rbTree.getRoot().getRight());
        assertEquals(20, rbTree.getRoot().getRight().getData());
        assertNull(rbTree.getRoot().getLeft());
    }

    @Test
    public void testInsertMaintainsRedBlackProperties() {
        RBTree rbTree = new RBTree();
        rbTree.insert(10);
        rbTree.insert(15);
        rbTree.insert(5);
        rbTree.insert(20);
        rbTree.insert(2);
        rbTree.insert(8);
        System.out.println("After insert 10, 15, 5, 20, 2, and 8:\n" + rbTree.getTreePrinter().prettyPrint());

        // Check root is black
        assertEquals(Node.COLOR.BLACK, rbTree.getRoot().getColor());

        // Check red nodes have black children
        checkRedBlackProperties(rbTree.getRoot());

        // Check tree is balanced (black height is consistent)
        int blackHeight = calculateBlackHeight(rbTree.getRoot());
        assertTrue(isBalanced(rbTree.getRoot(), blackHeight));
    }

    private void checkRedBlackProperties(Node node) {
        if (node == null || node.getData() == null) {
            return;
        }
        if (node.getColor() == Node.COLOR.RED) {
            assertEquals(Node.COLOR.BLACK, node.getLeft() != null ? node.getLeft().getColor() : Node.COLOR.BLACK);
            assertEquals(Node.COLOR.BLACK, node.getRight() != null ? node.getRight().getColor() : Node.COLOR.BLACK);
        }
        checkRedBlackProperties(node.getLeft());
        checkRedBlackProperties(node.getRight());
    }

    private int calculateBlackHeight(Node node) {
        if (node == null || node.getData() == null) {
            return 1; // Null nodes count as black
        }
        int leftBlackHeight = calculateBlackHeight(node.getLeft());
        int rightBlackHeight = calculateBlackHeight(node.getRight());
        return Math.max(leftBlackHeight, rightBlackHeight) + (node.getColor() == Node.COLOR.BLACK ? 1 : 0);
    }

    private boolean isBalanced(Node node, int blackHeight) {
        if (node == null || node.getData() == null) {
            return blackHeight == 1;
        }
        int leftBlackHeight = calculateBlackHeight(node.getLeft());
        int rightBlackHeight = calculateBlackHeight(node.getRight());
        return leftBlackHeight == rightBlackHeight && isBalanced(node.getLeft(), blackHeight - (node.getColor() == Node.COLOR.BLACK ? 1 : 0)) && isBalanced(node.getRight(), blackHeight - (node.getColor() == Node.COLOR.BLACK ? 1 : 0));
    }

    @Test
    public void testMultipleInsertsAndTreeStructure() {
        RBTree rbTree = new RBTree();
        rbTree.insert(30);
        rbTree.insert(20);
        rbTree.insert(40);
        rbTree.insert(10);
        rbTree.insert(25);
        rbTree.insert(35);
        rbTree.insert(50);

        // Check root and colors
        assertNotNull(rbTree.getRoot());
        assertEquals(30, rbTree.getRoot().getData());
        assertEquals(Node.COLOR.BLACK, rbTree.getRoot().getColor());

        // Check that tree maintains red-black properties
        checkRedBlackProperties(rbTree.getRoot());

        // Check tree is balanced
        int blackHeight = calculateBlackHeight(rbTree.getRoot());
        assertTrue(isBalanced(rbTree.getRoot(), blackHeight));
    }


    @Test
    public void testDeleteLeafNode() {
        RBTree rbTree = new RBTree();
        rbTree.insert(30);
        rbTree.insert(20);
        rbTree.insert(40);
        rbTree.insert(10);

        // Delete leaf node (10)
        rbTree.delete(10);

        // Verify node was deleted
        assertNull(rbTree.getRoot().getLeft().getLeft());

        // Check tree maintains red-black properties
        checkRedBlackProperties(rbTree.getRoot());

        // Check tree is balanced
        int blackHeight = calculateBlackHeight(rbTree.getRoot());
        assertTrue(isBalanced(rbTree.getRoot(), blackHeight));
    }

    @Test
    public void testDeleteNodeWithRotation() {
        RBTree rbTree = new RBTree();
        rbTree.insert(10);
        rbTree.insert(5);
        rbTree.insert(7);
        System.out.println("After insert 10, 5, and 7:\n" + rbTree.getTreePrinter().prettyPrint());
        assertEquals(Node.COLOR.RED, rbTree.getRoot().getLeft().getColor(), "because of the algorithm of deleteFixup in the book");

        // Delete node with single child (5)
        rbTree.delete(5);
        System.out.println("After delete 5:\n" + rbTree.getTreePrinter().prettyPrint());
        // Verify node was deleted and child replaced it
        assertEquals(7, rbTree.getRoot().getData());

        // Check tree maintains red-black properties
        checkRedBlackProperties(rbTree.getRoot());

        // Check tree is balanced
        int blackHeight = calculateBlackHeight(rbTree.getRoot());
        assertTrue(isBalanced(rbTree.getRoot(), blackHeight));
    }

    @Test
    public void testDeleteNodeWithTwoChildren() {
        RBTree rbTree = new RBTree();
        rbTree.insert(20);
        rbTree.insert(10);
        rbTree.insert(30);
        rbTree.insert(25);
        rbTree.insert(35);

        // Delete node with two children (30)
        rbTree.delete(30);

        // Verify node was deleted
        assertEquals(35, rbTree.getRoot().getRight().getData());

        // Check tree maintains red-black properties
        checkRedBlackProperties(rbTree.getRoot());

        // Check tree is balanced
        int blackHeight = calculateBlackHeight(rbTree.getRoot());
        assertTrue(isBalanced(rbTree.getRoot(), blackHeight));
    }

    @Test
    public void testDeleteRootNode() {
        RBTree rbTree = new RBTree();
        rbTree.insert(15);
        rbTree.insert(10);
        rbTree.insert(20);

        // Delete root node (15)
        rbTree.delete(15);

        // Verify new root is correct
        assertEquals(20, rbTree.getRoot().getData());
        assertEquals(Node.COLOR.BLACK, rbTree.getRoot().getColor());

        // Check tree maintains red-black properties
        checkRedBlackProperties(rbTree.getRoot());

        // Check tree is balanced
        int blackHeight = calculateBlackHeight(rbTree.getRoot());
        assertTrue(isBalanced(rbTree.getRoot(), blackHeight));
    }

    @Test
    public void testDeleteNodeMaintainsRedBlackProperties() {
        RBTree rbTree = new RBTree();
        rbTree.insert(50);
        rbTree.insert(40);
        rbTree.insert(60);
        rbTree.insert(30);
        rbTree.insert(45);
        System.out.println("After insert 50, 40, 60, 30, and 45:\n" + rbTree.getTreePrinter().prettyPrint());
        // Delete a node that requires fixups (40)
        rbTree.delete(40);
        System.out.println("After delete 40:\n" + rbTree.getTreePrinter().prettyPrint());

        // Verify node was deleted
        assertEquals(45, rbTree.getRoot().getLeft().getData());

        // Check tree maintains red-black properties
        checkRedBlackProperties(rbTree.getRoot());

        // Check tree is balanced
        int blackHeight = calculateBlackHeight(rbTree.getRoot());
        assertTrue(isBalanced(rbTree.getRoot(), blackHeight));
    }

    RBTree rbt;

    @BeforeEach
    public void setUp() {
        rbt = new RBTree();
    }

    @Test
    public void testInsertion() {
        my_insert(50);
        my_insert(30);
        my_insert(20);
        my_insert(40);
        my_insert(70);
        my_insert(60);
        my_insert(80);

        // Test for tree structure, colors, and properties
        // You can implement a method in RBT to check tree properties if needed
        assertTrue(checkRBTProperties(rbt.getRoot()));
    }

    @Test
    public void testInsertionDeletion() throws TreeException {
        my_insert(3);
        my_insert(4);
        my_insert(5);
        my_insert(10);
        my_insert(20);
        my_insert(30);
        my_insert(23);
        my_insert(24);
        int[] values = {3, 4, 5, 10, 20, 30, 23, 24};
        for (int value : values) {
            System.out.println("Delete: " + value);
            rbt.delete(value);
        }
        assertTrue(checkRBTProperties(rbt.getRoot()));
    }

    @Test
    public void testDeletion() throws TreeException {
        my_insert(50);
        my_insert(30);
        my_insert(70);
        rbt.delete(30);

        // Test for tree structure, colors, and properties after deletion
        assertTrue(checkRBTProperties(rbt.getRoot()));
    }

    @Test
    public void testTreePropertiesAfterOperations() throws TreeException {
        my_insert(50);
        my_insert(30);
        my_insert(20);
        my_insert(40);
        my_insert(70);
        my_insert(60);
        my_insert(80);

        rbt.delete(60);
        rbt.delete(20);

        // Test for tree structure, colors, and properties
        assertTrue(checkRBTProperties(rbt.getRoot()));
    }

    @Test
    public void testInsertionCase2andCase3() throws TreeException {
        my_insert(41);
        my_insert(38);
        my_insert(21);
        my_insert(12);
        my_insert(19);
        my_insert(8);

        assertTrue(checkRBTProperties(rbt.getRoot()));
        assertTrue(checkRBTProperties(rbt.getRoot()));
        assertEquals(Node.COLOR.BLACK, rbt.getRoot().getColor());
        assertEquals(Node.COLOR.BLACK, rbt.getRoot().getRight().getColor());
        assertEquals(Node.COLOR.RED, rbt.getRoot().getLeft().getColor());
        assertEquals(19, rbt.getRoot().getLeft().getData());
    }

    public boolean checkRBTProperties(Node root) {
        if (root == null) {
            return true; // An empty tree is a valid RBT
        }

        if (root.getColor() == Node.COLOR.RED) {

            return false; // The root must be black
        }

        return checkRBTPropertiesRec(root, 0, -1);
    }

    private boolean checkRBTPropertiesRec(Node node, int blackCount, int pathBlackCount) {
        if (node == null) {
            if (pathBlackCount == -1) {
                pathBlackCount = blackCount;
            }
            return pathBlackCount == blackCount;
        }

        if (node.getColor() == Node.COLOR.BLACK) {
            blackCount++;
        } else {
            // Check if left or right children are red (and exist)
            if ((node.getLeft() != null && node.getLeft().getColor() == Node.COLOR.RED) ||
                    (node.getRight() != null && node.getRight().getColor() == Node.COLOR.RED)) {
                return false; // Red node having red child
            }
        }

        return checkRBTPropertiesRec(node.getLeft(), blackCount, pathBlackCount) &&
                checkRBTPropertiesRec(node.getRight(), blackCount, pathBlackCount);
    }

    private void my_insert(int value) {
        assertDoesNotThrow(() -> rbt.insert(value));
    }
}

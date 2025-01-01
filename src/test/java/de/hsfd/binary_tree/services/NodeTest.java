package de.hsfd.binary_tree.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void setLeft() {
        // Create nodes
        Node parent = new Node(10);
        Node child = new Node(5);

        // Set child as the left child of the parent
        parent.setLeft(child);

        // Assert parent-child connection
        assertEquals(child, parent.getLeft(), "Child node should be the left child of the parent.");
        assertEquals(parent, child.getParent(), "Parent node should be correctly set in the child.");

        // Replace left child
        Node newChild = new Node(3);
        parent.setLeft(newChild);

        // Assert the new child connection
        assertEquals(newChild, parent.getLeft(), "New child should replace the old left child.");
        assertEquals(parent, newChild.getParent(), "Parent node should be correctly set in the new child.");

        // Assert the old child is disconnected
        assertNull(child.getParent(), "Old child should no longer have a parent.");
    }

    @Test
    void setRight() {
        // Create nodes
        Node parent = new Node(10);
        Node child = new Node(15);

        // Set child as the right child of the parent
        parent.setRight(child);

        // Assert parent-child connection
        assertEquals(child, parent.getRight(), "Child node should be the right child of the parent.");
        assertEquals(parent, child.getParent(), "Parent node should be correctly set in the child.");

        // Replace right child
        Node newChild = new Node(20);
        parent.setRight(newChild);

        // Assert the new child connection
        assertEquals(newChild, parent.getRight(), "New child should replace the old right child.");
        assertEquals(parent, newChild.getParent(), "Parent node should be correctly set in the new child.");

        // Assert the old child is disconnected
        assertNull(child.getParent(), "Old child should no longer have a parent.");
    }

    @Test
    void inorder() {
        Node root = new Node(10);
        Node left = new Node(5);
        Node right = new Node(15);

        root.setLeft(left);
        root.setRight(right);

        String result = Node.inorder(root);
        assertEquals("5 10 15", result, "In-order traversal should return '5 10 15'.");
    }

    @Test
    void preorder() {
        Node root = new Node(10);
        Node left = new Node(5);
        Node right = new Node(15);

        root.setLeft(left);
        root.setRight(right);

        String result = Node.preorder(root);
        assertEquals("10 5 15", result, "Pre-order traversal should return '10 5 15'.");
    }

    @Test
    void postorder() {
        Node root = new Node(10);
        Node left = new Node(5);
        Node right = new Node(15);

        root.setLeft(left);
        root.setRight(right);

        String result = Node.postorder(root);
        assertEquals("5 15 10", result, "Post-order traversal should return '5 15 10'.");
    }
}

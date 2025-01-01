package de.hsfd.binary_tree.services;

@SuppressWarnings("rawtypes")
public class Node {

    public COLOR getColor() {
        return color;
    }

    public void setColor(COLOR color) {
        this.color = color;
    }

    public enum COLOR {RED, BLACK}

    /**
     * A default value when a node data is not null
     */
    public static final int DEFAULT_HEIGHT = 1;
    private Comparable data;
    private Node left;
    private Node right;
    private Node parent;
    private int height = DEFAULT_HEIGHT;
    private COLOR color;

    public Node(Comparable data) {
        this.data = data;
        if(data == null) this.height = DEFAULT_HEIGHT - 1 ;
    }

     Node(Comparable data, COLOR color) {
            this.data = data;
            this.color = color;
            if(data == null) this.height = DEFAULT_HEIGHT - 1;
    }

    public Node getParent() {
        return parent;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node n) {
        checkParentConnection(n);
        // make sure that this.left.parent also null, so that the left children of this
        // does not have the connection to the parent or predecessor.
        if (this.left != null) this.left.parent = null;
        this.left = n;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node n) {
        checkParentConnection(n);
        // make sure that this.right.parent also null, so that the right children of this
        // does not have the connection to the parent or predecessor.
        if (this.right != null) this.right.parent = null;
        this.right = n;
    }

    private void checkParentConnection(Node n) {
        if(n != null) {
            if (n.parent != null) {
                // make sure that the n does not have any parent anymore
                // since we want to connect n parent to this object
                Node parent = n.parent;
                if (parent.right == n) parent.right = null;
                else parent.left = null;
            }
            n.parent = this;
        }
    }

    public Comparable getData() {
        return data;
    }

    public void setData(Comparable data) {
        this.data = data;
    }

    public static String inorder(Node n) {
        // Base case: return an empty string for null or empty nodes.
        if (n == null) return "";

        // Recursively get in-order traversal for left and right subtrees.
        String left = inorder(n.left) + (n.left == null ? "" : " ") ;
        String right = inorder(n.right);

        // Combine results with the current node's data.
        return (left + n.data.toString() + " " + right).trim();
    }

    public static String preorder(Node n) {
        if (n == null) return "";
        String left = preorder(n.left) + (n.left == null ? "" : " ");
        String right = preorder(n.right);
        return (n.data.toString() + " " + left + right).trim();
    }

    public static String postorder(Node n) {
        if (n == null) return "";
        String left = postorder(n.left) + (n.left == null ? "" : " ");
        String right = postorder(n.right) + (n.right == null ? "" : " ");
        return (left + right + n.data.toString()).trim();
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", height=" + height +
                ", color=" + color +
                '}';
    }
}

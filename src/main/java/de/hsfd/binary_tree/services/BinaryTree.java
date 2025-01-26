package de.hsfd.binary_tree.services;

import de.hsfd.binary_tree.TreePrinter;
import de.hsfd.binary_tree.services.exceptions.TreeException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BinaryTree {

    /**
     * Inserts a new element into the binary tree while maintaining its structure.
     * This is an abstract method, and its implementation should define the specific
     * logic for insertion based on the type of binary tree (e.g., Binary Search Tree, AVL Tree, etc.).
     * Please implement the insertNode() function to this method, so that it will be inserted directly into the respected position.
     * <p> Idea Inspired from AlgoDS Lecture HS Fulda 24/25 </p>
     * @param data the value to be inserted into the binary tree. Must implement the Comparable interface
     * to allow comparisons with other elements in the tree.
     */
    public abstract void insert(Comparable data);

    /**
     * Deletes a specified target node from the binary tree.
     * This method will only be executed, when the node is found in the tree by the delete method.
     * The method handles the appropriate removal of the target based on its position and updates the parent node's reference.
     * This is an abstract method, and its implementation should define the specific logic for deletion.
     * <p> Idea Inspired from AlgoDS Lecture HS Fulda 24/25 </p>
     *
     * @param parentTarget     The parent node of the target node that needs to be deleted.
     * @param target           The node to be deleted from the binary tree.
     * @param positionOfTarget Specifies whether the target node is the left or right child of the parent node.
     * @return a possible nullNode that will be deleted later if exist
     * @throws TreeException If a problem occurs during node deletion.
     */
    protected abstract Node deleteTarget(Node parentTarget, Node target, CHILD positionOfTarget) throws TreeException;

    protected enum CHILD {rightChildOfParent, leftChildOfParent}

    protected Node root = null;

    public Node getRoot() {
        return root;
    }

    /**
     * A search function to look if the value x exist in the tree. <p>
     * inspired from: AlgoDS Lecture HS Fulda 24/25
     * @param x the target value
     * @return the same value if found, otherwise null
     */
    public Comparable lookup(Comparable x) {
        Node k = root;
        while(k != null) {
            if(x.compareTo(k.getData()) < 0) { // x is smaller than k
                k = k.getLeft();
            } else if(x.compareTo(k.getData()) > 0){ // x is bigger than k
                k = k.getRight();
            } else if(x.compareTo(k.getData()) == 0) {
                return k.getData();
            }
        }
        return null;
    }

    /**
     * <p>
     * Insert the node into the tree without any self-balancing methods.
     * After the execution, the newNode will have a parent if root != null. </p>
     * <p>With this implementation without the wrapper, it can compare the objects
     * whether it is char or int</p>
     * inspired from: AlgoDS Lecture HS Fulda 24/25
     * @param newNode will be added to the tree
     */
    protected void insertNode(Node newNode) {
        if(root == null)
            root = newNode;
        else {
            Node parent = getParentOf(newNode.getData());

            if(newNode.getData().compareTo(parent.getData()) < 0) {
                parent.setLeft(newNode);
            } else if(newNode.getData().compareTo(parent.getData()) > 0) {
                parent.setRight(newNode);
            } else throw new TreeException("The value is already in the tree. No Parent will be returned");
        }
    }

    /**
     * This function is intended for insert method. (top-down approach)
     * Iterate through the nodes of the tree to find a parent node where a new node with the value x can be attached.
     * If the value of a node is the same as x, the function should return an error message, as duplicate values are not accepted. <p>
     *     inspired from: AlgoDS Lecture HS Fulda 24/25
     * </p>
     * @param x The value that needs to be attached to the parent
     * @return the available parent
     */
    protected Node getParentOf(Comparable x) {
        Node parent = null;
        Node n = root;
        while(n != null) {
            parent = n;
            if (x.compareTo(n.getData()) < 0) { // x is smaller than n
                n = n.getLeft();
            } else if (x.compareTo(n.getData()) > 0) { // x is bigger than n
                n = n.getRight();
            } else // the value is the same, do not add the value
                throw new TreeException("The value is already in the tree. No Parent will be returned");
        }
        return parent;
    }

    /**
     * Deletes a specified element from the binary tree, if it exists.
     * The method searches for the node containing the specified value,
     * removes it, and adjusts the tree to maintain its properties.
     * <p>Idea inspired from ALgoDS Lecture HS Fulda 24/25</p>
     *
     * @param x the value to be deleted from the binary tree.
     * @throws TreeException if the tree is empty or the value to delete is not found.
     */
    public void delete(Comparable x) throws TreeException {
        Node target = root;
        Node parentTarget = null;
        if(target == null)
            throw new TreeException("The tree is empty");

        CHILD positionOfTarget = null;
        while(target != null) {
            if(x.compareTo(target.getData()) < 0) {
                parentTarget = target;
                target = parentTarget.getLeft();
                positionOfTarget = CHILD.leftChildOfParent;
            } else if(x.compareTo(target.getData()) > 0) {
                parentTarget = target;
                target = parentTarget.getRight();
                positionOfTarget = CHILD.rightChildOfParent;
            } else if(x.compareTo(target.getData()) == 0) {
                removeNullNode(deleteTarget(parentTarget, target, positionOfTarget));
                return;
            }
        }
        throw new TreeException("The value " + x + " could not be found in the tree.");
    }

    /**
     * Delete a node with one Child or none
     * <p>
     * Case 0 if the target is leaf, then it will take the children of the target for the parent.
     * since it child of the target is null, the children of the parent will be null. </p>
     * <p>
     * Case 1 if the target has only one child, the parentTarget will
     * take either the target's children (subtree) left or right</p>
     * Idea Inspired from AlgoDS Lecture HS Fulda 24/25
     *
     * @param parentTarget The parent node of the target node to be deleted.
     *                     If the target node is the root, this parameter is null.
     * @param target       The node to be deleted from the binary tree.
     * @param positionOfTarget Indicates whether the target node is the left or right
     *                         child of the parent node.
     * @return The replacement node that takes the place of the deleted node, or null
     *         if the target node had no children.
     */
    protected Node deleteTargetWithOneChildOrNone(Node parentTarget, Node target, CHILD positionOfTarget) {
        Node replacement = target.getRight() == null ? target.getLeft() : target.getRight();
        Node nullNode = new Node(null, Node.COLOR.BLACK);
        replacement = replacement == null ? nullNode : replacement;
        if(parentTarget != null) {
            // Case 0 and 1 from the Lecture
            switch (positionOfTarget) {
                case leftChildOfParent -> parentTarget.setLeft(replacement);
                case rightChildOfParent -> parentTarget.setRight(replacement);
            }
        } else {
            // If the target is root, then the replacement becomes the root
            root = replacement;
        }
        return replacement;
    }

    /**
     * Finds and removes the smallest node from the left subtree of the right child of the current node.
     * <p>
     * This specialized utility is primarily used in tree deletion operations, particularly
     * when replacing a node being removed (e.g., during the deletion of a node in a binary search tree).
     * The function identifies the smallest node in the left subtree of the right child,
     * removes it from the tree, and returns it.
     * </p>
     * <p>
     * The function assumes that the current node has a right child, and the right child has a left subtree
     * (or is a leaf node). If the right child does not have a left subtree, the right child itself
     * will replace the current node. If the right child is empty, the function will throw an exception, ensuring
     * that it is only called under appropriate conditions.
     * </p>
     * <p>Idea inspired from AlgoDS lecture HS Fulda 24/25</p>
     * @throws TreeException If the right child does not exist, an exception is thrown,
     *         as the function requires a valid right child to operate.
     */
    public static Node replaceWithTheSmallestOfRightChildren(Node target) throws TreeException {
        if (target.getRight() == null)
            throw new TreeException("This method should not be called because there are no right child nodes.");

        Node parent = target;
        Node result = target.getRight();

        // Traverse to the leftmost node in the right subtree
        while (result.getLeft() != null) {
            // if this lines being executed, that means, there are children on the left side.
            // result will take the left child,
            // therefore parent.getRight != result after the iteration
            parent = result;
            result = parent.getLeft();
        }

        // Detach the smallest node from its parent
        if (parent.getRight() == result) {
            // If the smallest node is directly the right child
            // Because there is no other child on the left side.
            parent.setRight(result.getRight());
        } else {
            // If the smallest node is further down the left subtree
            // then take the right subtree of the result. if it does not exist then
            // the parent.left should be null
            parent.setLeft(result.getRight());
        }

        target.setData(result.getData());
        return parent;
    }

    /**
     * Constructs a tree structure that mirrors the current binary tree
     * and returns the root of the corresponding TreePrinter object.
     * The TreePrinter object is useful for visually representing the
     * structure of the binary tree with the terminal as String format.
     *
     * @return the root of the TreePrinter representation of the binary tree
     * @throws NullPointerException if the tree is empty (root is null)
     */
    public TreePrinter getTreePrinter() {
        if (root == null) throw new NullPointerException("The tree is empty");
        ArrayList<Node> queueNodes = new ArrayList<>();
        ArrayList<TreePrinter> queueNode = new ArrayList<>();
        TreePrinter treePrinterRoot = new TreePrinter((int) root.getData(), null, null);
        TreePrinter iterNode = treePrinterRoot;

        queueNodes.add(root);
        while (!queueNodes.isEmpty()) {
            Node current = queueNodes.removeFirst();
            if (current.getLeft() != null) {
                int leftValue = (int) current.getLeft().getData();
                iterNode.setLeft(new TreePrinter(leftValue, null, null));
                queueNode.add(iterNode.getLeft());
                queueNodes.add(current.getLeft());
            }
            if (current.getRight() != null) {
                int rightValue = (int) current.getRight().getData();
                iterNode.setRight(new TreePrinter(rightValue, null, null));
                queueNode.add(iterNode.getRight());
                queueNodes.add(current.getRight());
            }
            if (!queueNode.isEmpty()) iterNode = queueNode.removeFirst(); // remove the first element, so that the next element is the next node
        }
        return treePrinterRoot;
    }

    /**
     * Based on right-right case.
     * inspired from: <a href="https://www.geeksforgeeks.org/insertion-in-an-avl-tree/">Geek For Geeks</a>
     *
     * @param z the root of rotation
     */
    protected void leftRotate(Node z) {
        Node y = z.getRight();
        Node T2 = y.getLeft();

        updateParent(z,y);

        z.setRight(T2);
        y.setLeft(z);
        if (root == z) root = y;
        updateHeightAfterRotation(z,y);
    }

    /**
     * Based on the lef-left case.
     * inspired from: <a href="https://www.geeksforgeeks.org/insertion-in-an-avl-tree/">Geek For Geeks</a>
     *
     * @param z the root of rotation
     */
    protected void rightRotate(Node z) {
        Node y = z.getLeft();
        Node T3 = y.getRight();

        updateParent(z,y);

        z.setLeft(T3);
        y.setRight(z);
        if (root == z) root = y;
        updateHeightAfterRotation(z,y);
    }

    /**
     * Updates the parent of the given node `z` to point to node `y` instead, if a parent exists.
     * If `z` is the left child of its parent, the parent's left child is updated to `y`.
     * If `z` is the right child of its parent, the parent's right child is updated to `y`.
     *
     * @param z the node whose parent relationship is being updated
     * @param y the node that will replace `z` as the child of `z`'s parent
     */
    private static void updateParent(Node z, Node y) {
        Node parentZ = z.getParent();
        if(parentZ != null) {
            if(parentZ.getLeft() == z) parentZ.setLeft(y);
            else parentZ.setRight(y);
        }
    }

    /**
     * Special function only for the rightRotate and leftRotate
     * function only!
     * @param z node
     * @param y node
     */
    private static void updateHeightAfterRotation(Node z, Node y) {
        z.setHeight(Math.max(height(z.getLeft()),
                height(z.getRight()) + 1));
        y.setHeight(Math.max(height(y.getLeft()),
                height(y.getRight())) + 1);
    }

    /**
     *  This function should only be used to calculate balance factor or updating height after insertion or deletion
     *  to reduce redundancy.
     *  </p>
     *  If return of the height is 0, it could mean two things. Either the node is the leaf or n is unavailable
     * @param n the target node
     * @return the height of n
     */
    static int height(Node n) {
        return n == null ? 0 : n.getHeight() ;
    }

    private void removeNullNode(Node node) {
        if(node != null && node.getParent() != null && node.getData() == null ) {
            if(node.getParent().getLeft() == node) node.getParent().setLeft(null);
            else node.getParent().setRight(null);
        }
    }

    /**
     * Generates a base DOT representation of the binary tree starting from the
     * given node.
     * The DOT format is used for visualizing graphs. In this case for visualizing
     * the binary tree.
     * This method will be extended by {@link BinaryTree#exportDOT(String)} to
     * generate a complete DOT representation.
     *
     * @param node     The current node in the binary tree.
     * @param writer  The StringBuilder used to accumulate the DOT representation.
     * @param depth    The current depth of the node in the tree.
     * @param depthMap A map that groups nodes by their depth for rank=same grouping
     *                 in DOT.
     */
    private void generateDOT(Node node, BufferedWriter writer, int depth, Map<Integer, List<String>> depthMap) throws IOException {
        if (node != null) {
            // Node color logic
            String fillColor;
            String fontColor;
            if(node.getColor() != null) {
                fillColor = node.getColor().equals(Node.COLOR.RED) ? "red" : "black";
                fontColor = node.getColor().equals(Node.COLOR.RED) ? "black" : "white";
            } else {
                fillColor = "green";
                fontColor = "white";
            }

            // Write the node representation
            writer.write(String.format("    \"%s\" [style=filled, fillcolor=%s, fontcolor=%s];\n",
                    node.getData().toString(), fillColor, fontColor));

            // Add non-NIL node to depthMap for rank=same grouping
            depthMap.computeIfAbsent(depth, k -> new ArrayList<>()).add(node.getData().toString());

            // Left child
            if (node.getLeft() != null) {
                writer.write(String.format("    \"%s\" -> \"%s\";\n",
                        node.getData().toString(), node.getLeft().getData().toString()));
                generateDOT(node.getLeft(), writer, depth + 1, depthMap);
            } else if (node.getRight() != null) {
                // Invisible edge to represent missing left child
                String nilId = "NIL_" + System.identityHashCode(node) + "_left";
                writer.write(String.format("    \"%s\" [shape=circle, style=invis, fillcolor=black, width=0.1, height=0.1, label=\"\"];\n", nilId));
                writer.write(String.format("    \"%s\" -> \"%s\" [style=invis];\n", node.getData().toString(), nilId));
            }

            // Right child
            if (node.getRight() != null) {
                writer.write(String.format("    \"%s\" -> \"%s\";\n",
                        node.getData().toString(), node.getRight().getData().toString()));
                generateDOT(node.getRight(), writer, depth + 1, depthMap);
            } else if (node.getLeft() != null) {
                // Invisible edge to represent missing right child
                String nilId = "NIL_" + System.identityHashCode(node) + "_right";
                writer.write(String.format("    \"%s\" [shape=circle, style=invis, fillcolor=black, width=0.1, height=0.1, label=\"\"];\n", nilId));
                writer.write(String.format("    \"%s\" -> \"%s\" [style=invis];\n", node.getData().toString(), nilId));
            }
        }
    }

    /**
     * <p>
     * Exports the binary tree structure in DOT format as a file.
     * The DOT format is used for representing graphs and can be visualized using
     * tools like Graphviz.
     * </p>
     * The method generates DOT representation for the nodes and records their depth
     * levels.
     * It also ensures that nodes at the same depth level are ranked the same in the
     * DOT output.
     *
     */
    public void exportDOT(String filename) throws IOException {
        Path filePath = Paths.get(filename);
        try {
            // Ensure parent directories exist
            if(filePath.getParent() != null)
                // only create directories if the path is within the String filename
                Files.createDirectories(filePath.getParent());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                Map<Integer, List<String>> depthMap = new HashMap<>();
                writer.write("digraph Tree {\n");
                writer.write("    node [shape=circle];\n");

                // Generate DOT for nodes and record depth levels
                generateDOT(root, writer, 0, depthMap);

                // Add rank=same for nodes at the same depth
                for (Map.Entry<Integer, List<String>> entry : depthMap.entrySet()) {
                    writer.write("    { rank=same; ");
                    for (String nodeId : entry.getValue()) {
                        writer.write(String.format("\"%s\" ", nodeId));
                    }
                    writer.write("}\n");
                }

                writer.write("}\n");
                writer.flush();
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }





}


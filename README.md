# Binary_Tree

## Introduction

Welcome to the **Binary_Tree** project! This project is designed to provide a foundational implementation for various
types of binary trees, such as AVL and Red-Black Trees (RBT). The main goal is to ensure that the differences in tree
functionality are contained within their respective implementations while utilizing a shared structure for common
behavior.

### Overview

This project is implemented in Java and utilizes an abstract class `BinaryTree` to define the
shared functionality across different tree types. The specific tree types like AVL and RBT inherit from this abstract
class, allowing them to focus on their distinctive implementations.

Each tree-related class is organized inside the **services** directory for better modularity and separation of concerns.
For examples of how to use and implement these tree structures, you can refer to the test files available in the
project.

### Key Features

- **Abstract `BinaryTree` Class**: Provides a shared structure for all tree implementations.
- **Focus on Tree Differences**: Each tree type (e.g., AVL, RBT) encapsulates specific algorithms and differentiating
  features.
- **Modular Design**: Classes for binary trees are located in the **services** directory for easy navigation and
  scalability.
- **Comprehensive Tests**: Test files demonstrate tree functionality and use cases.

### Prerequisites

Ensure you have the following installed on your system before running the project:

- Java 21+
- IntelliJ IDEA (Recommended for better execution)

### Literature Overview

Here is a brief overview of the concepts and algorithms behind the foundational tree structures implemented in this
project:

- **Binary Search Tree (BST)**:
  A Binary Search Tree is a node-based binary tree data structure where each node has at most two children. For every
  node, the left child contains only values less than the node, and the right child contains only values greater than
  the node. BSTs are useful for quick lookups, insertions, and deletions, with an average time complexity of O(log n)
  (in case of a balanced tree). However, BSTs can become unbalanced, degrading time complexity to O(n) in the worst
  case.
- **AVL Tree**:
  The AVL (Adelson-Velsky and Landis) Tree is a self-balancing Binary Search Tree. It maintains its balance by ensuring
  the height difference (balance factor) between the left and right subtrees of any node is at most 1. If the balance
  factor exceeds this threshold after insertion or deletion, the tree performs rotations (single or double) to restore
  balance. AVL Trees guarantee O(log n) time complexity for lookups, insertions, and deletions, making them a reliable
  choice for dynamic datasets.
- **Red-Black Tree (RBT)**:
  A Red-Black Tree is another type of self-balancing Binary Search Tree, which enforces additional properties to ensure
  its height never exceeds 2*log(n). These properties include:
    - Every node is either red or black.
    - The root is always black.
    - Red nodes cannot have red children (no consecutive red nodes).
    - Every path from a node to its descendant NULL nodes must have the same number of black nodes.
      
  These rules allow Red-Black Trees to maintain balanced heights while being less rigid than AVL Trees. Insertions
  and deletions may trigger re-coloring and rotations to preserve these properties, ensuring O(log n) time complexity
  for operations. Each of these structures has its unique strengths and use cases. 
  While AVL Trees are often used in scenarios demanding
  strict balancing for frequent data access, Red-Black Trees are more efficient for scenarios with heavy insertion and
  deletion, such as in database implementations and language libraries (e.g., Java's `TreeMap`).

## References

- **AVL Tree**: Implemented based on the approach described
  in [Geeks for Geeks](https://www.geeksforgeeks.org/avl-tree-set-1-insertion/) for AVL trees, including the rotation
  techniques for maintaining tree balance.
    - WARNING! The implementation of the tree in this project is **ITERATIVE**. The website Geeks for Geeks uses a
      recursive approach, which is different from the approach in this project.
      The website is used only for conceptual inspiration, especially for the rotation after each deletion and
      insertion.
- **Red-Black Tree**: Developed following the pseudocode provided in the book *Introduction to Algorithms* (
  CLRS). [More details about CLRS](https://mitpress.mit.edu/9780262046305/introduction-to-algorithms/).
- **Foundational Binary Tree Operations**: Inspired by the content covered in the *Lecture: Algorithm and Data
  Structures* (Winter Semester 24/25) from Hochschule Fulda, Germany.
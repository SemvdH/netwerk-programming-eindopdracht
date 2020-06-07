package netwerkprog.game.util.tree;

import java.util.ArrayList;

public class BST<E extends Comparable<E>> extends AbstractTree<E> {
    protected TreeNode<E> root;
    protected int size = 0;


    public int sum() {
        return this.sum(this.getRoot());
    }

    public int sum(TreeNode<E> node) {

        if (node == null) {
            return 0;
        }

        int nodeValue = (Integer) node.element;
        return sum(node.left) + sum(node.right);
    }


    public int totalLeaves() {
        return this.totalLeaves(this.getRoot());
    }

    public int totalLeaves(TreeNode<E> node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return totalLeaves(node.left) + totalLeaves(node.right);
    }

    /**
     * Create a default binary tree
     */
    public BST() {
    }

    /**
     * Create a binary tree from an array of objects
     */
    public BST(E[] objects) {
        for (E object : objects) insert(object);
    }

    /**
     * Returns true if the element is in the tree
     */
    @Override
    public boolean search(E e) {
        return search(e, root);
    }

    private boolean search(E e, TreeNode<E> tree) {
        // nog niet correct
        if (tree == null) {
            return false;
        }
        if (e.compareTo(tree.element) == 0) {
            return true;
        }
        if (e.compareTo(tree.element) < 0) {
            return search(e, tree.left);
        } else // (e.compareTo(tree.element) > 0)
        {
            return search(e, tree.right);
        }
    }

    /**
     * Insert element o into the binary tree
     * Return true if the element is inserted successfully
     */
    @Override
    public void insert(E e) {
        if (root == null) {
            root = createNewNode(e); // Create a new root
            size++;
        } else {
            insert(e, root);
        }

    }

    /**
     * Insert element o into the binary tree
     * Return true if the element is inserted successfully
     * pre: root != null
     */
    public boolean insert(E e, TreeNode<E> tree) {
        if (e.compareTo(tree.element) == 0) {
            return false;  // Duplicate node not inserted
        } else if (e.compareTo(tree.element) < 0 && tree.left != null)
            return insert(e, tree.left);

        else if (e.compareTo(tree.element) > 0 && tree.right != null)
            return insert(e, tree.right);

            // Create the new node and attach it to the parent node
        else {
            if (e.compareTo(tree.element) < 0) {
                tree.left = createNewNode(e);
            } else {
                tree.right = createNewNode(e);
            }
            size++;
            return true;
        }
    }


    protected TreeNode<E> createNewNode(E e) {
        return new TreeNode<>(e);
    }

    /**
     * Inorder traversal from the root
     */
    @Override
    public void inorder() {
        inorder(root);
    }

    /**
     * Inorder traversal from a subtree
     */
    protected void inorder(TreeNode<E> root) {
        if (root == null) return;
        inorder(root.left);
        System.out.print(root.element + " ");
        inorder(root.right);
    }

    /**
     * Post order traversal from the root
     */
    @Override
    public void postOrder() {
        postorder(root);
    }

    /**
     * Post order traversal from a subtree
     */
    protected void postorder(TreeNode<E> root) {
        if (root == null) return;
        postorder(root.left);
        postorder(root.right);
        System.out.print(root.element + " ");
    }

    /**
     * Preorder traversal from the root
     */
    @Override
    public void preorder() {
        preorder(root);
    }

    /**
     * Preorder traversal from a subtree
     */
    protected void preorder(TreeNode<E> root) {
        if (root == null) return;
        System.out.print(root.element + " ");
        preorder(root.left);
        preorder(root.right);
    }

    /**
     * This inner class is static, because it does not access
     * any instance members defined in its outer class
     */
    public static class TreeNode<E extends Comparable<E>> {
        protected E element;
        protected TreeNode<E> left;
        protected TreeNode<E> right;

        public TreeNode(E e) {
            element = e;
        }
    }

    /**
     * Get the number of nodes in the tree
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Returns the root of the tree
     */
    public TreeNode<E> getRoot() {
        return root;
    }

    /**
     * Returns a path from the root leading to the specified element
     */
    public ArrayList<TreeNode<E>> path(E e) {
        ArrayList<TreeNode<E>> list =
                new ArrayList<>();
        TreeNode<E> current = root; // Start from the root

        while (current != null) {
            list.add(current); // Add the node to the list
            if (e.compareTo(current.element) < 0) {
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                current = current.right;
            } else
                break;
        }

        return list; // Return an array list of nodes
    }

    /**
     * Delete an element from the binary tree.
     * Return true if the element is deleted successfully
     * Return false if the element is not in the tree
     */
    @Override
    public void delete(E e) {
        // Locate the node to be deleted and also locate its parent node
        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                parent = current;
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                parent = current;
                current = current.right;
            } else
                break; // Element is in the tree pointed at by current
        }

        if (current == null)
            return; // Element is not in the tree

        // Case 1: current has no left child
        if (current.left == null) {
            // Connect the parent with the right child of the current node
            if (parent == null) {
                root = current.right;
            } else {
                if (e.compareTo(parent.element) < 0)
                    parent.left = current.right;
                else
                    parent.right = current.right;
            }
        } else {
            // Case 2: The current node has a left child
            // Locate the rightmost node in the left subtree of
            // the current node and also its parent
            TreeNode<E> parentOfRightMost = current;
            TreeNode<E> rightMost = current.left;

            while (rightMost.right != null) {
                parentOfRightMost = rightMost;
                rightMost = rightMost.right; // Keep going to the right
            }

            // Replace the element in current by the element in rightMost
            current.element = rightMost.element;

            // Eliminate rightmost node
            if (parentOfRightMost.right == rightMost)
                parentOfRightMost.right = rightMost.left;
            else
                // Special case: parentOfRightMost == current
                parentOfRightMost.left = rightMost.left;
        }

        size--;
    }

    /**
     * Obtain an iterator. Use inorder.
     */
    @Override
    public java.util.Iterator<E> iterator() {
        return new InorderIterator();
    }

    // Inner class InorderIterator
    private class InorderIterator implements java.util.Iterator<E> {
        // Store the elements in a list
        private final java.util.ArrayList<E> list =
                new java.util.ArrayList<>();
        private int current = 0; // Point to the current element in list

        public InorderIterator() {
            inorder(); // Traverse binary tree and store elements in list
        }

        /**
         * Inorder traversal from the root
         */
        private void inorder() {
            inorder(root);
        }

        /**
         * Inorder traversal from a subtree
         */
        private void inorder(TreeNode<E> root) {
            if (root == null) return;
            inorder(root.left);
            list.add(root.element);
            inorder(root.right);
        }

        /**
         * More elements for traversing?
         */
        @Override
        public boolean hasNext() {
            return current < list.size();
        }

        /**
         * Get the current element and move to the next
         */
        @Override
        public E next() {
            return list.get(current++);
        }

        /**
         * Remove the current element
         */
        @Override
        public void remove() {
            delete(list.get(current)); // Delete the current element
            list.clear(); // Clear the list
            inorder(); // Rebuild the list
        }
    }

    /**
     * Remove all elements from the tree
     */
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (E e : this) {
            res.append(e.toString());
        }
        return "BST{" +
                "root=" + root +
                ", size=" + size +
                ", " + res +
                '}';
    }
}

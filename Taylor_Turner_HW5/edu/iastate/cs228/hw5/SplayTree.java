package edu.iastate.cs228.hw5;


import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 *
 * @author Yuxiang Zhang
 *
 */

/**
 *
 * This class implements a splay tree. Add any helper methods or implementation details you'd like to include.
 *
 */
public class SplayTree<E extends Comparable<? super E>> extends AbstractSet<E> {

	protected Node root;

	public int size;

	public SplayTree() {
		size = 0;
	}

	/**
	 * Needs to call addBST() later on to complete tree construction.
	 */
	public SplayTree(E data) {
		root = new Node(data);
		size = 1;
	}

	/**
	 * Copies over an existing splay tree. Deep copying must be done. No splaying.
	 *
	 * @param tree
	 */
	public SplayTree(SplayTree<E> tree) {
		root = cloneTreeRec(tree.root);
		size = tree.size;
	}

	public Node cloneTreeRec(Node subTree) {
		if (subTree == null)
			return null;
		Node clone = subTree.clone();
		clone.left = cloneTreeRec(subTree.left);
		if (clone.left != null)
			clone.left.parent = clone;
		clone.right = cloneTreeRec(subTree.right);
		if (clone.right != null)
			clone.right.parent = clone;
		return clone;
	}

	/**
	 * This function is here for grading purpose. It is not a good programming practice. This method is fully implemented and should not be modified.
	 *
	 * @return root of the splay tree
	 */
	public E getRoot() {
		return root == null ? null : root.data;
	}

	public Node getRootNode() {
		return this.root;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		root = null;
		size = 0;
	}

	// ----------
	// BST method
	// ----------

	/**
	 * Adds an element to the tree without splaying. <br>
	 * The method carries out a binary search tree addition. <br>
	 * It is used for initialize a splay tree.
	 *
	 * @param data
	 * @return
	 */
	public boolean addBST(E data) {
		Node cur = root, pre = null;
		while (cur != null) {
			pre = cur;
			if (data.compareTo(cur.data) == 0)
				return false;
			else if (data.compareTo(cur.data) < 0)
				cur = cur.left;
			else
				cur = cur.right;
		}
		Node n = new Node(data);
		link(pre, n);
		size++;
		return true;
	}

	/**
	 * Inserts an element into the splay tree. <br>
	 * In case the element was not contained, this creates a new node and splays the tree at the new node. <br>
	 * If the element exists in the tree already, it splays at the node containing the element.
	 *
	 * @param data
	 *            element to be inserted
	 * @return true if addition takes place false otherwise (i.e., data is in the tree already)
	 */
	@Override
	public boolean add(E data) {
		Node e = findEntry(data);
		if (e != null && data.compareTo(e.data) == 0) {
			splay(e);
			return false;
		}
		Node n = new Node(data);
		link(e, n);
		size++;
		splay(n);
		return true;
	}

	// ------------------
	// Splay tree methods
	// ------------------

	/**
	 * Determines whether the tree contains an element. <br>
	 * Splays at the Node that stores the element. <br>
	 * If the element is not found, splays at the last Node on the search path.
	 *
	 * @param data
	 *            element to be determined whether to exist in the tree
	 * @return true if the element is contained in the tree and false otherwise
	 */
	public boolean contains(E data) {
		Node entry = findEntry(data);
		if (entry == null)
			return false;
		splay(entry);
		return entry.data.compareTo(data) == 0;
	}

	/**
	 * Removes the Node that stores an element. <br>
	 * Splays at its parent Node after removal. (No splay if the removed Node was the root) <br>
	 * If the Node was not found, the last Node encountered on the search path is splayed to the root.
	 *
	 * @param data
	 *            element to be removed from the tree
	 * @return true if the object is removed false if it was not contained in the tree
	 */
	public boolean remove(E data) {
		if (size == 0)
			return false;
		Node n = findEntry(data);
		if (n.data.compareTo(data) == 0) {
			unlink(n);
			size--;
			if (n.parent != null)
				splay(n.parent);
			return true;
		}
		splay(n);
		return false;
	}

	/**
	 * Finds the node that stores the data and splays at it.
	 *
	 * @param data
	 */
	public void splay(E data) {
		contains(data);
	}

	public E findElement(E data) {
		Node entry = findEntry(data);
		splay(entry);
		return entry == null || entry.data.compareTo(data) != 0 ? null : entry.data;
	}

	/**
	 * Finds the node that stores an item. It is called by contains(), add(), and splay().
	 *
	 * @param data
	 *            item to be searched
	 * @return node if found or the last node on the search path otherwise null if size == 0.
	 */
	public Node findEntry(E data) {
		Node cur = root, pre = null;
		while (cur != null) {
			pre = cur;
			if (data.compareTo(cur.data) == 0)
				return cur;
			else if (data.compareTo(cur.data) < 0)
				cur = cur.left;
			else
				cur = cur.right;
		}
		return pre;
	}

	/**
	 * Join the two subtrees T1 and T2 rooted at root1 and root2 into one. It is called by remove().
	 *
	 * Precondition: All datas in T1 are less than those in T2.
	 *
	 * Access the largest data in T1, and splay at the Node to make it the root of T1. <br>
	 * Make T2 the right subtree of T1. The method is called by remove().
	 *
	 * @param root1
	 *            root of the subtree T1
	 * @param root2
	 *            root of the subtree T2
	 * @return the root of the joined subtree
	 */
	public Node join(Node root1, Node root2) {
		if (root1 == null)
			return root2;
		Node max = root1;
		while (max.right != null)
			max = max.right;
		splay(max);
		max.right = root2;
		if (root2 != null)
			root2.parent = max;
		return max;
	}

	/**
	 * Splay at the current node. <br>
	 * This consists of a sequence of zig, zigZig, or zigZag operations until the current Node is moved to the root of the tree.
	 *
	 * @param current
	 *            Node to splay
	 */
	protected void splay(Node current) {
		if (current == null)
			return;
		while (current.parent != null) {
			if (current.parent.parent == null)
				zig(current);
			else if (current.parent.left == current && current.parent.parent.left == current.parent
					|| current.parent.right == current && current.parent.parent.right == current.parent)
				zigZig(current);
			else
				zigZag(current);
		}
	}

	/**
	 * This method performs the zig operation on a node. Calls leftRotate() or rightRotate().
	 *
	 * @param current
	 *            Node to perform the zig operation on
	 */
	protected void zig(Node current) {
		if (current == null || current.parent == null)
			throw new IllegalStateException();
		else if (current.parent.right == current)
			leftRotate(current);
		else
			rightRotate(current);
	}

	/**
	 * This method performs the zig-zig operation on a node. Calls leftRotate() or rightRotate().
	 *
	 * @param current
	 *            Node to perform the zig-zig operation on
	 */
	protected void zigZig(Node current) {
		if (current == null || current.parent == null || current.parent.parent == null)
			throw new IllegalStateException();
		// current is a left child, current.parent is also a left child
		else if (current.parent.left == current && current.parent.parent.left == current.parent) {
			rightRotate(current.parent);
			rightRotate(current);
			return;
		}
		// current is a right child, current.parent is also a right child
		else if (current.parent.right == current && current.parent.parent.right == current.parent) {
			leftRotate(current.parent);
			leftRotate(current);
			return;
		}
		// cannot zig-zig
		else
			throw new IllegalStateException();
	}

	/**
	 * This method performs the zig-zag operation on a node. Calls leftRotate() or rightRotate() or both.
	 *
	 * @param current
	 *            Node to perform the zig-zag operation on
	 */
	protected void zigZag(Node current) {
		if (current == null || current.parent == null || current.parent.parent == null)
			throw new IllegalStateException();
		// current is a left child, current.parent is a right child
		else if (current.parent.left == current && current.parent.parent.right == current.parent) {
			rightRotate(current);
			leftRotate(current);
			return;
		}
		// current is a right child, current.parent is a left child
		else if (current.parent.right == current && current.parent.parent.left == current.parent) {
			leftRotate(current);
			rightRotate(current);
			return;
		}
	}

	/**
	 * Carries out a left rotation at a node such that after the rotation its former parent becomes its left child.
	 *
	 * @param current
	 */
	public void leftRotate(Node current) {
		if (current == null || current.parent == null || current == current.parent.left)
			throw new IllegalStateException();

		Node parent = current.parent;

		if (parent == root)
			root = current;

		link(parent.parent, current);

		parent.right = current.left;
		if (current.left != null)
			current.left.parent = parent;

		current.left = parent;
		parent.parent = current;
	}

	/**
	 * Carries out a right rotation at a node such that after the rotation its former parent becomes its right child.
	 *
	 * @param current
	 */
	public void rightRotate(Node current) {
		if (current == null || current.parent == null || current == current.parent.right)
			throw new IllegalStateException();

		Node parent = current.parent;

		if (parent == root)
			root = current;

		link(parent.parent, current);

		parent.left = current.right;
		if (current.right != null)
			current.right.parent = parent;

		current.right = parent;
		parent.parent = current;
	}

	private void link(Node parent, Node child) {
		if (child == null)
			return;
		child.parent = parent;

		if (parent == null) {
			if (root == null)
				root = child;
			return;
		}

		if (child.data.compareTo(parent.data) == 0)
			return;
		if (child.data.compareTo(parent.data) < 0)
			parent.left = child;
		else
			parent.right = child;
	}

	private void unlink(Node n) {
		if (n == null)
			return;

		// when n is leaf
		if (n.left == null && n.right == null) {
			if (n == root)
				root = null;
			else if (n.parent.left == n)
				n.parent.left = null;
			else if (n.parent.right == n)
				n.parent.right = null;
			return;
		}

		// make n unaccessible from its children
		if (n.left != null)
			n.left.parent = null;
		if (n.right != null)
			n.right.parent = null;

		// join subtrees of n
		if (n == root)
			root = join(n.left, n.right);
		else
			link(n.parent, join(n.left, n.right));
	}

	public void unlinkBST(Node n) {
		if (n.left != null && n.right != null) {
			Node s = successor(n);
			n.data = s.data;
			n = s;
		}

		// n has at most one child
		Node replacement = null;
		if (n.left != null)
			replacement = n.left;
		else if (n.right != null)
			replacement = n.right;

		// link replacement into tree in place of node n (replacement may be null)
		if (n.parent == null)
			root = replacement;
		else if (n == n.parent.left)
			n.parent.left = replacement;
		else
			n.parent.right = replacement;
		if (replacement != null)
			replacement.parent = n.parent;
	}

	public Node successor(Node n) {
		if (n == null)
			return null;
		if (n.right == null) {
			while (n != root && n.parent.right == n)
				n = n.parent;
			if (n == root)
				n = null;
			else
				n = n.parent;
		} else {
			n = n.right;
			while (n.left != null)
				n = n.left;
		}
		return n;
	}

	@Override
	public Iterator<E> iterator() {
		return new SplayTreeIterator();
	}

	/**
	 * Write the splay tree according to the format specified below (also read Section 2.2 of the project description).
	 *
	 * 1. Every Node of the tree occupies a separate line with its data written only.
	 *
	 * 2. The data stored at a Node at depth d is printed with indentation 4d (i.e., preceded by 4d blanks).
	 *
	 * 3. Start with the root (at depth 0) and display the nodes in a preorder traversal. More specifically, suppose a Node n is shown at line l.
	 * Then, starting at line l+1,
	 *
	 * 3a) recursively print all nodes in the left subtree (if any) of n;
	 *
	 * 3b) recursively print all nodes in the right subtree (if any) of n.
	 *
	 * 4. If a Node has a left child but no right child, print its right child as null.
	 *
	 * 5. If a Node has a right child but no left child, print its left child as null.
	 *
	 * 6. If a Node is a leaf, print it with no further recursion.
	 *
	 * Use an iterator.
	 *
	 */
	@Override
	public String toString() {
		return toStringRec(root, 0);
	}

	private String toStringRec(Node n, int depth) {
		String s = "";
		for (int i = 0; i < depth; i++)
			s += "    ";
		if (n == null)
			return s + n + "\n";
		s += n.data + "\n";
		if (n.left == null && n.right == null)
			return s;
		s += toStringRec(n.left, depth + 1);
		s += toStringRec(n.right, depth + 1);
		return s;
	}

	public class Node {
		public E data;
		public Node left;
		public Node parent;
		public Node right;

		public Node(E data) {
			this.data = data;
		}

		@Override
		public Node clone() {
			return new Node(data);
		}
	}

	/**
	 *
	 * Iterator implementation for this splay tree. <br>
	 * The elements are returned in ascending order according to their natural ordering. <br>
	 * All iterator methods are exactly the same as those for a binary search tree --- no splaying at any Node as the cursor moves.
	 *
	 */
	private class SplayTreeIterator implements Iterator<E> {
		Node cursor, pending;

		public SplayTreeIterator() {
			cursor = root;
			while (cursor != null && cursor.left != null)
				cursor = cursor.left;
		}

		@Override
		public boolean hasNext() {
			return cursor != null;
		}

		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			pending = cursor;
			cursor = successor(cursor);
			return pending.data;
		}

		@Override
		public void remove() {
			if (pending == null)
				throw new IllegalStateException();
			unlinkBST(pending);
			size--;
			pending = null;
		}
	}
}


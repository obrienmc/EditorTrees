package editortrees;

// A height-balanced binary tree with rank that could be the basis for a text editor.

public class EditTree {

	public Node root;
	private int size;
	int totalRotationCount;

	/**
	 * Construct an empty tree
	 */
	public EditTree() {
		this.root = Node.NULL_NODE;
		this.size = 0;
		this.totalRotationCount = 0;
	}

	/**
	 * Construct a single-node tree whose element is c
	 * 
	 * @param c
	 */
	public EditTree(char c) {
		this.root = new Node(c);
		this.size = 1;
		this.totalRotationCount = 0;
	}

	/**
	 * Create an EditTree whose toString is s. This can be done in O(N) time,
	 * where N is the length of the tree (repeatedly calling insert() would be
	 * O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {
		this.root = new Node('x');
		this.root.buildStringTree(s);
		this.size = this.root.size;
		this.totalRotationCount = 0;
	}

	/**
	 * Make this tree be a copy of e, with all new nodes, but the same shape and
	 * contents.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {
		if (e.root != Node.NULL_NODE) {
			this.root = new Node();
			this.root.editTreeCopy(e.root);
			this.size = e.size;
			this.totalRotationCount = e.totalRotationCount;
		} else {
			this.root = Node.NULL_NODE;
			this.size = 0;
			this.totalRotationCount = 0;
		}

	}

	/**
	 * 
	 * @return the height of this tree
	 */
	public int height() {
		return this.root.height; // replace by a real calculation.
	}

	/**
	 * 
	 * returns the total number of rotations done in this tree since it was
	 * created. A double rotation counts as two.
	 * 
	 * @return number of rotations since tree was created.
	 */
	public int totalRotationCount() {
		return this.totalRotationCount;
	}

	/**
	 * return the string produced by an inorder traversal of this tree
	 */
	@Override
	public String toString() {
		if (this.root == Node.NULL_NODE) {
			return "";
		}
		return this.root.toString();
	}

	/**
	 * 
	 * @param pos
	 *            position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		if (pos < 0 || pos >= this.size || this.root == Node.NULL_NODE) {
			throw new IndexOutOfBoundsException();
		}
		return this.root.get(pos).element;
	}

	/**
	 * 
	 * @param c
	 *            character to add to the end of this tree.
	 */
	public void add(char c) {
		if (this.root == Node.NULL_NODE) {
			this.root = new Node(c);
			this.size++;
		} else {
			this.add(c, this.size);
		}
	}

	/**
	 * 
	 * @param c
	 *            character to add
	 * @param pos
	 *            character added in this inorder position
	 * @throws IndexOutOfBoundsException
	 *             id pos is negative or too large for this tree
	 */
	public void add(char c, int pos) throws IndexOutOfBoundsException {
		if (pos < 0 || pos > this.size) {
			throw new IndexOutOfBoundsException();
		}
		if (this.root == Node.NULL_NODE) {
			this.root = new Node(c);
		} else {
			this.root.add(c, pos, this);
		}
		this.size++;
	}

	/**
	 * 
	 * @return the number of nodes in this tree
	 */
	public int size() {
		return this.size;
	}

	/**
	 * 
	 * @param pos
	 *            position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		if (pos < 0 || pos >= this.size || this.root == Node.NULL_NODE) {
			throw new IndexOutOfBoundsException();
		}
		if (this.size == 1) {
			char temp = this.root.element;
			this.root = Node.NULL_NODE;
			this.size = 0;
			return temp;
		}
		return this.root.get(pos).delete(this);
	}

	/**
	 * This method operates in O(length*log N), where N is the size of this
	 * tree.
	 * 
	 * @param pos
	 *            location of the beginning of the string to retrieve
	 * @param length
	 *            length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException
	 *             unless both pos and pos+length-1 are legitimate indexes
	 *             within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		if (pos < 0 || (pos + length) > this.size) {
			throw new IndexOutOfBoundsException("The index is out of bounds!");
		}
		Node start = this.root.get(pos);
		return start.getString(length);
	}

	/**
	 * Append (in time proportional to the log of the size of the larger tree)
	 * the contents of the other tree to this one. Other should be made empty
	 * after this operation.
	 * 
	 * @param other
	 * @throws IllegalArgumentException
	 *             if this == other
	 */
	public void concatenate(EditTree other) throws IllegalArgumentException {

		if (this == other) {
			throw new IllegalArgumentException("YOU CAN'T DO THAT!");
		}
		if (other.root == Node.NULL_NODE) {
			// do nothing
		}
		// If the original tree was empty it is just replaced with the other
		// tree.
		else if (this.root == Node.NULL_NODE) {
			this.root = new Node();
			this.root = other.root;
			this.size = other.size;
			other.size = 0;
			other.root = Node.NULL_NODE;
		}
		// If the trees are both of size 1 then the new tree is just the old
		// with the root's right the old root of the other tree.
		else if (this.size == 1 && other.size == 1) {
			this.root.right = other.root;
			this.root.right.parent = this.root;
			this.root.size++;
			this.size++;
			other.size = 0;
			other.root = Node.NULL_NODE;
		}
		// Traverses down to the far left leaf of the tree being concatenated to
		// the old tree and calls the Node concatenate method at that node.
		else {
			Node current = other.root;
			// Finds the far left leaf and adjusts sizes and ranks accordingly.
			while (current.left != Node.NULL_NODE) {
				current.rank--;
				current.size--;
				current = current.left;
			}
			this.root.concatenate(other.root, current);
			if (this.root.parent != Node.NULL_NODE) {
				this.root = this.root.parent;
			}
			// Fixes the trees' sizes and empties the other tree.
			this.size = this.size + other.size;
			other.size = 0;
			other.root = Node.NULL_NODE;
		}
	}


	/**
	 * Don't worry if you can't do this one efficiently.
	 * 
	 * @param s
	 *            the string to look for
	 * @return the position in this tree of the first occurrence of s; -1 if s
	 *         does not occur
	 */
	public int find(String s) {
		if (!this.toString().contains(s)) {
			return -1;
		}
		return this.toString().indexOf(s);
	}

	/**
	 * 
	 * @param s
	 *            the string to search for
	 * @param pos
	 *            the position in the tree to begin the search
	 * @return the position in this tree of the first occurrence of s that does
	 *         not occur before position pos; -1 if s does not occur
	 */
	public int find(String s, int pos) {
		String current = this.toString().substring(pos);
		if (!current.contains(s)) {
			return -1;
		}
		return current.indexOf(s) + pos;
	}

	/**
	 * @return The root of this tree.
	 */
	public Node getRoot() {
		return this.root;
	}

	/**
	 * Sets the tree size.
	 */
	public void setTreeSize() {
		this.size = this.root.left.size + this.root.right.size + 1;
	}

}

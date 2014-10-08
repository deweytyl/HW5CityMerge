public class PriorityQueue extends PQ {

	/**
	 * Constructs a PriorityQueue of maximum size k
	 * 
	 * @param k
	 */
	public PriorityQueue(int k) {
		super(k);
	}

	/*
	 * Abstract class implementation
	 */

	/**
	 * Insert an item into the queue
	 * 
	 * @pre the queue maintains the heap property
	 * @post the queue maintains the heap property
	 */
	@Override
	public void insert(PQItem item) {
		// place item at end (this.currentSize)
		this.heapArray[this.currentSize] = item;

		this.swapUp(this.currentSize);

		// increase current size
		this.currentSize++;
	}

	/**
	 * Remove the smallest item from the queue
	 * 
	 * @pre the queue maintains the heap property
	 * @post the queue maintains the heap property
	 */
	@Override
	public PQItem remove() {
		// remove the first item
		PQItem oldItem = this.heapArray[0];

		// replace it with the last
		this.heapArray[0] = this.heapArray[this.currentSize - 1];
		this.heapArray[this.currentSize - 1] = null;

		// decrease heap size
		this.currentSize--;

		// go down the tree, replacing top with max between parent and children
		this.swapDown(0);

		return oldItem;
	}

	/*
	 * Utility methods
	 */

	/**
	 * Establish the heap property from a node to the root
	 * 
	 * @param i
	 * @pre i < currentSize
	 * @post the value of the node at i will be of lower priority than its
	 *       parents
	 */
	protected void swapUp(int i) {
		int parentIndex = parent(i);
		PQItem current = this.heapArray[i];
		PQItem parent = this.heapArray[parentIndex];

		if (parent.compareTo(current) > 0) {
			this.heapArray[parentIndex] = current;
			this.heapArray[i] = parent;

			swapUp(parentIndex);
		}
	}

	/**
	 * Establish the heap property moving from a parent to the leaves of the
	 * tree
	 * 
	 * @param parent
	 * @pre parent < currentSize
	 * @post the value of the node at parent will be of higher priority than its
	 *       children
	 */
	protected void swapDown(int parent) {
		int l = leftChild(parent);
		int r = rightChild(parent);
		int smallest;

		if (l < this.currentSize
				&& heapArray[l].compareTo(heapArray[parent]) < 0) {
			smallest = l;
		} else {
			smallest = parent;
		}

		if (r < this.currentSize
				&& heapArray[r].compareTo(heapArray[smallest]) < 0) {
			smallest = r;
		}

		if (smallest != parent) {
			this.swap(parent, smallest);
			swapDown(smallest);
		}
	} // heapifyDown(int)

	/**
	 * Swap the positions of two PQItems in the heap
	 * 
	 * @param i
	 *            the index of an item on the heap
	 * @param j
	 *            the index of an item on the heap
	 * @pre i < currentSize && j < currentSize
	 */
	protected void swap(int i, int j) {
		PQItem temp = this.heapArray[i];

		this.heapArray[i] = this.heapArray[j];
		this.heapArray[j] = temp;
	} // swap(int, int)

	/**
	 * Calculate the index of a given node's parent
	 * 
	 * @param child
	 * @return the index of that node's parent
	 */
	static int parent(int child) {
		return child / 2;
	} // parent(int)

	/**
	 * calculate the index of a given node's left child
	 * 
	 * @param parent
	 * @return the index of that node's left child
	 */
	static int leftChild(int parent) {
		return 2 * parent;
	}

	/**
	 * calculate the index of a given node's right child
	 * 
	 * @param parent
	 * @return the index of that node's right child
	 */
	static int rightChild(int parent) {
		return (2 * parent) + 1;
	}

}

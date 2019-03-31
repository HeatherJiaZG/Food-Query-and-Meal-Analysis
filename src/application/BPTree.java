/**
 * Filename: BPTree.java 
 * 
 * Project: p5 
 * 
 * Course: cs400 
 * 
 * Authors: Alex Yang lyang298@wisc.edu 
 * Libin Zhou lzhou228@wisc.edu
 * Yao Yao yyao69@wisc.edu
 * York Li yli875@wisc.edu
 * Heather Jia yjia42@wisc.edu
 * 
 * Due Date: 12/12/2018
 *
 * Additional credits: N/A
 *
 * Bugs or other notes: no known bugs
 */

package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to many different
 * indexes of a large data set. BPTree objects are created for each type of
 * index needed by the program. BPTrees provide an efficient range search as
 * compared to other types of data structures due to the ability to perform
 * log_m N lookups and linear in-order traversals of the data items.
 * 
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

	// Root of the tree
	private Node root;

	// Branching factor is the number of children nodes
	// for internal nodes of the tree
	private int branchingFactor;
	//height of the tree
	private int height;
	//num of Items in the tree
	private int numOfItems;

	/**
	 * Public constructor
	 * 
	 * @param branchingFactor
	 * @author sapan (sapan@cs.wisc.edu)
	 */
	public BPTree(int branchingFactor) {
		//need more than two children nodes
		if (branchingFactor <= 2) {
			throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
		}
		this.branchingFactor = branchingFactor;
		//only 1 node at start
		root = new LeafNode();
		//height is 1 at start
		height = 1;
		//no item at start
		numOfItems = 0;
	}

	/**
	 * Inserts the key and value in the appropriate nodes in the tree
	 * 
	 * key-value pairs with duplicate keys can be inserted into the tree.
	 * 
	 * @param key
	 * @param value
	 * @author sapan (sapan@cs.wisc.edu)
	 */
	@Override
	public void insert(K key, V value) {
		if (root == null) {
			root = new LeafNode();
			root.insert(key, value);
			return;
		}
		root.insert(key, value);
	}

	/**
	 * for later use
	 */
    private void connect() {
    	
    }

	/**
	 * Gets the values that satisfy the given range search arguments.
	 * 
	 * Value of comparator can be one of these: "<=", "==", ">="
	 * 
	 * If key is null or not found, return empty list. If comparator is null, empty,
	 * or not according to required form, return empty list.
	 * 
	 * @param key to be searched
	 * @param comparator is a string
	 * @return list of values that are the result of the range search; if nothing
	 * found, return empty list
	 * @author sapan (sapan@cs.wisc.edu)
	 */
	@Override
	public List<V> rangeSearch(K key, String comparator) {
		if (!comparator.contentEquals(">=") && 
		!comparator.contentEquals("==") && !comparator.contentEquals("<="))
			return new ArrayList<V>();
		return root.rangeSearch(key, comparator);
	}

	/**
	 * Returns a string representation for the tree This method is provided to
	 * students in the implementation.
	 * 
	 * @return a string representation
	 * @author sapan (sapan@cs.wisc.edu)
	 */
	@Override
	public String toString() {
		Queue<List<Node>> queue = new LinkedList<List<Node>>();
		queue.add(Arrays.asList(root));
		StringBuilder sb = new StringBuilder();
		while (!queue.isEmpty()) {
			Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
			while (!queue.isEmpty()) {
				List<Node> nodes = queue.remove();
				sb.append('{');
				Iterator<Node> it = nodes.iterator();
				while (it.hasNext()) {
					Node node = it.next();
					sb.append(node.toString());
					if (it.hasNext())
						sb.append(", ");
					if (node instanceof BPTree.InternalNode)
						nextQueue.add(((InternalNode) node).children);
				}
				sb.append('}');
				if (!queue.isEmpty())
					sb.append(", ");
				else {
					sb.append('\n');
				}
			}
			queue = nextQueue;
		}
		return sb.toString();
	}

	/**
	 * This abstract class represents any type of node in the tree This class is a
	 * super class of the LeafNode and InternalNode types.
	 * 
	 * @author sapan (sapan@cs.wisc.edu)
	 */
	private abstract class Node {

		// List of keys
		List<K> keys;

		/**
		 * Package constructor
		 */
		Node() {
			keys = new ArrayList<K>();
		}

		/**
		 * Inserts key and value in the appropriate leaf node and balances the tree if
		 * required by splitting
		 * 
		 * @param key
		 * @param value
		 */
		abstract void insert(K key, V value);

		/**
		 * Gets the first leaf key of the tree
		 * 
		 * @return key
		 */
		abstract K getFirstLeafKey();

		/**
		 * Gets the new sibling created after splitting the node
		 * 
		 * @return Node
		 */
		abstract Node split();

		/**
		 * set the value of previous node
		 * 
		 * @param LeafNode n
		 */
		abstract void setPrevious(LeafNode n);

		/**
		 * set the value of next node
		 * 
		 * @param LeafNode n
		 */
		abstract void setNext(LeafNode n);

		/*
		 * (non-Javadoc)
		 * 
		 * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
		 */
		abstract List<V> rangeSearch(K key, String comparator);

		/**
		 * @return boolean
		 */
		abstract boolean isOverflow();

		abstract boolean isLeaf();

		public String toString() {
			return keys.toString();
		}
	}

	/**
	 * This class represents an internal node of the tree. This class is a concrete
	 * sub class of the abstract Node class and provides implementation of the
	 * operations required for internal (non-leaf) nodes.
	 * 
	 * @author sapan
	 */
	private class InternalNode extends Node {

		// List of children nodes
		List<Node> children;

		/**
		 * Package constructor
		 */
		InternalNode() {
			super();
			children = new ArrayList<Node>();
		}

		/**
		 * Gets the first leaf key of the tree
		 * 
		 * @return key
		 */
		K getFirstLeafKey() {
			if (children.size() > 0)
				return children.get(0).keys.get(0);
			return null;
		}

		/**
		 * set the value of next node
		 */
		void setNext() {
		}

		/**
		 * set the value of previous node
		 */
		void setPrevious() {
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#isOverflow()
		 */
		boolean isOverflow() {
			if (keys.size() >= branchingFactor)
				return true;
			return false;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
		 */
		void insert(K key, V value) {
			int loc = location(key);
			children.get(loc).insert(key, value);
			if (children.get(loc).isOverflow()) {
				InternalNode newNode = (BPTree<K, V>.InternalNode) children.get(loc).split();
				for (K nK : newNode.keys) {
					keys.add(nK);
				}
				children.remove(loc);
				children.add(loc, newNode.children.get(0));
				children.add(loc + 1, newNode.children.get(1));
			}
			connect();
			superConnect();
			Collections.sort(keys);
		}

		/**
		 * connect current node to previous and next node
		 */
		void connect() {
			//nothing to connect
			if (children.get(0) == null)
				return;
			int index = 0;
			//if there is a leaf, increment index
			if (children.get(index).isLeaf()) {
				index++;
				//connect to previous and next
				while (children.get(index) != null) {
					children.get(index - 1).setNext((LeafNode) children.get(index));
					children.get(index).setPrevious((LeafNode) children.get(index - 1));
					index++;
				}
			}
		}

		/**
		 * connect all leaf nodes
		 */
		void superConnect() {
			//if no node or only 1 node, do nothing
			if (children.get(0) == null || children.get(0).isLeaf())
				return;
			//find leftmost and rightmost node
			InternalNode leftMost = (BPTree<K, V>.InternalNode) children.get(0);
			InternalNode rightMost = (BPTree<K, V>.InternalNode) children.get(children.size() - 1);
			//find each leaf
			for (int i = 0; i < children.size() - 1; i++) {
				//find left child and size
				InternalNode leftChild = (BPTree<K, V>.InternalNode) children.get(i);
				int lSize = leftChild.children.size();
				//find right child and size
				InternalNode rightChild = (BPTree<K, V>.InternalNode) children.get(i + 1);
				int rSize = rightChild.children.size();
				//initialize n1 and n2
				LeafNode n1 = null;
				LeafNode n2 = null;
				while (true) {
					//as long as a leaf, connect
					if (leftChild.children.get(lSize - 1).isLeaf()) {
						n1 = (BPTree<K, V>.LeafNode) leftChild.children.get(lSize - 1);
						n2 = (BPTree<K, V>.LeafNode) rightChild.children.get(0);
						n1.setNext(n2);
						n2.setPrevious(n1);
						break;
					}
					//set left and right child
					leftChild = (BPTree<K, V>.InternalNode) leftChild.children.get(lSize - 1);
					rightChild = (BPTree<K, V>.InternalNode) rightChild.children.get(0);
				}
			}
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#split()
		 */
		Node split() {
			InternalNode newPar = new InternalNode();
			//insert at middle
			newPar.keys.add(keys.size() / 2, keys.get(keys.size() / 2));
			InternalNode left = new InternalNode();
			//left side of split
			for (int i = 0; i < (keys.size() / 2 - 1); i++) {
				left.keys.add(i, keys.get(i));
			}
			newPar.children.add(0, left);
			InternalNode right = new InternalNode();
			//right side of split
			for (int i = (keys.size() / 2 + 1); i < keys.size(); i++) {
				right.keys.add(i, keys.get(i));
			}
			newPar.children.add(1, right);
			return newPar;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
		 */
		List<V> rangeSearch(K key, String comparator) {
			int loc = -1;
			//if key is in the list, find index
			if (keys.contains(key))
				loc = keys.indexOf(key);
			//if not in list, call location
			else {
				loc = location(key);
			}
			//recursive call
			return children.get(loc).rangeSearch(key, comparator);

		}

		/**
		 * find the given key in the tree
		 * 
		 * @param K key
		 * @return index of key
		 */
		int location(K key) {
			//return index on success
			for (int i = 0; i < keys.size(); i++) {
				if (key.compareTo(keys.get(i)) <= 0)
					return i;
			}
			//pretty much fail if return size instead
			return keys.size();
		}

		/**
		 * Internal node is not a leaf
		 */
		@Override
		boolean isLeaf() {
			return false;
		}

		/**
		 * not using this
		 */
		@Override
		void setPrevious(BPTree<K, V>.LeafNode n) {
			// TODO Auto-generated method stub
		}

		/**
		 * not using this
		 */
		@Override
		void setNext(BPTree<K, V>.LeafNode n) {
			// TODO Auto-generated method stub
		}
	}

	/**
	 * This class represents a leaf node of the tree. This class is a concrete sub
	 * class of the abstract Node class and provides implementation of the
	 * operations that required for leaf nodes.
	 * 
	 * @author sapan
	 */
	private class LeafNode extends Node {

		// List of values
		List<V> values;

		// Reference to the next leaf node
		LeafNode next;

		// Reference to the previous leaf node
		LeafNode previous;

		int size;

		/**
		 * Package constructor
		 */
		LeafNode() {
			super();
			values = new LinkedList<V>();
			next = null;
			previous = null;
			size = 0;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#getFirstLeafKey()
		 */
		K getFirstLeafKey() {
			return keys.get(0);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#isOverflow()
		 */
		boolean isOverflow() {
			if (keys.size() >= branchingFactor)
				return true;
			return false;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#insert(Comparable, Object)
		 */
		void insert(K key, V value) {
			keys.add(size, key);
			Collections.sort(keys);
			values.add(keys.indexOf(key), value);
			size++;

		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#split()
		 */
		Node split() {
			InternalNode newPar = new InternalNode();
			//find middle
			newPar.keys.add(keys.size() / 2, keys.get(keys.size() / 2));
			LeafNode left = new LeafNode();
			//left of split
			for (int i = 0; i < (keys.size() / 2 - 1); i++) {
				left.keys.add(i, keys.get(i));
				left.values.add(i, values.get(i));
			}
			//right of split
			LeafNode right = new LeafNode();
			for (int i = (keys.size() / 2); i < keys.size(); i++) {
				right.keys.add(i, keys.get(i));
				left.values.add(i, values.get(i));
			}
			//connect each other
			left.setNext(right);
			right.setPrevious(left);
			newPar.children.add(0, left);
			newPar.children.add(1, right);
			return newPar;
		}
		
		/**
		 * set previous node
		 * 
		 * @param LeafNode n
		 */
		void setPrevious(LeafNode n) {
			previous = n;
		}

		/**
		 * set next node
		 * 
		 * @param LeafNode n
		 */
		void setNext(LeafNode n) {
			next = n;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#rangeSearch(Comparable, String)
		 */
		List<V> rangeSearch(K key, String comparator) {
			//set up list to store values
			List<V> val = values;
			if (keys.contains(key)) {
				//less than or equal to given key
				if (comparator.equals("<=")) {
					ArrayList<V> items = new ArrayList<V>();
					//index of given key
					int index = keys.indexOf(key);
					V valueToAdd = val.get(index);
					index++;
					//if reach end, go back
					if (index == keys.size())
						index--;
					//if not found, go back
					else if (!keys.get(index).equals(key))
						index--;
					else {
						//if found, go to next 
						while (keys.get(index).equals(key)) {
							index++;
							if (index == val.size()) {
								//reach null, break
								if (next == null)
									break;
								//found item, reset
								val = next.values;
								keys = next.keys;
								next = next.next;
								index = 0;
							}
						}
						index--;
					}
					while (index >= 0) {
						//loop through tree
						valueToAdd = val.get(index);
						items.add(valueToAdd);
						index--;
						//reaches end
						if (index == 0) {
							if (previous != null) {
								//get values before
								val = previous.values;
								previous = previous.previous;
								index = val.size() - 1;
							}
						}
					}
					return items;
				} 
				//if equal
				else if (comparator.equals("==")) {
					//set up list
					ArrayList<V> items = new ArrayList<V>();
					//index of given key
					int index = keys.indexOf(key);
					V valueToAdd = val.get(index);
					items.add(valueToAdd);
					while (true) {
						index += 1;
						//search only equal
						if (key.equals(keys.get(index)))
							items.add(val.get(index));
						else
							break;
					}
					return items;
				} 
				//if greater or equal
				else if (comparator.equals(">=")) {
					//set up list
					ArrayList<V> items = new ArrayList<V>();
					int index = keys.indexOf(key);
					V valueToAdd = val.get(index);
					items.add(valueToAdd);
					index++;
					//loop through tree
					while (index < keys.size()) {
						valueToAdd = val.get(index);
						items.add(valueToAdd);
						index++;
						//reaches end
						if (index == keys.size()) {
							if (next != null) {
								val = next.values;
								next = next.next;
								index = 0;
							}	
						}
					}
					return items;
				}
			} 
			else {
				//set up list
				ArrayList<V> items = new ArrayList<V>();
				if (keys.size() == 0)
					return items;
				//if less or equal
				if (comparator.equals("<=")) {
					int index = 0;
					//found
					if (keys.get(index).compareTo(key) > 0)
						return items;
					while (true) {
						//not found, go to next
						if (keys.get(index).compareTo(key) < 0) {
						index++;
						} 
						else
							break;
						//reach end
						if (index == keys.size()) {
							if (next != null) {
								keys = next.keys;
								val = next.values;
								next = next.next;
								index = 0;
							} 
							else
								break;
						}
					}
					index--;
					//add to tentative list
					V valueToAdd = val.get(index);
					items.add(valueToAdd);
					if (index == 0)
						return items;
					index--;
					valueToAdd = val.get(index);
					while (index >= 0) {
						//add till reach end
						valueToAdd = val.get(index);
						items.add(valueToAdd);
						index--;
						if (index == 0) {
							//reach end
							if (previous != null) {
								val = previous.values;
								previous = previous.previous;
								index = val.size() - 1;
							}
						}
					}
					return items;
				}
				//equal
				else if (comparator.equals("==")) {
					items = new ArrayList<V>();
					return items;
				}
				//greater or equal
				else if (comparator.equals(">=")) {
					//set up list
					items = new ArrayList<V>();
					int index = keys.size() - 1;
					//found
					if (keys.get(index).compareTo(key) < 0)
						return items;
					while (true) {
						//not found, go previous
						if (keys.get(index).compareTo(key) > 0) {
							index--;
						} else
							break;
						if (index == -1) {
							//reach end
							if (previous != null) {
								val = previous.values;
								keys = previous.keys;
								previous = previous.previous;
								index = keys.size() - 1;
							} else
								break;
						}
					}
					index++;
					//add to tentative value
					V valueToAdd = val.get(index);
					items.add(valueToAdd);
					index++;
					//loop through
					while (index < keys.size()) {
						valueToAdd = val.get(index);
						items.add(valueToAdd);
						index++;
						//reach end
						if (index == keys.size()) {
							if (next != null) {
								val = next.values;
								next = next.next;
								index = 0;
							}
						}
					}
					return items;
				}
			}
			return null;
		}
		
		/**
		 * leaf node is a leaf
		 */
		@Override
		boolean isLeaf() {
			return true;

		}

	} 
}
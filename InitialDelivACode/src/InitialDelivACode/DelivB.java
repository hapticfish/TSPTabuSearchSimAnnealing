package InitialDelivACode;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Class DelivB does the work for deliverable DelivB of the Prog340

/*
 * TODO 
 * 
 * -find solution for labeling ('T') edges when node nextNode found
 * -Make sure that DelivA toString also works (make custom toString for DelivB)
 * -Make sure toString formating is correct
 * 
 * -add notes to classes (Node, Edge, etc) where additional code was added
 *
 */

/**
 * DelivB Depth First Search with edge type labeling.
 * 
 * Changes where made to the Node Class and the Edge Class
 * 
 * @author jquin
 *
 */

public class DelivB {

	File inputFile;
	File outputFile;
	PrintWriter output;
	static int time = 0;

	Graph g;

	public DelivB(File in, Graph gr) {
		inputFile = in;
		g = gr;

		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring(0, inputFileName.length() - 4); // Strip off ".txt"
		String outputFileName = baseFileName.concat("_out.txt");
		outputFile = new File(outputFileName);
		if (outputFile.exists()) { // For retests
			outputFile.delete();
		}

		try {
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}

		DFS();
		// print out required info
		printNodeandEdgeDetails();
		output.flush();
		System.out.println("");
		output.println("");
	}

	/**
	 * DFS method initialize time to 0, edges to 'F' find starting node and call
	 * DFSVist
	 * 
	 * condition accounting for disconnected graph sections after complete assign
	 * remaining edge types
	 *
	 */
	public void DFS() {
		time = 0;
		Node startingNode = null;

		// initialize edge types to 'F'
		for (Edge edge : g.edgeList) {
			edge.setType('F');
		}

		// find starting node
		for (Node node : g.nodeList) {
			if (node.getVal().equalsIgnoreCase("S")) {
				startingNode = node;
			}
		}
		DFSVisit(startingNode);
		// checking for case of disconnected graph, sort by Node list
		Collections.sort(g.nodeList, new NameComparator());
		for (Node node : g.nodeList) {
			if (node.getColor().equals("WHITE")) {
				DFSVisit(node);
			}
		}
		edgeAssigment();
	}// end DFS

	/**
	 * DFSVisit method
	 * 
	 * increment time, label Node color, initialize nextNode
	 * 
	 * Method for navigation of the directed graph with conditions node.color
	 * explanation GRAY = currently exploring node neighbors WHITE = unexplored node
	 * BLACK = explored all neighbor nodes
	 *
	 */
	private void DFSVisit(Node node) {
		time++;
		node.startTime = time;
		node.color = "GRAY";
		ArrayList<Edge> edges = node.getOutgoingEdges();

		if (edgeLabelsAllInts(node)) {
			Collections.sort(edges, new IntEdgeComparator());
		} else if (!edgeLabelsAllInts(node) || edgeLabelsNegativeVal(node)) {
			Collections.sort(edges, new LabelEdgeComparator());
		}
		if (edges.isEmpty()) {

		}
		for (Edge e : edges) {
			if (e.getHead().color.equals("WHITE")) {
				DFSVisit(e.getHead());
				e.setType('T');
			}
		}
		node.color = "BLACK";
		time++;
		node.finishTime = time;

	}// end DFSVisit

	/*
	 * edgeAssignment() method
	 * 
	 * Assigns the edge types with character values pertaining to their respective
	 * edge type 'T' Tree edge, 'B' Back edge, 'F' Forward edge
	 */
	public void edgeAssigment() {

		for (Edge edge : g.edgeList) {
			Node u = edge.tail;
			Node v = edge.head;
			if (u.startTime < v.startTime && v.startTime < v.finishTime && v.finishTime < u.finishTime) {

			} else if (v.startTime <= u.startTime && u.startTime < u.finishTime && u.finishTime <= v.finishTime) {
				edge.setType('B');// set back edge
			} else if (v.startTime < v.finishTime && v.finishTime < u.startTime && u.startTime < u.finishTime) {
				edge.setType('C'); // set Cross Edge

			} else if (edge.edgeType != 'T' || edge.edgeType != 'B' || edge.edgeType != 'C') {
				edge.setType('F');// set edge as forward edge when not 'T'
			}

		}
	}// end edgeAssignment

	/*
	 * edgeLabelsAllInts() Checks for condition where the labels of all outgoing
	 * edges are integers.
	 */
	public boolean edgeLabelsAllInts(Node node) {

		boolean isInt = true;
		for (Edge e : node.getOutgoingEdges()) {
			try {
				int v = Integer.parseInt(e.getLabel());
				if (v < 0) {
					return false;
				}
			} catch (NullPointerException nex) {
				return false;
			} catch (NumberFormatException ne) {
				return false;
			}
		}
		return isInt;
	}// end edgeLabelsAllInts

	/**
	 * edgeLabelsNegitiveVal() Check if if outgoing edge labels are negative
	 *
	 */
	public boolean edgeLabelsNegativeVal(Node node) {
		boolean isNegitive = false;
		for (int i = 0; i < node.outgoingEdges.size(); i++) {
			String edgeLable = node.outgoingEdges.get(i).getLabel();
			if (edgeLable == null) {
				isNegitive = false;
			}
			try {

				int check = Integer.parseInt(edgeLable);
				if (check < 0) {
					isNegitive = true;
					break;
				}
			} catch (NumberFormatException nfe) {
				isNegitive = false;
			}
		}
		return isNegitive;
	}// end edgeLabelsNegativeVal()

	/**
	 * valComparator.compare Node val field comparator
	 *
	 */
	class valComparator implements Comparator<Node> {

		public int compare(Node node1, Node node2) {
			return node1.getVal().compareTo(node2.getVal());
		}
	}

	/**
	 * nameComparator.compare Node name field comparator
	 *
	 */
	class NameComparator implements Comparator<Node> {

		@Override
		public int compare(Node node1, Node node2) {

			return node1.getName().compareTo(node2.getName());
		}
	}

	/**
	 * EdgeComparator.compare Edge head node name field comparator
	 *
	 */
	class IntEdgeComparator implements Comparator<Edge> {
		@Override
		public int compare(Edge edge1, Edge edge2) {
			int cmp = Integer.parseInt(edge1.getLabel());
			if (cmp == 0) {
				return edge1.getHead().getName().compareTo(edge2.getHead().getName());
			}

			return cmp;
		}
	}

	class LabelEdgeComparator implements Comparator<Edge> {
		@Override
		public int compare(Edge edge1, Edge edge2) {
			return edge1.getHead().getName().compareTo(edge2.getHead().getName());
		}
	}

	/**
	 * countOutgoingWhiteNode() count the number of outgoing white nodes and return
	 * the number
	 *
	 */
	private int countOutgoingWhiteNode(Node node) {
		int countWhite = 0;
		for (Edge edge : node.outgoingEdges) {
			Node nextNode = edge.head;
			if (nextNode.getColor().equalsIgnoreCase("WHITE")) {
				countWhite++;
			}
		}
		return countWhite;
	}// end countOutgoingWhiteNode

	/**
	 * printNodeandEdgeDetails() Prints the nodeList toString and the edgeList to
	 * String
	 *
	 */
	private void printNodeandEdgeDetails() {

		System.out.println("Node    Start Time    End Time");
		for (Node n : g.nodeList) {
			System.out.println(n.nameStartFinish());
			output.println(n.nameStartFinish());
		}
		System.out.println();
		System.out.println("Edge      Type");
		for (Edge e : g.edgeList) {
			System.out.println(e.abbrevAndType());
			output.println(e.abbrevAndType());
		}

	}// end printNodeandEdgeDetails

}// end class

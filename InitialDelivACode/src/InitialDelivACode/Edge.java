package InitialDelivACode;

// Edge Between Two Nodes
public class Edge {

	String label;
	int dist;
	Node tail;
	Node head;
	char edgeType;

	public Edge(Node tailNode, Node headNode, String theLabel) {
		setLabel(theLabel);
		setTail(tailNode);
		setHead(headNode);
	}

	public String getLabel() {
		return label;
	}

	public Node getTail() {
		return tail;
	}

	public Node getHead() {
		return head;
	}

	public int getDist() {
		return dist;
	}

	public void setLabel(String s) {
		label = s;
	}

	public void setTail(Node n) {
		tail = n;
	}

	public void setHead(Node n) {
		head = n;
	}

	public void setDist(String s) {
		try {
			dist = Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			dist = Integer.MAX_VALUE;
		}
	}

	public char getType() {
		return edgeType;
	}

	public void setType(char type) {
		this.edgeType = type;
	}

	/*
	 * toString formatted to print Edge and label.
	 * 
	 */
	public String toString() {
		return "Edge from " + tail.getName() + " to " + head.getName() + " labeled " + label;
	}

	/*
	 * Print Edge and type.
	 * 
	 */
	public String abbrevAndType() {
		return tail.getAbbrev() + "-" + head.getAbbrev() + " " + this.getType();

	}
}

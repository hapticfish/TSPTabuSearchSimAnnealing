package InitialDelivACode;

import java.util.ArrayList;

// A node of a graph for the Spring 2018 ICS 340 program

public class Node {

	String color;
	int startTime;
	int finishTime;
	Node parent;
	String name;
	String val; // The value of the Node
	String abbrev; // The abbreviation for the Node
	ArrayList<Edge> outgoingEdges;
	ArrayList<Edge> incomingEdges;

	public Node(String theAbbrev) {
		setAbbrev(theAbbrev);
		color = "WHITE";
		startTime = 0;
		finishTime = 0;
		parent = null;
		val = null;
		name = null;
		outgoingEdges = new ArrayList<Edge>();
		incomingEdges = new ArrayList<Edge>();
	}

	public String getAbbrev() {
		return abbrev;
	}

	public String getName() {
		return name;
	}

	public String getVal() {
		return val;
	}

	public ArrayList<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}

	public ArrayList<Edge> getIncomingEdges() {
		return incomingEdges;
	}

	public void setAbbrev(String theAbbrev) {
		abbrev = theAbbrev;
	}

	public void setName(String theName) {
		name = theName;
	}

	public void setVal(String theVal) {
		val = theVal;
	}

	public void addOutgoingEdge(Edge e) {
		outgoingEdges.add(e);
	}

	public void addIncomingEdge(Edge e) {
		incomingEdges.add(e);
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	// Check for val == int
	// Ref
	// https://stackoverflow.com/questions/12558206/how-can-i-check-if-a-value-is-of-type-integer
	public boolean isValInt() {
		try {
			Integer.parseInt(this.val);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}

	}

	public String nameStartFinish() {
		return this.getName() + " " + this.getStartTime() + " " + this.getFinishTime();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}

}

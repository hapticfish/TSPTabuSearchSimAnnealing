package InitialDelivACode;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

// Class DelivC does the work for deliverable DelivC of the Prog340

public class DelivC {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;

	public DelivC(File in, Graph gr) {
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

		ArrayList<Edge> edges = gr.getEdgeList();
		for (Edge e : edges) {
			String label = e.getLabel();
			if (label.equals("~")) {
				e.setDist("0");
			} else {
				e.setDist(label);
			}
		}
		localSearch();

		output.flush();
		output.close();
	}

	/**
	 * @author jquin
	 * @Source https://en.wikipedia.org/wiki/Tabu_search This algorithm was
	 *         developed from the sudo code provided by the Wiki above.
	 *
	 *         Executes a localSearch of the cites utilizing Tabu Search and
	 *         Simulated Annealing Strategies.
	 *
	 * @param N/A
	 * @return void
	 */
	private void localSearch() {
		ArrayList<Node> cities = g.nodeList;
		Random rand = new Random();
		boolean randomRestart = true;
		String path = "";
		int stepsTaken = 0;
		int lastMatch = 0;
		final int maxSteps = 5000; // Optimize
		int maxSmallSteps = 400; // OSptimize
		int subStringLength = 15; // Optimize
		int tabuListSizeMaintainer = 300; // Optimize
		int smallStepsIncrementor = 600; // Optimize
		double aP = 50;

		ArrayDeque<String> tabuList = new ArrayDeque<>(100);
		Collections.shuffle(cities);
		ArrayList<Node> bestRoute = new ArrayList<>(cities);
		int shortestPath = distanceCost(cities);
		path = buildPath(cities);
		writePathToFile(path, shortestPath, randomRestart);

		// outerWhile
		while (stepsTaken < maxSteps) {
			Collections.shuffle(cities);
			randomRestart = true;
			int distance = distanceCost(cities);
			writePathToFile(path, shortestPath, randomRestart);
//			annealingControl(aP, stepsTaken, maxSteps);  //to long running time, but better results though

			if (distance < shortestPath || (stepsTaken - lastMatch) > aP) {
				path = buildPath(cities);

				if (distance < shortestPath) {
//					System.out.println(
//							"step " + stepsTaken + " OuterLoop Improvement from " + shortestPath + " to " + distance);
//					System.out.println(path);
					shortestPath = distance;
					bestRoute = new ArrayList<>(cities);
					printImprovedTour(path, shortestPath);
					writePathToFile(path, shortestPath, randomRestart);
				} else {
					// taking a sub optimal path.

					if (tabuList.contains(path.substring(0, subStringLength))) {
//						System.out.println(" Already been here.");
						writePathToFile(path, shortestPath, randomRestart);
						continue;
					}
				}
				tabuList.addLast(path.substring(0, subStringLength)); // optimize
				if (tabuList.size() == tabuListSizeMaintainer) { // optimize //control for size of tabuList
					tabuList.removeFirst();
				}

				lastMatch = stepsTaken;
				int smallSteps = 0;

				// innerWhile
				// swap city positions
				while (smallSteps < maxSmallSteps) {

					Collections.swap(cities, rand.nextInt(cities.size()), rand.nextInt(cities.size()));
					randomRestart = false;
					path = buildPath(cities);
					bestRoute = new ArrayList<>(cities);
					distance = distanceCost(cities);

					if (distance < shortestPath) {
//						System.out.println(stepsTaken + " " + smallSteps + " InnerLoop Improvement " + shortestPath
//								+ " to " + distance);
//						System.out.println(buildPath(bestRoute));
						shortestPath = distance;
						printImprovedTour(path, shortestPath);
					}
					writePathToFile(path, shortestPath, randomRestart);
					smallSteps++;
				}
				maxSmallSteps += smallStepsIncrementor; // optimize
			}
			stepsTaken++;
		}
		System.out.println("Done");
		printImprovedTour(path, shortestPath);

	}

	/**
	 * @author jquin
	 *
	 *         Sum the distance cost of the edges in the order of the cities
	 *
	 * @param ArrayList<Node>
	 * @return int
	 */
	private int distanceCost(ArrayList<Node> cities) {
		int distance = 0;
		int numCities = cities.size();
		Node head = null;
		Node tail = null;
		for (int i = 0; i < cities.size(); i++) {
			if (i == numCities - 1) {
				head = cities.get(0);
				tail = cities.get(i);
			} else {
				head = cities.get(i + 1);
				tail = cities.get(i);
			}
			distance += findOutGoingEdgeDistance(cities, i, head, tail);
		}
		return distance;

	}

	/**
	 * @author jquin
	 *
	 *         Builds the string for the current city path taken.
	 *
	 * @param ArrayList<Node>
	 * @return String
	 */
	private String buildPath(ArrayList<Node> cities) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < cities.size(); i++) {
			b.append(cities.get(i).getAbbrev()); // changed name to abb
			b.append("->");
		}
		b.append(cities.get(0).getAbbrev());

		return b.toString();
	}

	/**
	 * @author jquin
	 *
	 *         Finds the outgoing edge distance to the next city in the current city
	 *         path.
	 *
	 * @param ArrayList<Node>, int, Node, Node
	 * @return int
	 */
	private int findOutGoingEdgeDistance(ArrayList<Node> cities, int index, Node head, Node tail) {
		int edgeDistance = 0;
		ArrayList<Edge> edges = cities.get(index).getOutgoingEdges();
		for (Edge edge : edges) {
			if (edge.getHead() == head && edge.getTail() == tail) {
				edgeDistance = edge.getDist();
				break;
			}
		}
		return edgeDistance;

	}

	/**
	 * @author jquin
	 *
	 *         Prints the improved tour
	 *
	 * @param String, int
	 * @return void
	 */
	public void printImprovedTour(String path, int distance) {
		System.out.println(path + " " + distance);
	}

	/**
	 * @author jquin
	 *
	 *         Writes the path taken to the output file.
	 *
	 * @param String, int, boolean
	 * @return void
	 */
	public void writePathToFile(String path, int distance, boolean randomRestart) {
		if (randomRestart) {
			output.println(path + " " + distance + " (random restart)");
		} else {
			output.println(path + " " + distance + " (swap)");
		}
	}

	/**
	 * @author jquin
	 *
	 *         For every 25% of the search space the annealingParameter is reduced
	 *         by a set percentage. 25% reduce by .85, 50% reduce by .65, 75% reduce
	 *         by .40, final search space reduce by .20
	 * 
	 *         Utilizing this method I was able to provide better results for larger
	 *         files but the running time increased to an unreasonable amount.
	 *
	 * @param double, int, int
	 * @return void
	 */
	private void annealingControl(double annealingParameter, int stepsTaken, int maxSteps) {
		if (annealingParameter > 25) {
			if (stepsTaken % 5 == 0 && stepsTaken < (maxSteps * .25)) {
				annealingParameter = annealingParameter * .85;
			} else if (stepsTaken % 5 == 0 && stepsTaken > (maxSteps * .25) && stepsTaken < (maxSteps * .50)) {
				annealingParameter = annealingParameter * .65;
			} else if (stepsTaken % 5 == 0 && stepsTaken > (maxSteps * .50) && stepsTaken < (maxSteps * .75)) {
				annealingParameter = annealingParameter * .40;
			} else if (stepsTaken % 5 == 0 && stepsTaken > (maxSteps * .75) && stepsTaken < maxSteps) {
				annealingParameter = annealingParameter * .20;
			}
		}

	}

}

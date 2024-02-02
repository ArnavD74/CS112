package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered
 * linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	public Transit() {
		trainZero = null;
	}

	public Transit(TNode tz) {
		trainZero = tz;
	}

	public TNode getTrainZero() {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations,
	 * bus
	 * stops, and walking locations. Each layer begins with a location of 0, even
	 * though
	 * the arrays don't contain the value 0. Store the zero node in the train layer
	 * in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops      Int array listing all the bus stops
	 * @param locations     Int array listing all the walking locations (always
	 *                      increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {
		int locationCounter = 0;
		int busCounter = 0;
		int trainCounter = 0;

		int walkingStop = 0;
		int busStop = 0;
		int trainStop = 0;

		TNode firstLocation = new TNode(0);
		TNode firstBus = new TNode(0, null, firstLocation);
		trainZero = new TNode(0, null, firstBus);

		TNode locationNode = null, busNode = null, trainNode = null;
		TNode prevLocationNode = firstLocation, prevBusNode = firstBus, prevTrainNode = trainZero;

		for (locationCounter = 0; locationCounter < locations.length; locationCounter++) {

			walkingStop = locations[locationCounter];

			if (busCounter < busStops.length) {
				busStop = busStops[busCounter];
			}

			if (trainCounter < trainStations.length) {
				trainStop = trainStations[trainCounter];
			}

			locationNode = new TNode(walkingStop);

			if (prevLocationNode != null) {
				prevLocationNode.setNext(locationNode);
			}

			prevLocationNode = locationNode;

			if (walkingStop == busStop) {

				busNode = new TNode(busStop, null, locationNode);
				if (prevBusNode != null) {
					prevBusNode.setNext(busNode);
				}

				prevBusNode = busNode;
				busCounter++;

				if (busStop == trainStop) {

					trainNode = new TNode(trainStop, null, busNode);
					if (prevTrainNode != null) {
						prevTrainNode.setNext(trainNode);
					}

					prevTrainNode = trainNode;
					trainCounter++;
				}
			}
		}
	}

	/**
	 * Modifies the layered list to remove the given train station but NOT its
	 * associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {

		TNode currentStop = trainZero.getNext();
		TNode previousStop = trainZero;

		while (currentStop != null) {

			if (currentStop.getLocation() == station) {
				previousStop.setNext(currentStop.getNext());
			}

			previousStop = currentStop;
			currentStop = currentStop.getNext();
		}
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do
	 * nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {

		TNode currentBus = trainZero.getDown();

		while (currentBus.getLocation() < busStop) {
			if (currentBus.getNext().getLocation() > busStop) {

				TNode location = currentBus.getDown();
				for (int i = 0;
					((location != null) && (location.getLocation() < busStop)); location = location.getNext()) {
					if (location.getLocation() == busStop) {
						location = currentBus.getDown();
					}
				}

				TNode newBus = new TNode(busStop, currentBus.getNext(), location);
				currentBus.setNext(newBus);
			}
			currentBus = currentBus.getNext();
		}
	}

	/**
	 * Determines the optimal path to get to a given destination in the walking
	 * layer, and
	 * collects all the nodes which are visited in this path into an arraylist.
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */


	public ArrayList < TNode > bestPath(int destination) {

		ArrayList < TNode > path = new ArrayList < > ();

		ArrayList < TNode > trainMap = new ArrayList < > ();
		TNode trainCurrent = trainZero;
		for (int i = 0;
			((trainCurrent != null) && (trainCurrent.getLocation() <= destination)); trainCurrent = trainCurrent.getNext()) {
			trainMap.add(trainCurrent);
		}

		ArrayList < TNode > busMap = new ArrayList < > ();
		TNode busCurrent = trainMap.get(trainMap.size() - 1).getDown();
		for (int i = 0;
			((busCurrent != null) && (busCurrent.getLocation() <= destination)); busCurrent = busCurrent.getNext()) {
			busMap.add(busCurrent);
		}

		ArrayList < TNode > locationMap = new ArrayList < > ();
		TNode locationCurrent = busMap.get(busMap.size() - 1).getDown();
		for (int i = 0;
			((locationCurrent != null) && (locationCurrent.getLocation() <= destination)); locationCurrent = locationCurrent.getNext()) {
			locationMap.add(locationCurrent);
		}
		path.addAll(trainMap);
		path.addAll(busMap);
		path.addAll(locationMap);
		return path;
	}


	/**
	 * Returns a deep copy of the given layered list, which contains exactly the
	 * same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {

		TNode train = trainZero;
		TNode result = new TNode();
		TNode scooterNode = result;

		while (train.getNext() != null) {
			result.setNext(new TNode(train.getNext().getLocation()));
			train = train.getNext();
			result = result.getNext();
		}

		train = trainZero.getDown();
		TNode temp = new TNode();

		result = temp;
		scooterNode.setDown(temp);

		while (train.getNext() != null) {
			result.setNext(new TNode(train.getNext().getLocation()));
			train = train.getNext();
			result = result.getNext();
		}

		train = trainZero.getDown().getDown();
		TNode copy2 = new TNode();
		result = copy2;
		scooterNode.getDown().setDown(copy2);

		while (train.getNext() != null) {
			result.setNext(new TNode(train.getNext().getLocation()));
			train = train.getNext();
			result = result.getNext();
		}

		TNode layerAboveAbove = scooterNode;
		TNode layer = temp;

		while (layerAboveAbove != null) {
			if (layerAboveAbove.getLocation() == layer.getLocation()) {
				layerAboveAbove.setDown(layer);
				layerAboveAbove = layerAboveAbove.getNext();
			}
			layer = layer.getNext();
		}

		layer = temp;
		TNode layerAbove = copy2;

		while (layer != null) {
			if (layer.getLocation() == layerAbove.getLocation()) {
				layer.setDown(layerAbove);
				layer = layer.getNext();
			}
			layerAbove = layerAbove.getNext();
		}

		result = scooterNode;
		return result;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are
	 *                     located
	 */
	public void addScooter(int[] scooterStops) {

		TNode busZero = trainZero.getDown();
		TNode scooterZero = new TNode();
		TNode scooterNode = scooterZero;
		int length = scooterStops.length;

		TNode trainNode = trainZero;
		TNode bus = trainNode.getDown();
		TNode location = bus.getDown();

		int trainLength = 0;
		while (trainNode != null) {
			trainLength++;
			trainNode = trainNode.getNext();
		}
		int busLength = 0;
		while (bus != null) {
			busLength++;
			bus = bus.getNext();
		}
		int locationLength = 0;
		while (location != null) {
			locationLength++;
			location = location.getNext();
		}

		scooterZero.setDown(trainZero.getDown().getDown()); //train > bus > scooter > location
		busZero.setDown(scooterZero);

		for (int i = 0; i < length; i++) {
			TNode nextScooter = new TNode(scooterStops[i]);
			scooterNode.setNext(nextScooter);
			scooterNode = scooterNode.getNext();
		}

		TNode busNode = busZero;
		scooterNode = scooterZero;

		while (busNode != null) {
			if (busNode.getLocation() == scooterNode.getLocation()) {
				busNode.setDown(scooterNode);
				busNode = busNode.getNext();
			}
			scooterNode = scooterNode.getNext();
		}

		scooterNode = scooterZero;
		TNode locationNode = scooterZero.getDown();

		while (scooterNode != null) {
			if (scooterNode.getLocation() == locationNode.getLocation()) {
				scooterNode.setDown(locationNode);
				scooterNode = scooterNode.getNext();
			}
			locationNode = locationNode.getNext();
		}
	}

	/**
	 * Used by the driver to display the layered linked list.
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null)
					break;

				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation() + 1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++)
						StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null)
				break;
			StdOut.println();

			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation())
					downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr)
					StdOut.print("|");
				else
					StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen - 1; j++)
					StdOut.print(" ");

				if (horizPtr.getNext() == null)
					break;

				for (int i = horizPtr.getLocation() + 1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++)
							StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}

	/**
	 * Used by the driver to display best path.
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList < TNode > path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr))
					StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++)
						StdOut.print(" ");
				}
				if (horizPtr.getNext() == null)
					break;

				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation() + 1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);

					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++)
						StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}

			if (vertPtr.getDown() == null)
				break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen - 1; j++)
					StdOut.print(" ");

				if (horizPtr.getNext() == null)
					break;

				for (int i = horizPtr.getLocation() + 1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++)
							StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
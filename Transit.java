import java.util.ArrayList;


/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 * @author Elizabeth Lam
 */
public class Transit {
	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 * @return The zero node in the train layer of the final layered linked list
	 */
	public static TNode makeList(int[] trainStations, int[] busStops, int[] locations) { //works

		TNode firstTrain = new TNode(0);
		TNode firstBus = new TNode(0);
		firstTrain.down = firstBus;
		TNode first = new TNode(0);
		firstBus.down = first;
		TNode ptr;

		ptr = firstTrain;
		for (int i = 0; i < trainStations.length; i++){
			ptr.next = new TNode(trainStations[i]);
			ptr = ptr.next;
		}

		ptr = firstBus;
		for(int i =0; i < busStops.length; i++){
			ptr.next = new TNode(busStops[i]);
			ptr = ptr.next;
		}

		ptr = first;
		for (int i=0; i < locations.length; i++){
			ptr.next = new TNode(locations[i]);
			ptr = ptr.next;
		}

		TNode ptr1 = firstTrain;
		TNode busNode = firstBus;
		while (ptr1 != null){
			if (ptr1.location == busNode.location){
				ptr1.down = busNode;
				ptr1 = ptr1.next;
				busNode = busNode.next;
			}
			else if (busNode.location < ptr1.location){
				busNode = busNode.next;
			}
			else if (ptr1.location  >  busNode.location){
				ptr1 = ptr1.next;
			}
		}

		ptr1 = firstBus;
		TNode walkNode = first;
		while (ptr1 != null){
			if (ptr1.location == walkNode.location){
				ptr1.down = walkNode;
				ptr1 = ptr1.next;
				walkNode = walkNode.next;
			}
			else if (walkNode.location < ptr1.location){
				walkNode = walkNode.next;
			}
			else if (ptr1.location  >  walkNode.location){
				ptr1 = ptr1.next;
			}
			
		}
		return firstTrain;
	}
	
	/**
	 * Modifies the given layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param station The location of the train station to remove
	 */
	public static void removeTrainStation(TNode trainZero, int station) { //need to test end cases like 19 on input2.txt and inside cases that have no stop

		TNode ptr = trainZero;

		while (ptr.next != null){

			if (ptr.next.location == station){

				if (ptr.next.next == null){
					ptr.next = null;
					return;
				}

				ptr.next = ptr.next.next;
	
			}
			ptr = ptr.next;
		}
	}

	/**
	 * Modifies the given layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param busStop The location of the bus stop to add
	 */
	public static void addBusStop(TNode trainZero, int busStop) { // works i think.
		// WRITE YOUR CODE HERE

		TNode x = trainZero.down.down;
		while (x.location != busStop){
			x = x.next;

		}

		TNode ptr = trainZero.down;

		while (ptr.next == null || ptr.next.location < busStop){

			if (ptr.next == null){
				TNode y = new TNode(busStop);
				y.down = x;
				ptr.next = y;
				return;
				
			}
			else{
				ptr = ptr.next;
			}

		}
		if (ptr.next.location != busStop){
		TNode temp = ptr.next;
		TNode z = new TNode (busStop);
		ptr.next = z;
		z.next = temp;
		z.down = x;

		}
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param destination An int representing the destination
	 * @return
	 */
	public static ArrayList<TNode> bestPath(TNode trainZero, int destination) { // works !!

		ArrayList<TNode> best = new ArrayList<TNode>();
		TNode ptr = trainZero;
	
		
		while (ptr != null){

			if (ptr.location == destination){
				best.add(ptr);
				ptr = ptr.down;
			}
			
			else if (ptr.location < destination){
				best.add(ptr);
				if (ptr.next == null){
					ptr = ptr.down;
				}
				else if (ptr.next.location <= destination){
					ptr = ptr.next;
				}
				else{
					ptr = ptr.down;
				}

			}
		}

		return best;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @return
	 */
	public static TNode duplicate(TNode trainZero) { 

		TNode newWalk = new TNode (0);
        TNode dupeWalk = newWalk;
        TNode ptr = trainZero.down.down.next;

        while (ptr != null){
            newWalk.next = new TNode (ptr.location);
            newWalk = newWalk.next;
            ptr = ptr.next;
        }

        TNode ptr1 = trainZero.down.next;
        TNode newBus = new TNode (0);
        TNode dupeBus = newBus;
        newBus.down = dupeWalk;
        dupeWalk = dupeWalk.next;

        while (ptr1 != null){
            newBus.next = new TNode(ptr1.location);
            newBus = newBus.next;

            while (ptr1 != null){
                if (newBus.location == dupeWalk.location) {
                    newBus.down = dupeWalk;
                    dupeWalk = dupeWalk.next;
                    break;
                }
                else {
                dupeWalk = dupeWalk.next;
                }
            }
            ptr1 = ptr1.next;
        }

        TNode ptr2 = trainZero.next;
        TNode newTrain = new TNode(0);
        TNode dupe = newTrain;
        newTrain.down = dupeBus;
        dupeBus = dupeBus.next;

        while (ptr2 != null){
            newTrain.next = new TNode(ptr2.location);
            newTrain = newTrain.next;

            while (ptr2 != null){
                if (newTrain.location == dupeBus.location) {
                    newTrain.down = dupeBus;
                    dupeBus = dupeBus.next;
                    break;
                }
                else {
                dupeBus = dupeBus.next;
                }
        
            }
            ptr2 = ptr2.next;
        }
        
        return dupe;

	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public static void addScooter(TNode trainZero, int[] scooterStops) { //needing to connecting down nodes

		TNode first = new TNode(0);
		first.next = new TNode(trainZero.down.down.next.location);

		TNode x  = trainZero.down.down.next.next;
		TNode y = first.next;

		while (x != null){
			y.next = new TNode (x.location, x.next, null);

			x = x.next;
			y = y.next;
		}

		TNode scooterNode = new TNode(0);
		scooterNode.down = first;
		TNode ptr = scooterNode;

		
	
		for (int i =0; i < scooterStops.length; i++){
			ptr.next = new TNode(scooterStops[i]);
			ptr = ptr.next;
			
		}
		
		
		//connecting scooterNode to busNodes
		TNode ptr1 = trainZero.down;
		while (ptr1 != null){
			if (ptr1.location == scooterNode.location){
				
				ptr1.down = scooterNode;
				ptr1 = ptr1.next;
				scooterNode = scooterNode.next;
			}
			else if (scooterNode.location < ptr1.location){
				scooterNode = scooterNode.next;
			}
			else if (scooterNode.location  >  ptr1.location){
				ptr1 = ptr1.next;
			}
			
		}

		// scooterNode.down points to locations 
		TNode p = trainZero.down.down;
		TNode q = trainZero.down.down.down;

		while ( p != null){
			if (p.location == q.location){
				p.down = q;
				p = p.next;
				q = q.next;
			}
			else if (p.location > q.location){
				q = q.next;
			}
			else if (p.location < q.location){
				p = p.next;
			}
		}
	}
}
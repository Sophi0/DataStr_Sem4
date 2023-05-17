package module;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MyGraph<T> {
	private MyVerticeNode[] graphElements;
	private final int DEFAULT_ARRAY_SIZE = 10;
	private int arraySize = DEFAULT_ARRAY_SIZE;
	private int elementCounter = 0;
	
	//no-args
	public MyGraph() {
		graphElements = new MyVerticeNode[arraySize];
	}
	
	//args
	public MyGraph(int inputArraySize) {
		if(inputArraySize > 0) {
			arraySize = inputArraySize;
		}
		graphElements = new MyVerticeNode[arraySize];
	}
		
	public boolean isFull() {
		return (elementCounter == arraySize);
	}
	
	public boolean isEmpty() {
		return (elementCounter == 0);
	}
	
	public int howManyElements() {
		return elementCounter;
	}
	
	private void increaseArray() {
		int newArraySize = (arraySize > 100) ? (int)(arraySize * 1.5) : arraySize * 2;
		MyVerticeNode[] newElements = new MyVerticeNode[newArraySize];
		for(int i = 0; i < elementCounter; i++) {
			newElements[i] = graphElements[i];
		}
		graphElements = newElements;
		arraySize = newArraySize;
	}
	
	//addVertice
	public void addVertice(T inputElement) throws Exception {
		if(inputElement == null) {
			throw (new Exception("It is not real vertice!"));
		}
		
		//verify if vertice is not already in graph
		/*for(int i = 0; i < elementCounter; i++) {
			if(graphElements[i].getElement().equals(inputElement)) {
				throw (new Exception("Vertice is already in graph"));
			}
		}*/
		
		if(searchVertice(inputElement) >= 1) {
			throw (new Exception("Vertice is already in graph"));
		}
		
		if(isFull()) {
			increaseArray();
		}
		
		MyVerticeNode newVertice = new MyVerticeNode<T>(inputElement);
		graphElements[elementCounter] = newVertice;
		elementCounter++;
		
		//for optimization
		//graphElements[elementCounter++] = new MyVerticeNode<T>(inputElement);
	}
	
	
	//addEdge
	public void addEdge(T elementFrom, T elementTo, float edgeWeigth) throws Exception {
		if(elementFrom == null || elementTo == null || edgeWeigth <= 0) {
			throw (new Exception("Incorrect arguments"));
		}
		int indexOfElementFrom = searchVertice(elementFrom);
		int indexOfElementTo = searchVertice(elementTo);
		
		 // Add vertices if they are not found in the graph
	    if (indexOfElementFrom < 0) {
	        addVertice(elementFrom);
	        indexOfElementFrom = searchVertice(elementFrom);
	    }
	    if (indexOfElementTo < 0) {
	        addVertice(elementTo);
	        indexOfElementTo = searchVertice(elementTo);
	    }
	    /*
		if(indexOfElementFrom < 0 || indexOfElementTo < 0) {
			throw (new Exception("One or both vertices are not in graph"));
		}
		*/
	    
		//check if an edge already exists between elementFrom and elementTo
	    //if exists, throw exception
		MyEdgeNode object = graphElements[indexOfElementFrom].getFirstEdge();
	    while (object != null) {
	        if (object.getIndexOfVertice() == indexOfElementTo) {
	            throw new Exception("Edge already exists");
	        }
	    //will move to the next
	        object = object.getNext();
	    }
	    
		MyEdgeNode newNode = new MyEdgeNode(indexOfElementTo, edgeWeigth);
		//add new edge to the list for elementFrom
		if(graphElements[indexOfElementFrom].getFirstEdge() == null) {
		//if elementFrom does not have any edges, it sets the new edge at the first edge
			graphElements[indexOfElementFrom].setFirstEdge(newNode);
		}
		else {
		//iterates through the edges of elementFrom and adds the new edge 
			MyEdgeNode temp = graphElements[indexOfElementFrom].getFirstEdge();
			while(temp.getNext() != null) {
				temp = temp.getNext();
			}
			temp.setNext(newNode);
		}
	}
	
	private int searchVertice(T inputElement) {
		//return index of element
		for(int i = 0; i < elementCounter; i++) {
			if(graphElements[i].getElement().equals(inputElement)) {
				return i;
			}
		}
		//return -1 if element is not there
		return -1;
	}
	
	
	//print
	public void print() throws Exception {
		//1.if verification if it is isEmpty
		if(isEmpty()) {
			throw (new Exception("Graph is empty"));
		}
		//2.for loop for vertices and print each vertice
		for(int i = 0; i < elementCounter; i++) {
			System.out.print(graphElements[i].getElement() + " --> ");
		//2.1.while loop for edges and print each edge of the specific vertice	
			MyEdgeNode tempEdgeNode = graphElements[i].getFirstEdge();
			
			while(tempEdgeNode != null) {
				T verticeTo = (T) graphElements[tempEdgeNode.getIndexOfVertice()].getElement();
				System.out.print(verticeTo + " (" + tempEdgeNode.getWeigth()  + " km); ");
				tempEdgeNode = tempEdgeNode.getNext();
			}
			System.out.println();
		}

		
	}
	
	//makeEmpty
	public void makeEmpty() {
		elementCounter = 0;
		arraySize = DEFAULT_ARRAY_SIZE;
		graphElements = new MyVerticeNode[arraySize];
		System.gc();
	}
	

	//removeVertice
	public void removeVertice(T inputElement) throws Exception {
		if (inputElement == null) {
			throw (new Exception("It is not a real vertice")); 
		}
		//find the index of the vertice to be removed
		int indexToRemove = searchVertice(inputElement);

		if (indexToRemove < 0) {
			throw (new Exception("Vertice is not in graph")); 
		}
		//remove all edges that connect to the vertice to be removed
		for (int i = 0; i < elementCounter; i++) {
			MyEdgeNode temp = graphElements[i].getFirstEdge();
			MyEdgeNode prev = null;
			while (temp != null) {
				if (temp.getIndexOfVertice() == indexToRemove) {
					if (prev == null) {
						graphElements[i].setFirstEdge(temp.getNext());
					} else {
						prev.setNext(temp.getNext());
					}
				}
				prev = temp;
				temp = temp.getNext();
			}
		}
		//remove the vertice
		graphElements[indexToRemove] = null;
		elementCounter--;

		//shift all elements after the removed vertice to the left
		for (int i = indexToRemove; i < elementCounter; i++) {
			graphElements[i] = graphElements[i+1];
		}
		//set null to remove the duplicate element copied from the previous index
		graphElements[elementCounter] = null;
	}
	 
	//updateVertice
	public void updateVertice(T oldElement, T newElement) throws Exception {
	    if (oldElement == null || newElement == null) {
	        throw new Exception("Invalid input");
	    }
	    int indexToUpdate = searchVertice(oldElement);
	    if (indexToUpdate < 0) {
	        throw new Exception("Vertice is not in graph");
	    }
	    graphElements[indexToUpdate].setElement(newElement);
	}
	
	//removeEdge
	public void removeEdge(T elementFrom, T elementTo) throws Exception {
	    if (elementFrom == null || elementTo == null) {
	        throw new Exception("Invalid input");
	    }
	    //find the indexes of the vertices that the edge connects
	    int indexFrom = searchVertice(elementFrom);
	    int indexTo = searchVertice(elementTo);
	    
	    if (indexFrom < 0 || indexTo < 0) {
	        throw new Exception("One or both vertices are not in graph");
	    }
	    
	    MyEdgeNode temp = graphElements[indexFrom].getFirstEdge();
	    MyEdgeNode prev = null;
	    //in while loop checks if any of the edges connect to the vertice elementTo
	    while (temp != null) {
	    //if an edge is found that connects elementFrom and elementTo, it is removed
	    //by setting the pointer of the previous edge to the next pointer of the current edge
	        if (temp.getIndexOfVertice() == indexTo) {
	            if (prev == null) {
	                graphElements[indexFrom].setFirstEdge(temp.getNext());
	            } 
	    //if the edge to be removed is the first edge of the vertice, the vertice pointer 
	    //firstEdge should be set to the next pointer of the current edge       
	            else {
	                prev.setNext(temp.getNext());
	            }
	    //stop the method
	            return;
	        }
	        prev = temp;
	        temp = temp.getNext();
	    }
	    throw new Exception("Edge does not exist");
	}

	//updateEdgeWeigth
	public void updateEdgeWeight(T elementFrom, T elementTo, float inputWeigth) throws Exception {
	    if (elementFrom == null || elementTo == null) {
	        throw new Exception("Invalid input");
	    }
	    int indexFrom = searchVertice(elementFrom);
	    int indexTo = searchVertice(elementTo);
	    
	    if (indexFrom < 0 || indexTo < 0) {
	        throw new Exception("One or both vertices are not in graph");
	    }
	    MyEdgeNode temp = graphElements[indexFrom].getFirstEdge();
	    
	    while (temp != null) {
	        if (temp.getIndexOfVertice() == indexTo) {
	            temp.setWeigth(inputWeigth);
	            //stop the method after the weight of the edge has been updated
	            return;
	        }
	        //go to the next edge
	        temp = temp.getNext();
	    }
	    //throw exception if loop was reached without finding an edge 
	    throw new Exception("Edge does not exist");
	}
	
	//updateEdgeByItsVertices
	public void updateEdgeByItsVerticeTo(T elementFrom, T oldElementTo, T newElementTo) throws Exception {
	    if (elementFrom == null || oldElementTo == null || newElementTo == null) {
	        throw new Exception("Invalid input");
	    }
	    //find the index of the vertice that the edge starts from
	    int indexFrom = searchVertice(elementFrom);
	    //find the index of the vertice that the edge currently points to
	    int oldIndexTo = searchVertice(oldElementTo);
	    //find the index of the vertice that the edge will be updated to point to
	    int newIndexTo = searchVertice(newElementTo);
	    
	    if (indexFrom < 0 || oldIndexTo < 0 || newIndexTo < 0) {
	        throw new Exception("One or more vertices are not in graph");
	    }
	    
	    //create temp edge node variable to the first edge of the vertice that the edge starts from
	    MyEdgeNode temp = graphElements[indexFrom].getFirstEdge();
	    while (temp != null) {
	        if (temp.getIndexOfVertice() == oldIndexTo) {
	        	//check if there is already an edge that points from the indexFrom to the newIndexTo
	            if (edgeExists(indexFrom, newIndexTo)) {
	                throw new Exception("Edge already exists");
	            }
	            //update index of the vertice that the current edge points to
	            temp.setIndexOfVertice(newIndexTo);
	            //stop the method
	            return;
	        }
	        temp = temp.getNext();
	    }
	    throw new Exception("Edge does not exist");
	}
	
	//edgeExists
	private boolean edgeExists(int indexFrom, int indexTo) {
	    MyEdgeNode temp = graphElements[indexFrom].getFirstEdge();
	    while (temp != null) {
	        if (temp.getIndexOfVertice() == indexTo) {
	            return true;
	        }
	        temp = temp.getNext();
	    }
	    return false;
	}
	
	//updateEdgeByItsVerticeFrom
	public void updateEdgeByItsVerticeFrom(T oldElementFrom, T elementTo, T newElementFrom) throws Exception {
	    if (oldElementFrom == null || elementTo == null || newElementFrom == null) {
	        throw new Exception("Invalid input");
	    }
	    //find the index of the vertice that the edge currently starts from
	    int oldIndexFrom = searchVertice(oldElementFrom);
	    //find the index of the vertice that the edge points to
	    int indexTo = searchVertice(elementTo);
	    //find the index of the vertice that the edge will be updated to start from
	    int newIndexFrom = searchVertice(newElementFrom);
	    
	    if (oldIndexFrom < 0 || indexTo < 0 || newIndexFrom < 0) {
	        throw new Exception("One or more vertices are not in graph");
	    }
	    
	    //create variable that the edge currently starts from
	    MyEdgeNode temp = graphElements[oldIndexFrom].getFirstEdge();
	    //create variable, which save info about prev edge node
	    MyEdgeNode prev = null;
	    while (temp != null) {
	        if (temp.getIndexOfVertice() == indexTo) {
	            if (edgeExists(newIndexFrom, indexTo)) {
	                throw new Exception("Edge already exists");
	            }
	            //removes the current edge from the list of edges for the starting vertice
	            if (prev == null) {
	            //if current edge is the first edge, must call setFisrtEdge to set the first
	            //edge to the next edge in the list
	                graphElements[oldIndexFrom].setFirstEdge(temp.getNext());
	            } 
	            else {
	            //setNext is called on the previous edge to set its next edge to the next edge
	            	prev.setNext(temp.getNext());
	            }
	            //update index of the vertice that the current edge starts from
	            temp.setIndexOfVertice(newIndexFrom);
	            //set the next edge of the current edge to the first edge of the vertice
	            //that the edge will be updated to start from
	            temp.setNext(graphElements[newIndexFrom].getFirstEdge());
	            //set the first edge of the vertice that the edge will be updated to start from
	            //to the current edge
	            graphElements[newIndexFrom].setFirstEdge(temp);
	            //stop the method
	            return;
	        }
	        //move to the next edge
	        prev = temp;
	        temp = temp.getNext();
	    }
	    throw new Exception("Edge does not exist");
	}
	            
	//depth	
	public void depthFirstSearch(T startElement, T endElement) throws Exception {
	    if (startElement == null || endElement == null) {
	        throw new Exception("Invalid input");
	    }
	    int startIndex = searchVertice(startElement);
	    int endIndex = searchVertice(endElement);
	    
	    if (startIndex < 0 || endIndex < 0) {
	        throw new Exception("One or both vertices are not in graph");
	    }
	    //create an array to store the visited vertices
	    boolean[] visited = new boolean[elementCounter];
	    //call a helper method with the start vertice index, end and the visited array
	    dfsHelper(startIndex, endIndex, visited);
	    //print a message if the dfs function does not find path between start and end
	    System.out.println("Path not found");
	}
	
	//create recursive helper method that performs dfs traversal,
	//which takes in the current vertice index, 
	//end vertice index and visited array
	private void dfsHelper(int current, int endIndex, boolean[] visited) {
	//set the current vertice as visited
	    visited[current] = true;
	    if (current == endIndex) {
	        System.out.println("Path found");
	        return;
	    }
	  //get the first edge of the current vertice
	    MyEdgeNode temp = graphElements[current].getFirstEdge();
	    while (temp != null) {
	        int neighborIndex = temp.getIndexOfVertice();
	        //call dfs function for each unvisited neighbor vertice
	        if (!visited[neighborIndex]) {
	            dfsHelper(neighborIndex, endIndex, visited);
	        }
	        //go to the next edge
	        temp = temp.getNext();
	    }
	}
	
	/*
	 * https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/
	 */
	
	//width
	public void breadthFirstSearch(T startElement, T endElement) throws Exception {
	    if (startElement == null || endElement == null) {
	        throw new Exception("Invalid input");
	    }
	    
	    int startIndex = searchVertice(startElement);
	    int endIndex = searchVertice(endElement);
	    
	    if (startIndex < 0 || endIndex < 0) {
	        throw new Exception("One or both vertices are not in graph");
	    }
	    //create a queue to store the vertices to be visited
	    Queue<Integer> queue = new LinkedList<>();
	    //create an array to store the visited vertices
	    boolean[] visited = new boolean[elementCounter];
	    //mark the starting vertex as visited and add it to the queue
	    visited[startIndex] = true;
	    queue.add(startIndex);
	    //while the queue is not empty, visit the next vertex in the queue
	    while (!queue.isEmpty()) {
	        int currentVertex = queue.poll();
	        //if the current vertex is the end vertex, stop the method
	        if (currentVertex == endIndex) {
	            return;
	        }
	        //iterate through the edges of the current vertex 
	        //and add any unvisited vertices to the queue
	        MyEdgeNode temp = graphElements[currentVertex].getFirstEdge();
	        while (temp != null) {
	            int nextVertex = temp.getIndexOfVertice();
	            if (!visited[nextVertex]) {
	                visited[nextVertex] = true;
	                queue.add(nextVertex);
	            }
	            temp = temp.getNext();
	        }
	    }
	    //if the end vertex was not found, throw an exception
	    throw new Exception("End vertex not found");
	}
	
	/*
	 * https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
	 */
	
	
	public MyGraph<T> minimumSpanningTree() throws Exception {
		if (isEmpty()) {
			throw new Exception("Graph is empty");
		}

		MyGraph<T> mst = new MyGraph<T>(elementCounter);

		// Initialize the visited array and the priority queue
		boolean[] visited = new boolean[elementCounter];
		PriorityQueue<MyEdgeNode> pq = new PriorityQueue<MyEdgeNode>();

		// Start with the first vertice
		visited[0] = true;
		MyVerticeNode<T> currentVertice = graphElements[0];

		// Add all edges of the first vertice to the priority queue
		MyEdgeNode currentEdge = currentVertice.getFirstEdge();
		while (currentEdge != null) {
			pq.add(currentEdge);
			currentEdge = currentEdge.getNext();
		}

		// Loop until all vertices are visited
		while (mst.howManyElements() < elementCounter) {
			// Get the edge with the smallest weight from the priority queue
			MyEdgeNode minEdge = pq.poll();
			int indexOfMinVertice = minEdge.getIndexOfVertice();

			// If the vertice of the minEdge is not visited, add it to the MST
			if (!visited[indexOfMinVertice]) {
				T minVerticeElement = (T) graphElements[indexOfMinVertice].getElement();
				mst.addVertice(minVerticeElement);
				mst.addEdge(currentVertice.getElement(), minVerticeElement, minEdge.getWeigth());

				// Mark the vertice as visited and add its edges to the priority queue
				visited[indexOfMinVertice] = true;
				currentVertice = graphElements[indexOfMinVertice];
				currentEdge = currentVertice.getFirstEdge();
				while (currentEdge != null) {
					pq.add(currentEdge);
					currentEdge = currentEdge.getNext();
				}
			}
		}

		return mst;
	}

	/*
	 * https://www.geeksforgeeks.org/prims-minimum-spanning-tree-mst-greedy-algo-5/
	 */
}

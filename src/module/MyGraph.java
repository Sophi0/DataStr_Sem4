package module;

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
	    
		/*
		 * This code checks if the edge already exists by iterating through the edges 
		 * of the elementFrom vertex and comparing the verticeIndex of each edge with 
		 * the index of the elementTo vertex. If a matching edge is found, an exception is thrown.
		 */
		MyEdgeNode object = graphElements[indexOfElementFrom].getFirstEdge();
	    while (object != null) {
	        if (object.getIndexOfVertice() == indexOfElementTo) {
	            throw new Exception("Edge already exists");
	        }
	        object = object.getNext();
	    }
	    
		MyEdgeNode newNode = new MyEdgeNode(indexOfElementTo, edgeWeigth);
		//if it is at first edge
		if(graphElements[indexOfElementFrom].getFirstEdge() == null) {
			graphElements[indexOfElementFrom].setFirstEdge(newNode);
		}
		else {
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
		// find the index of the vertice to be removed
		int indexToRemove = searchVertice(inputElement);

		if (indexToRemove < 0) {
			throw (new Exception("Vertice is not in graph")); 
		}
		// remove all edges that connect to the vertice to be removed
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
		// remove the vertice
		graphElements[indexToRemove] = null;
		elementCounter--;

		// shift all elements after the removed vertice to the left
		for (int i = indexToRemove; i < elementCounter; i++) {
			graphElements[i] = graphElements[i+1];
		}
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
	    int indexFrom = searchVertice(elementFrom);
	    int indexTo = searchVertice(elementTo);
	    
	    if (indexFrom < 0 || indexTo < 0) {
	        throw new Exception("One or both vertices are not in graph");
	    }
	    MyEdgeNode temp = graphElements[indexFrom].getFirstEdge();
	    MyEdgeNode prev = null;
	    
	    while (temp != null) {
	        if (temp.getIndexOfVertice() == indexTo) {
	            if (prev == null) {
	                graphElements[indexFrom].setFirstEdge(temp.getNext());
	            } else {
	                prev.setNext(temp.getNext());
	            }
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
	    int indexFrom = searchVertice(elementFrom);
	    int oldIndexTo = searchVertice(oldElementTo);
	    int newIndexTo = searchVertice(newElementTo);
	    
	    if (indexFrom < 0 || oldIndexTo < 0 || newIndexTo < 0) {
	        throw new Exception("One or more vertices are not in graph");
	    }
	    
	    MyEdgeNode temp = graphElements[indexFrom].getFirstEdge();
	    
	    while (temp != null) {
	        if (temp.getIndexOfVertice() == oldIndexTo) {
	            if (edgeExists(indexFrom, newIndexTo)) {
	                throw new Exception("Edge already exists");
	            }
	            temp.setIndexOfVertice(newIndexTo);
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
	    int oldIndexFrom = searchVertice(oldElementFrom);
	    int indexTo = searchVertice(elementTo);
	    int newIndexFrom = searchVertice(newElementFrom);
	    
	    if (oldIndexFrom < 0 || indexTo < 0 || newIndexFrom < 0) {
	        throw new Exception("One or more vertices are not in graph");
	    }
	    MyEdgeNode temp = graphElements[oldIndexFrom].getFirstEdge();
	    MyEdgeNode prev = null;
	    
	    while (temp != null) {
	        if (temp.getIndexOfVertice() == indexTo) {
	            if (edgeExists(newIndexFrom, indexTo)) {
	                throw new Exception("Edge already exists");
	            }
	            if (prev == null) {
	                graphElements[oldIndexFrom].setFirstEdge(temp.getNext());
	            } 
	            else {
	            	prev.setNext(temp.getNext());
	            }
	            temp.setIndexOfVertice(newIndexFrom);
	            temp.setNext(graphElements[newIndexFrom].getFirstEdge());
	            graphElements[newIndexFrom].setFirstEdge(temp);
	            return;
	        }
	        prev = temp;
	        temp = temp.getNext();
	    }
	    throw new Exception("Edge does not exist");
	}
	            
			
}

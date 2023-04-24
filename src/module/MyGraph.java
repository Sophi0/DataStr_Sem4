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
		
		//TODO add vertices if it is not found in graph
		if(indexOfElementFrom < 0 || indexOfElementTo < 0) {
			throw (new Exception("One or both vertices are not in graph"));
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
			System.out.println(graphElements[i].getElement() + " -->");
		//2.1.while loop for edges and print each edge of the specific vertice	
			MyEdgeNode tempEdgeNode = graphElements[i].getFirstEdge();
			
			while(tempEdgeNode != null) {
				T verticeTo = (T) graphElements[tempEdgeNode.getIndexOfVertice()].getElement();
				System.out.println(verticeTo + "( " + tempEdgeNode.getWeigth() + " km");
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
	
	//TODO
	//removeVertice
	//updateVertice
	//removeEdge
	//updateEdgeWeigth
	//updateEdgeByItsVertices
	//changeEdge
			
}

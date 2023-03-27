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
	
	//TODO isFull, isEmpty, howManyElements, increaseArray
	
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
	//addEdge
	//print
	//makeEmpty
	
	//TODO
	//removeVertice
	//removeEdge
	//changeEdge
			
}

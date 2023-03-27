package module;

public class MyVerticeNode <T>{
	private T element;
	private MyEdgeNode firstEdge = null;
	
	
	public T getElement() {
		return element;
	}
	
	public void setElement(T inputElement) {
		if(inputElement != null) {
			element = inputElement;
		}
		else {
			element = (T) new Object();
		}
	}
	
	public MyEdgeNode getFirstEdge() {
		return firstEdge;
	}
	
	public void setFirstEdge(MyEdgeNode firstEdge) {
		this.firstEdge = firstEdge;
	}
	
	//TODO constructors and toString
	
	public MyVerticeNode(T element) {
		setElement(element);
	}
	
	public String toString() {
		return "" + element;
	}
	
}

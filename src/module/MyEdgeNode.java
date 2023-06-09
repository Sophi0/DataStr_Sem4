package module;

public class MyEdgeNode{
	private int indexOfVertice;
	private float weigth;
	private MyEdgeNode next = null;
	
	
	public int getIndexOfVertice() {
		return indexOfVertice;
	}
	
	public void setIndexOfVertice(int inputIndexOfVertice) {
		if(inputIndexOfVertice >= 0) {
			indexOfVertice = inputIndexOfVertice;
		}
		else {
			indexOfVertice = -1;
		}
	}
	public float getWeigth() {
		return weigth;
	}
	
	public void setWeigth(float inputWeigth) {
		if(inputWeigth > 0 && inputWeigth < 43000) { //43000 ekvators
			weigth = inputWeigth;
		}
		else {
			weigth = 0;
		}
	}
	
	public MyEdgeNode getNext() {
		return next;
	}
	
	public void setNext(MyEdgeNode next) {
		this.next = next;
	}
	
	public MyEdgeNode(int indexOfVertice, float weigth) {
		setIndexOfVertice(indexOfVertice);
		setWeigth(weigth);
	}
	
	public String toString() {
		return "" + weigth;
	}
	
}


public class CQ {
	
	private Node[] elements;
	private int rear,front;
	
	private int number;
	
	public CQ(int capacity){
		rear = -1;
		front = 0;
		elements = new Node[capacity];
		number = 0;
	}
	
	public boolean isEmpty(){
		return elements[front] == null;
	}
	
	public boolean isFull(){
		return ((front == (rear + 1)%elements.length) && elements[front] != null && elements[rear] != null);
	}
	
	public Node peek(){
		if(!isEmpty()) return elements[front];
		else return null;
	}
	
	public void enqueue(Node item){
		if(!isFull()){
			rear = (rear + 1) % elements.length;
			elements[rear] = item;
			number++;
		}
	}
	
	
	
	public Node dequeue(){
		if(!isEmpty()){
			Node item = elements[front];
			elements[front] = null;
			front = (front + 1) % elements.length;
			number--;
			return item;
		}
		else return null;
	}
	
	
	
	public int size(){
		return number;
	}
	
	
}

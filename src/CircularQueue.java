

public class CircularQueue {

	private int rear,front,number;
	private Object[] elements;
	
	public CircularQueue(int capacity){
		
		elements = new Object[capacity];
		front = 0;
		rear = -1;
		number = 0;
		
	}
	
	public boolean isEmpty(){
		return number == 0;
	}
	
	public boolean isFull(){
		return elements.length == number;
	}
	
	public int size(){
		return number;
	}
	
	public void enqueue(Object o){
		if(!isFull()){
		rear = (rear+1)%elements.length;
		elements[rear] = o;
		number++;
		}
	}
	
	public Object dequeue(){
		if(!isEmpty()){
		Object data = elements[front];
		elements[front] = null;
		front = (front+1)%elements.length;
		number--;
		return data;
		}
		else{
			return null;
		}
	}
	
	public Object peek(){
		if(!isEmpty()){
			return elements[front];
		}
		else{ 
			System.out.println("Queue is empty!");
			return null;
		}
	}
}

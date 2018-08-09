

public class Stack {
	
	private int top;
	private Object[] elements;
	
	public Stack(int capacity){
		top = -1;
		elements = new Object[capacity];
	}
	
	

	public boolean isEmpty(){
		return top == -1;
	}
	
	public boolean isFull(){
		return elements.length - 1 == top;
	}
	
	public int size(){
		return top+1;
	}
	
	public void push(Object o){
		if(!isFull()){
		top++;
		elements[top] = o;
		}
	}
	
	public Object pop(){
		if(!isEmpty()){
		Object data = elements[top];
		elements[top] = null;
		top--;
		return data;
		}
		else return null;
	}
	
	public Object peek(){
		if(!isEmpty())
		return elements[top];
		else return null;
	}

}

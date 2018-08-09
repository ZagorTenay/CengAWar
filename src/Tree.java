
public class Tree {

	
	private int life,foodLimit,foodCreating;
	private String name;
	private int x,y;
	private Food food;
	private boolean team;
	private boolean invisible;
	private boolean death;
	
	public Tree(){
		death = false;
		invisible = false;
		team = true;
		food = new Food();
		x = 0;
		y = 0;
		name = "T";
		this.life = 50;
		this.foodLimit = 50;
		this.foodCreating = 5;
	}
	
	public void createFood(){
		if(food.getAmount()+foodCreating <= 50){
		food.setAmount(food.getAmount()+foodCreating);
		}
		else{
			food.setAmount(50);
		}
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isTeam() {
		return team;
	}

	public void setTeam(boolean team) {
		this.team = team;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public boolean isDeath() {
		return death;
	}

	public void setDeath(boolean death) {
		this.death = death;
	}
	
}

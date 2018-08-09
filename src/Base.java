
public class Base {

	private int life,repairCost,repairTime;
	private int x,y;
	private boolean state;
	private boolean team;
	private boolean invisible;
	private CengMAN enemy;
	
	public Base(){
		enemy = null;
		invisible = false;
		team = true;
		this.life = 1000;
		this.repairCost = 1;
		this.repairTime = 10;
		x = 0;
		y = 0;
		state = false;
	}

	public void repair(){
		boolean flag = false;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if(CengAWar.board[y+i][x+j] instanceof Food){
					Food food = (Food)CengAWar.board[y+i][x+j];
					if(food.getAmount() > repairTime){
						life += repairTime;
						food.setAmount(food.getAmount() - repairTime);
						flag = true;
						break;
					}
					else{
						life += food.getAmount();
						CengAWar.board[y+i][x+j] = " ";
						CengAWar.tempBoard[y+i][x+j] = " ";
						CengAWar.Pboard[y+i][x+j] = 0;
						food = null;
						flag = true;
						break;
					}
				}
			}
			if(flag){
				flag = false;
				break;
			}
		}
		if(!flag) state = false;
		
	}
	
	public void underAttack(){
		boolean flag = false;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if(CengAWar.board[y+i][x+j] instanceof CengMAN){
					enemy = (CengMAN) CengAWar.board[y+i][x+j];
					flag = true;
					break;
				}
				else{
					enemy = null;
				}
			}
			if(flag){
				flag = false;
				break;
			}
		}
	}
	
	
	public CengMAN getEnemy() {
		return enemy;
	}

	public void setEnemy(CengMAN enemy) {
		this.enemy = enemy;
	}

	public boolean isTeam() {
		return team;
	}

	public void setTeam(boolean team) {
		this.team = team;
	}

	public CengMAN createCengman(){
		return new CengMAN();
	}
	
	public Tree createTree(){
		return new Tree();
	}
	
	public Cement createCement(){
		return new Cement();
	}
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getRepairCost() {
		return repairCost;
	}

	public void setRepairCost(int repairCost) {
		this.repairCost = repairCost;
	}

	public int getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(int repairTime) {
		this.repairTime = repairTime;
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

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	
	
}

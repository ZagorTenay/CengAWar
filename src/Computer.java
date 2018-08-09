import java.util.*;
public class Computer {

	Random rnd = new Random();
	private CengMAN[] cengMans;
	private Tree[] trees;
	private CengMAN activeCengMan;
	private int countCeng,countTree;
	private Base base;
	private CircularQueue productionQueue;
	private long time;
	private boolean isProducible;
	private int proX,proY;
	private int lastBaseLife;
	private boolean baseTarget; // Base saldýrý altýnda ise

	public Computer(){
		baseTarget = false;
		lastBaseLife = 0;
		proX = 0;
		proY = 0;
		activeCengMan = null;
		base = new Base();
		cengMans = new CengMAN[20];
		trees = new Tree[20];
		productionQueue = new CircularQueue(10);
		countCeng = 0;
		countTree = 0;
		time = 0;
		isProducible = false;
		createTree();
		createTree();
		createTree();
		generateCengMans(); //baþlangýç cengmanleri
		generateBase();
		collectFood();
	}

	public void routine(){

		if(base.getLife() < lastBaseLife){
			if(!baseTarget){
				baseUnderAttack();
			}
			base.setState(true);

		}
		else{
			baseTarget = false;
		}

		for (int i = 0; i < countCeng; i++) { // CengMan Routines
			if(cengMans[i] != null){

				
				if(cengMans[i].getState() == 1){
					cengMans[i].go();
				}
				else if(cengMans[i].getState() == 2){
					cengMans[i].extract(cengMans[i].getGox(), cengMans[i].getGoy());
					Food food = (Food) cengMans[i].getBagPack().peek();
					if(food.getAmount()>=50){
						cengMans[i].setState(1);
						cengMans[i].setGox(base.getX());
						cengMans[i].setGoy(base.getY());
					}
				}
				else if(cengMans[i].getState() == 5){
					cengMans[i].fight();
				}
				else if(cengMans[i].getState() == 51){
					cengMans[i].fightTWB();
				}
				else if(cengMans[i].getState() == 31){ // feed from bag
					cengMans[i].feed();
				}
				if(cengMans[i].isDeath()) cengMans[i] = null;
			}
		}

		for (int i = 0; i < countTree; i++) {
			if(trees[i] != null){
				trees[i].createFood();
				if(trees[i].isDeath()) trees[i] = null;
			}
		}

		if(base.isState() == false)
			productUnit();
		else 
			base.repair();

		lastBaseLife = base.getLife();// Base in en son lifeýný tut
		base.underAttack();

	}


	public void baseUnderAttack(){
		base.setState(true);

		for (int i = 0; i < countCeng; i++) {
			if(cengMans[i] != null){

				if((cengMans[i].getState() != 5 && cengMans[i].getState() != 51) && cengMans[i].getTarget() == null){ // savaþmýyorsa ve bi hedefe gitmiyorsa

					if(base.getEnemy() != null){
						cengMans[i].setTarget(base.getEnemy());
						cengMans[i].setGox(base.getEnemy().getX());
						cengMans[i].setGoy(base.getEnemy().getY());
						cengMans[i].setState(1);
						baseTarget = true;
					}
					break;
				}
			}
		}

	}

	public void collectFood(){
		for (int i = 0; i < countCeng; i++) {
			if(cengMans[i] != null){
				cengMans[i].setGox(trees[i].getX());
				cengMans[i].setGoy(trees[i].getY());
				cengMans[i].setState(1);
			}
		}
	}

	public void generateBase(){
		int x1 = 38;
		int y1 = 15;
		//do{
			//x1 = rnd.nextInt(24)+24;
			//y1 = rnd.nextInt(9) + 9;
		//}while(!CengAWar.board[y1][x1].toString().equals(" "));
		CengAWar.board[y1][x1] = base;
		CengAWar.tempBoard[y1][x1] = "B";
		CengAWar.Pboard[y1][x1] = 1;
		base.setX(x1);
		base.setY(y1);
		base.setTeam(false);
	}

	public void generateCengMans(){
		for (int i = 0; i < 3; i++) {
			cengMans[countCeng] = new CengMAN();
			int x1 = 0;
			int y1 = 0;
			do{
				x1 = rnd.nextInt(24)+24;
				y1 = rnd.nextInt(9)+9;
			}while(!CengAWar.board[y1][x1].toString().equals(" "));
			cengMans[countCeng].setX(x1);
			cengMans[countCeng].setY(y1);
			cengMans[countCeng].setTeam(false);
			cengMans[countCeng].setId(countCeng+1);
			CengAWar.board[y1][x1] = cengMans[countCeng];
			CengAWar.tempBoard[y1][x1] = cengMans[countCeng].getId();
			CengAWar.Pboard[y1][x1] = 1;

			countCeng++;
		}
	}

	public boolean coorOfBaseProduction(){
		int x1 = base.getX();
		int y1 = base.getY();
		boolean flag = false;
		for (int i = -1; i <=1 ; i++) {
			for (int j = -1; j <=1 ; j++) {
				if(CengAWar.Pboard[y1+i][x1+j] != 1){
					proX= x1+j;
					proY= y1+i;
					flag = true;
					break;
				}
			}
			if(flag) break;
		}
		return flag;
	}

	public void addProduction(String s){
		productionQueue.enqueue(s);
	}

	public boolean foodControl(int amount){ // amount = gerekli food miktarý // Üretimler için yeterli food kontrolü
		int x = base.getX();
		int y = base.getY();
		int need = 0;

		for (int i = -1; i <= 1; i++) {// etrafýndaki toplam food miktarýný bulma
			for (int j = -1; j <= 1; j++) {
				if(CengAWar.board[y+i][x+j] instanceof Food){
					Food food = (Food)CengAWar.board[y+i][x+j];
					need += food.getAmount();
				}
			}
		}

		if(need >= amount){ // eðer yeterli miktarda food varsa
			need = 0;
			boolean flag = false;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if(CengAWar.board[y+i][x+j] instanceof Food){
						Food food = (Food)CengAWar.board[y+i][x+j];
						if(need + food.getAmount() < amount){
							need += food.getAmount();
							CengAWar.board[y+i][x+j] = " ";
							CengAWar.tempBoard[y+i][x+j] = " ";
							CengAWar.Pboard[y+i][x+j] = 0;
						}
						else{
							if(food.getAmount() > amount - need){
								need += amount - need;
								food.setAmount(food.getAmount() - (amount - need));
								flag = true;
								break;
							}
							else{
								need += food.getAmount();
								CengAWar.board[y+i][x+j] = " ";
								CengAWar.tempBoard[y+i][x+j] = " ";
								CengAWar.Pboard[y+i][x+j] = 0;
							}

						}
					}
				}
				if(flag) break;
			}
			return true;
		}
		else return false;
	}

	public void productUnit(){
		if(!productionQueue.isEmpty()){
			if(productionQueue.peek().toString().equals("T")){ // Tree
				if(!isProducible && foodControl(50)){
					time = System.currentTimeMillis();
					isProducible = true;
				}
				if(isProducible && time+1000 <= System.currentTimeMillis()){
					createTree();
					isProducible = false;
					productionQueue.dequeue();

				}

			}
			else if(productionQueue.peek().toString().equals("C")){ // Cengman
				if(!isProducible && foodControl(50)){
					time = System.currentTimeMillis();
					isProducible = true;
				}
				if(isProducible && time+8000 <= System.currentTimeMillis()){
					if(coorOfBaseProduction() == true){
						createCengMan();
						isProducible = false;
						productionQueue.dequeue();
					}
				}

			}
			else if(productionQueue.peek().toString().equals("W")){ // Cement
				if(!isProducible && foodControl(100)){
					time = System.currentTimeMillis();
					isProducible = true;
				}
				if(isProducible && time+1000 <= System.currentTimeMillis()){
					if(coorOfBaseProduction() == true){
						isProducible = false;
						CengAWar.board[proY][proX] = new Cement();
						CengAWar.Pboard[proY][proX] = 1;
						CengAWar.tempBoard[proY][proX] = "O";
						productionQueue.dequeue();
					}
				}

			}

		}

	}

	public void createCengMan(){
		CengMAN ceng = base.createCengman();
		cengMans[countCeng] = ceng;
		cengMans[countCeng].setId(countCeng+1);
		countCeng++;
		cengMans[countCeng-1].setY(proY);
		cengMans[countCeng-1].setGoy(proY);
		cengMans[countCeng-1].setTeam(false);
		CengAWar.board[proY][proX] = cengMans[countCeng-1];
		CengAWar.Pboard[proY][proX] = 1;
		CengAWar.tempBoard[proY][proX] = cengMans[countCeng-1].getId();
	}

	public void createTree(){
		trees[countTree] = base.createTree();
		countTree++;
		int x1 = 0;
		int y1 = 0;
		do{
			x1 = rnd.nextInt(24) + 24;
			y1 = rnd.nextInt(9) + 9;
		}while(!CengAWar.board[y1][x1].toString().equals(" "));
		CengAWar.board[y1][x1] = trees[countTree-1];
		CengAWar.Pboard[y1][x1] = 1;
		CengAWar.tempBoard[y1][x1] = "T";
		trees[countTree-1].setX(x1);
		trees[countTree-1].setY(y1);
		trees[countTree-1].setTeam(false);
	}

	public CengMAN getActiveCengMan() {
		return activeCengMan;
	}

	public void setActiveCengMan(CengMAN activeCengMan) {
		this.activeCengMan = activeCengMan;
	}

	public Base getBase() {
		return base;
	}

	public void setBase(Base base) {
		this.base = base;
	}


	public CengMAN[] getCengMans() {
		return cengMans;
	}

	public void setCengMans(CengMAN[] cengMans) {
		this.cengMans = cengMans;
	}

	public Tree[] getTrees() {
		return trees;
	}

	public void setTrees(Tree[] trees) {
		this.trees = trees;
	}

	public int getCountCeng() {
		return countCeng;
	}

	public void setCountCeng(int countCeng) {
		this.countCeng = countCeng;
	}

	public int getCountTree() {
		return countTree;
	}

	public void setCountTree(int countTree) {
		this.countTree = countTree;
	}



}

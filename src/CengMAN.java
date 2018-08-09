import java.util.*;
public class CengMAN {

	Random rnd = new Random();
	private int level,life,attackDamage,attackSpeed,wiewRange,movementSpeed;
	private int id;
	private String name;
	private Stack bagPack;
	private int x,y;
	//private Object[][] tempBoard;
	//private int[][] Pboard;
	private int[][] tPboard;
	boolean team;
	private CengMAN enemy;
	private int gox,goy;
	private int state;
	private CengMAN target;
	private Tree targetTree;
	private Wall targetWall;
	private Base targetBase;
	private boolean death;
	private int feedx,feedy;

	public CengMAN(){
		death = false;
		target = null;
		targetTree = null;
		targetWall = null;
		targetBase = null;
		enemy = null;
		team = true;
		tPboard = new int[19][49];
		x = 0;
		y = 0;
		gox = 0;
		goy = 0;

		state = 0; // 5-Fight
		id = 0;
		name = "CengMAN";
		this.life = 100;
		this.level = 1;
		this.attackDamage = level * 10;
		this.attackSpeed = 1;
		this.movementSpeed = 1;
		this.wiewRange = 5;
		bagPack = new Stack(8);

	}

	public void view(){

		for (int i = -5; i <= 5; i++) {
			for (int j = -5; j <= 5; j++) {
				if(x+j >= 0 && x+j<=48 && y+i >= 0 && y+i <= 18){
					if(CengAWar.board[y+i][x+j] instanceof Tree){
						Tree tree = (Tree) CengAWar.board[y+i][x+j];
						tree.setInvisible(true);
					}
					else if(CengAWar.board[y+i][x+j] instanceof Base){
						Base base = (Base) CengAWar.board[y+i][x+j];
						base.setInvisible(true);
					}
					CengAWar.rangeBoard[y+i][x+j] = true;
				}
			}
			
		}

	}


	public void extract(int x, int y) {

		if (!(bagPack.isEmpty()) && CengAWar.tempBoard[y][x].equals(" ")) {
			if ((y-1 == this.y && x == this.x) || (y+1 == this.y && x == this.x)
					|| (y-1 == this.y && x-1 == this.x)
					|| (y+1 == this.y && x+1 == this.x)
					|| (y == this.y && x-1 == this.x)
					|| (y == this.y && x+1 == this.x)
					|| (y-1 == this.y && x+1 == this.x)
					|| (y+1 == this.y && x-1 == this.x)) {
				if (bagPack.peek() instanceof Cement) {
					CengAWar.board[y][x] = bagPack.pop();
					;
					CengAWar.tempBoard[y][x] = "O";
					CengAWar.Pboard[y][x] = 1;
				} else if (bagPack.peek() instanceof Food) {
					CengAWar.board[y][x] = bagPack.pop();
					;
					CengAWar.tempBoard[y][x] = "F";
					CengAWar.Pboard[y][x] = 1;
				} else {
					bagPack.pop();
				}

			}
		} else if ((CengAWar.board[y][x] instanceof Food || CengAWar.board[y][x] instanceof Cement)
				&& bagPack.isFull() == false) {
			if ((y-1 == this.y && x == this.x) || (y+1 == this.y && x == this.x)
					|| (y-1 == this.y && x-1 == this.x)
					|| (y+1 == this.y && x+1 == this.x)
					|| (y == this.y && x-1 == this.x)
					|| (y == this.y && x+1 == this.x)
					|| (y-1 == this.y && x+1 == this.x)
					|| (y+1 == this.y && x-1 == this.x)) {

				bagPack.push(CengAWar.board[y][x]);
				CengAWar.board[y][x] = " ";
				CengAWar.tempBoard[y][x] = " ";
				CengAWar.Pboard[y][x] = 0;
			}
		} else if (CengAWar.board[y][x] instanceof Tree && bagPack.isFull() == false) {
			if ((y-1 == this.y && x == this.x) || (y+1 == this.y && x == this.x)
					|| (y-1 == this.y && x-1 == this.x)
					|| (y+1 == this.y && x+1 == this.x)
					|| (y == this.y && x-1 == this.x)
					|| (y == this.y && x+1 == this.x)
					|| (y-1 == this.y && x+1 == this.x)
					|| (y+1 == this.y && x-1 == this.x)) {

				Tree tree = (Tree) CengAWar.board[y][x];

				if (tree.getFood().getAmount() > 0) {

					Food food1 = new Food();
					food1.setAmount(tree.getFood().getAmount());
					tree.getFood().setAmount(0);

					if (bagPack.peek() instanceof Food) {

						Food food2 = (Food) bagPack.peek();

						if (food2.getAmount() < 50) {

							food2.setAmount(food2.getAmount() + food1.getAmount());

							if (food2.getAmount() > 50) {
								food2.setAmount(50);
								food1.setAmount(food2.getAmount() - 50);

								if(bagPack.isFull() == false){
									bagPack.push(food1);}
							}
						}
						else if(bagPack.isFull() == false){
							bagPack.push(food1);
						}

					} else if (bagPack.isFull() == false) {
						bagPack.push(food1);
					}

				}
			}
		}

		tilda();

	}

	public void tilda() {
		Stack tempbag = new Stack(8);
		boolean flag = false;

		while (!bagPack.isEmpty()) {
			if (bagPack.peek() instanceof Cement) {
				flag = true;
				tempbag.push(bagPack.pop());
			} else if (bagPack.peek() instanceof Food) {

				if (flag) {
					bagPack.pop();
					tempbag.push("~");
				} else {
					tempbag.push(bagPack.pop());
				}
			} else {
				tempbag.push(bagPack.pop());
			}

		}

		while (!tempbag.isEmpty()) {
			bagPack.push(tempbag.pop());
		}

	}

	public void wall(int x, int y) {

		if (CengAWar.board[y][x] instanceof Cement && CengAWar.board[y - 1][x] == Player.activeCengMan || CengAWar.board[y + 1][x] == Player.activeCengMan
				|| CengAWar.board[y - 1][x - 1] == Player.activeCengMan
				|| CengAWar.board[y + 1][x + 1] == Player.activeCengMan
				|| CengAWar.board[y][x - 1] == Player.activeCengMan 
				|| CengAWar.board[y][x + 1] == Player.activeCengMan
				|| CengAWar.board[y - 1][x + 1] == Player.activeCengMan
				|| CengAWar.board[y + 1][x - 1] == Player.activeCengMan) {

			CengAWar.board[y][x] = new Wall();
			CengAWar.tempBoard[y][x] = "X";
			CengAWar.Pboard[y][x] = 1;

		}

	}

	public void tempPboard(){
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 49; j++) {
				tPboard[i][j] = CengAWar.Pboard[i][j]; // daha önceki kontrol ettiði yerleri tekrar 0 lamak için
			}
		}
	}
	
	public void feed(){
		if(state == 3){ // yerden food yemek için
			int x = feedx;
			int y = feedy;
			
			boolean flag = false;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if(x+j == this.x && y+i == this.y){ // týklanan food active cengmanin yanýnda mý kontrol etmek için
						flag = true;
						break;
					}
				}
				if(flag) break;
			}
			
			if(flag){ // cengmanin yanýnda ise
				Food food = (Food) CengAWar.board[y][x];
				life += food.getAmount();
				food.setAmount(0);
				CengAWar.board[y][x] = " ";
				CengAWar.tempBoard[y][x] = " ";
				CengAWar.Pboard[y][x] = 0;
				
				if(life <200){
					level = 1;
					attackDamage = level * 10;
				}
				else{
					int lvl = (int) Math.floor(life/100);
					level = lvl;
					attackDamage = level * 10;
				}
				
			}
			
		}
		else if(state == 31){ // çantadan food yemek için
			
			if(!bagPack.isEmpty()){
				if(bagPack.peek() instanceof Food){
					Food food = (Food) bagPack.pop();
					
					life += food.getAmount();
					food.setAmount(0);
					
					if(life <200){
						level = 1;
						attackDamage = level * 10;
					}
					else{
						int lvl = (int) Math.floor(life/100);
						level = lvl;
						attackDamage = level * 10;
					}
					
					if(!(bagPack.peek() instanceof Food)){
						state = -1;
					}
				}
				else{
					state = -1;
				}
			}
			else state = -1;
		}
	}

	public Stack pathFind(){
		CQ goQ = new CQ(1000);
		Stack goStack = new Stack(100);
		Node begining = new Node(x,y); // kendi bulunduðu nokta
		tempPboard();
		tPboard[y][x] = 2; 
		goQ.enqueue(begining);
		boolean isFind = false; //hedefe ulaþma durumunu gösteren boolean
		Node temp = null; // bulduðunda tutmak için temp Node

		while(!isFind){
			int x = goQ.peek().getX(); // kuyruktaki her eleman için x,y alma
			int y = goQ.peek().getY();
			if(target != null){
				gox = target.getX(); // Hareket halindeki bir cengman hedefse
				goy = target.getY();
			}

			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if(i != j && i != -j && tPboard[y+i][x+j] == 0){ // 4 tarafýna bakmak için ve boþsa
						Node newnode = new Node(x+j,y+i);
						newnode.setParent(goQ.peek());
						newnode.setDistance(goQ.peek().getDistance()+1);
						goQ.enqueue(newnode);
						tPboard[y+i][x+j] = 2;
						if(gox == x+j && goy == y+i){ // hedef boþluk ise ve hedefe ulaþýldýysa
							isFind = true;
							temp = newnode;
							break;
						}
					}
					else if(i != j && i != -j && tPboard[y+i][x+j] == 1){
						if(gox == x+j && goy == y+i){ // hedef herhangi bi nesne ise ve istenilen hedefe ulaþýldýysa
							isFind = true;
							temp = goQ.peek();
							break;
						}
					}
				}
				if(isFind) break;
			}
			goQ.enqueue(goQ.dequeue());
			if(isFind) break;

		}

		while(temp.getParent() != null){ //yol tespit edildikten sonra ilk gidilecek noktayý almak için stack e atýyoruz
			goStack.push(temp);
			int x = temp.getX();
			int y = temp.getY();
			CengAWar.tempBoard[y][x] = "."; // gidilecek yolu göstermek için 
			temp = temp.getParent();
		}

		return goStack;
	}

	public void go(){
		Stack temp = pathFind();
		Node a = (Node)temp.peek();
		if(!temp.isEmpty()){ // gidilecek yol var ise 
			int x = a.getX();
			int y = a.getY();

			CengAWar.tempBoard[y][x] = id;
			CengAWar.tempBoard[this.y][this.x] = " ";
			CengAWar.board[y][x] = CengAWar.board[this.y][this.x];
			CengAWar.board[this.y][this.x] = null;
			CengAWar.Pboard[y][x] = 1;
			CengAWar.Pboard[this.y][this.x] = 0;

			setX(x);
			setY(y);
			temp.pop();
		}

		if(gox == x && goy == y) state = -1; // hedef boþluk ise ve ulaþýldýysa 
		else if(CengAWar.board[goy][gox] instanceof Tree && temp.isEmpty()){ //hedef tree ise ve ulaþýldýysa 
			Tree tree = (Tree) CengAWar.board[goy][gox];
			if(tree.isTeam() == team) // eðer bizim agacýmýzsa
				state = 0;
			else{ // düþman agacý ise
				targetTree = (Tree) CengAWar.board[goy][gox];
				state = 51;
			}
			if(!team){ //yapay zeka için
				state = 2;
			}
		}
		else if(CengAWar.board[goy][gox] instanceof Wall && temp.isEmpty()){ // hedef duvar ise ve ulaþýldýysa
			targetWall = (Wall) CengAWar.board[goy][gox];
			state = 51;
		}
		else if(CengAWar.board[goy][gox] instanceof Base && temp.isEmpty()){ // hedef base ise ve ulaþýldýysa
			Base base = (Base) CengAWar.board[goy][gox];
			if(base.isTeam() == team){ // bizim base imiz ise
				state = -1;
			}
			else{ // düþman base i ise
				targetBase = (Base) CengAWar.board[goy][gox];
				state = 51;
			}
			if(!team){ // yapay zeka base in yanýna food býrakýp boþ bi yere gitsin
				boolean flag = false;//fordan cýkmak için
				int x = base.getX();
				int y = base.getY();
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if(CengAWar.tempBoard[y+i][x+j].equals(" ")){
							extract(x+j,y+i);
							flag = true;
							break;
						}
					}
					if(flag) break;
				}
				state = 1;
				int x1 = 0;
				int y1 = 0;
				do{
					x1 = rnd.nextInt(24)+24;
					y1 = rnd.nextInt(9) + 9;
				}while(!CengAWar.tempBoard[y1][x1].toString().equals(" "));
				gox = x1;
				goy = y1;
			}
		}
		else if((CengAWar.board[goy][gox] instanceof Cement || CengAWar.board[goy][gox] instanceof Food) && temp.isEmpty()){ // hedef food veya cement ise ve ulaþýldýysa
			state = 2;
		}
		else if(CengAWar.board[goy][gox] instanceof CengMAN && temp.isEmpty()){ // hedef CengMans ise ve ulaþýldýysa
			state = 5;
		}

	}

	public void fight(){

		boolean isEnemy = false; // yanýnda düþman olup olmadýðýný belirlemek etmek için boolean

		CengMAN ceng = null;
		if(CengAWar.board[y-1][x] instanceof CengMAN){
			isEnemy = true;
			ceng = (CengMAN) CengAWar.board[y-1][x];
			enemy = ceng;
			target = null;
		}
		else if(CengAWar.board[y][x-1] instanceof CengMAN){
			isEnemy = true;
			ceng = (CengMAN) CengAWar.board[y][x-1];
			enemy = ceng;
			target = null;
		}
		else if(CengAWar.board[y][x+1] instanceof CengMAN){
			isEnemy = true;
			ceng = (CengMAN) CengAWar.board[y][x+1];
			enemy = ceng;
			target = null;
		}
		else if(CengAWar.board[y+1][x] instanceof CengMAN){
			isEnemy = true;
			ceng = (CengMAN) CengAWar.board[y+1][x];
			enemy = ceng;
			target = null;
		}
		else{
			enemy = null;
		}

		if(enemy != null){ // düþman var ise yanýnda


			enemy.setLife(enemy.getLife() - attackDamage);
			if(enemy.getLife() <= 0){
				enemy.setState(0);
				state = -1;
				int x = enemy.getX();
				int y = enemy.getY();
				CengAWar.board[y][x] = " ";
				CengAWar.tempBoard[y][x] = " ";
				CengAWar.Pboard[y][x] = 0;
				enemy.setDeath(true);
				enemy = null;
			}
			else if(enemy.getLife() <200){
				enemy.setLevel(1);
				enemy.setAttackDamage(enemy.getLevel()*10);
			}
			else{
				int lvl = (int) Math.floor(enemy.getLife()/100);
				enemy.setLevel(lvl);
				enemy.setAttackDamage(enemy.getLevel()*10);
			}

		}
		else if(!isEnemy){
			target = enemy;
			enemy = null;
			if(state == 5) state = -1;
		}
	}

	public void fightTWB(){  // Cengman harici nesnelere saldýrmak için
		if(targetTree != null){ // aðaca saldýrý
			if(targetTree.getLife()> attackDamage){ 
				targetTree.setLife(targetTree.getLife() - attackDamage);
			}
			else{
				targetTree.setLife(targetTree.getLife() - attackDamage);
				int x = targetTree.getX();
				int y = targetTree.getY();
				CengAWar.board[y][x] = " ";
				CengAWar.tempBoard[y][x] = " ";
				CengAWar.Pboard[y][x] = 0;
				targetTree.setDeath(true);
				targetTree = null;
				state = -1;
			}
		}
		else if(targetWall != null){ // duvara saldýrý
			if(targetWall.getLife() > attackDamage){
				targetWall.setLife(targetWall.getLife() - attackDamage);
			}
			else{
				targetWall.setLife(targetWall.getLife() - attackDamage);
				int x = gox;
				int y = goy;
				CengAWar.board[y][x] = " ";
				CengAWar.tempBoard[y][x] = " ";
				CengAWar.Pboard[y][x] = 0;
				targetWall = null;
				state = -1;
			}
		}
		else if(targetBase != null){ // base e saldýrý
			if(targetBase.getLife() > attackDamage){
				targetBase.setLife(targetBase.getLife() - attackDamage);
			}
			else{
				targetBase.setLife(targetBase.getLife() - attackDamage);
				int x = gox;
				int y = goy;
				CengAWar.board[y][x] = null;
				CengAWar.tempBoard[y][x] = " ";
				CengAWar.Pboard[y][x] = 0;
				targetBase = null;
				state = -1;
			}
		}

	}
	
	public int distancetoUnit(int x1, int y1){
		
		CQ goQ = new CQ(1000);
		Node begining = new Node(x,y); // kendi bulunduðu nokta
		tempPboard();
		tPboard[y][x] = 2;
		goQ.enqueue(begining);
		boolean isFind = false; //hedefe ulaþma durumunu gösteren boolean
		Node temp = null; // bulduðunda tutmak için temp Node

		while(!isFind){
			int x = goQ.peek().getX(); // kuyruktaki her eleman için x,y alma
			int y = goQ.peek().getY();
			

			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if(i != j && i != -j && tPboard[y+i][x+j] == 0){ // 4 tarafýna bakmak için ve boþsa
						Node newnode = new Node(x+j,y+i);
						newnode.setParent(goQ.peek());
						newnode.setDistance(goQ.peek().getDistance()+1);
						goQ.enqueue(newnode);
						tPboard[y+i][x+j] = 2;
						if(x1 == x+j && y1 == y+i){ // hedef boþluk ise ve hedefe ulaþýldýysa
							isFind = true;
							temp = newnode;
							break;
						}
					}
					else if(i != j && i != -j && tPboard[y+i][x+j] == 1){
						if(x1 == x+j && y1 == y+i){ // hedef herhangi bi nesne ise ve istenilen hedefe ulaþýldýysa
							isFind = true;
							temp = goQ.peek();
							break;
						}
					}
				}
				if(isFind) break;
			}
			goQ.enqueue(goQ.dequeue());
			if(isFind) break;

		}
		
		int distance = 0;
		
		while(temp.getParent() != null){
			
			distance++;
			temp = temp.getParent();
		}
		return distance;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public int getWiewRange() {
		return wiewRange;
	}

	public void setWiewRange(int wiewRange) {
		this.wiewRange = wiewRange;
	}

	public int getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stack getBagPack() {
		return bagPack;
	}

	public void setBagPack(Stack bagPack) {
		this.bagPack = bagPack;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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


	public int getGox() {
		return gox;
	}

	public void setGox(int gox) {
		this.gox = gox;
	}

	public int getGoy() {
		return goy;
	}

	public void setGoy(int goy) {
		this.goy = goy;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isTeam() {
		return team;
	}

	public void setTeam(boolean team) {
		this.team = team;
	}



	public CengMAN getTarget() {
		return target;
	}



	public void setTarget(CengMAN target) {
		this.target = target;
	}



	public Tree getTargetTree() {
		return targetTree;
	}



	public void setTargetTree(Tree targetTree) {
		this.targetTree = targetTree;
	}



	public Wall getTargetWall() {
		return targetWall;
	}



	public void setTargetWall(Wall targetWall) {
		this.targetWall = targetWall;
	}



	public CengMAN getEnemy() {
		return enemy;
	}



	public void setEnemy(CengMAN enemy) {
		this.enemy = enemy;
	}



	public Base getTargetBase() {
		return targetBase;
	}



	public void setTargetBase(Base targetBase) {
		this.targetBase = targetBase;
	}

	public boolean isDeath() {
		return death;
	}

	public void setDeath(boolean death) {
		this.death = death;
	}

	public int getFeedx() {
		return feedx;
	}

	public void setFeedx(int feedx) {
		this.feedx = feedx;
	}

	public int getFeedy() {
		return feedy;
	}

	public void setFeedy(int feedy) {
		this.feedy = feedy;
	}



}

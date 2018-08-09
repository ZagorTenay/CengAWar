import enigma.console.TextAttributes;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import java.io.*;
import java.util.*;
import java.awt.Color;
import java.awt.color.*;
import java.awt.font.TextAttribute;
public class CengAWar {

	public enigma.console.Console cn = Enigma.getConsole("Ceng-A-War",120,30,true);
	public TextMouseListener tmlis; 

	// ------ Standard variables for mouse and keyboard ------
	public int mousepr;          // mouse pressed?
	public int mousex, mousey;   // mouse text coords.
	// ----------------------------------------------------

	Random rnd = new Random();
	private Player player;
	private Computer computer;
	public static Object[][] board;
	public static Object[][] tempBoard;
	public static int[][] Pboard;
	public static boolean[][] rangeBoard;
	private static int time;
	private boolean visible;

	public CengAWar() throws Exception{
		visible = false;
		fileRead();
		player = new Player();
		computer = new Computer();
		
		
		

		// Mouse Listener
		tmlis=new TextMouseListener() {
			public void mouseClicked(TextMouseEvent arg0) {}
			public void mousePressed(TextMouseEvent arg0) {
				if(mousepr==0) {
					mousepr=1;
					mousex=arg0.getX();
					mousey=arg0.getY();
					command(mousex,mousey);
					mousepr=0;

				}
			}
			public void mouseReleased(TextMouseEvent arg0) {}
		};
		cn.getTextWindow().addTextMouseListener(tmlis);
		// Mouse Listener
		

	}



	public void command(int x,int y){

		cn.getTextWindow().setCursorPosition(54,23);
		cn.getTextWindow().output("                            ");

		if(x>=1 && x<= 47 && y>=1 && y<=17){ // Actions on Board

			if(board[y][x] instanceof CengMAN){ // Select a Cengman
				CengMAN a = (CengMAN) board[y][x];
				if(a.isTeam() == true){
					CengMAN ceng = (CengMAN) board[y][x];
					player.setActiveCeng(ceng);
				}
			}
			if(player.getActiveCeng() != null){ // Cengman operations

				if(player.getActiveCeng().getState() == -1 || player.getActiveCeng().getState() == 1){ // GO
					if(tempBoard[y][x].equals(" ") || board[y][x] instanceof Tree 
							|| board[y][x] instanceof Wall || board[y][x] instanceof Base 
							|| board[y][x] instanceof Cement 
							|| board[y][x] instanceof Food){
						player.getActiveCeng().setGox(x);
						player.getActiveCeng().setGoy(y);
						player.getActiveCeng().setState(1);
						player.getActiveCeng().setTarget(null);
					}
					else if(board[y][x] instanceof CengMAN){
						CengMAN temp = (CengMAN) board[y][x];
						if(temp.isTeam() == false){ // düþman cengmane týklandý ise
							player.getActiveCeng().setGox(temp.getX());
							player.getActiveCeng().setGoy(temp.getY());
							player.getActiveCeng().setState(1);
							player.getActiveCeng().setTarget(temp);
						}
					}
				}
				else if(player.getActiveCeng().getState() == 2){ // EXTRACT
					player.getActiveCeng().extract(x, y);
				}
				else if(player.getActiveCeng().getState() == 3){ // FEED from ground
					player.getActiveCeng().feed();
					player.getActiveCeng().setFeedx(x);
					player.getActiveCeng().setFeedy(y);
				}
				else if(player.getActiveCeng().getState() == 4){ // WALL
					player.getActiveCeng().wall(x, y);
				}
				else if(player.getActiveCeng().getState() == 0){
					if(board[y][x] instanceof Tree){
						Tree tree = (Tree) board[y][x];
						cn.getTextWindow().setCursorPosition(54,23);
						cn.getTextWindow().output("Tree --> Food Amount: ");
						System.out.print(tree.getFood().getAmount());
					}
				}

			}
		}
		else{ // Base Operations and CengMan Modes
			if(x == 54 && y == 3){ // Create Tree
				player.addProduction("T");
			}
			else if(x == 60 && y == 3){ // Create CengMan
				player.addProduction("C");
			}
			else if(x == 66 && y == 3){ // Create Cement(Wall)
				player.addProduction("W");
			}
			else if(x == 72 && y == 3){ // Repair
				if(player.getBase().isState() == false)
					player.getBase().setState(true);
				else
					player.getBase().setState(false);
			}
			else if(player.getActiveCeng() != null){ // Cengman Actions
				if(x == 54 && y == 20){ // GO
					player.getActiveCeng().setState(-1);
				}
				else if(x == 60 && y == 20){ // EXTRACT
					player.getActiveCeng().setState(2);
				}
				else if(x == 66 && y == 20){ // FEED

					if(player.getActiveCeng().getState() == 3)
						player.getActiveCeng().setState(31);
					else if(player.getActiveCeng().getState() == 31)
						player.getActiveCeng().setState(3);
					else{
						player.getActiveCeng().setState(3);
					}
				}
				else if(x == 72 && y == 20){ // WALL
					player.getActiveCeng().setState(4);
				}
				else if(x > 75 || y > 22){
				player.getActiveCeng().setState(0);	
				}
			}
			

		}

	}

	// File Reader
	public void fileRead(){
		BufferedReader br = null;

		board = new Object[19][49];
		tempBoard = new Object[19][49];
		Pboard = new int[19][49];
		rangeBoard = new boolean[19][49];
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("C:\\Users\\iso\\workspace\\CengaWar\\src\\map.txt"));
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {

				for (int j = 0; j < sCurrentLine.length(); j++) {
					String s = sCurrentLine.substring(j,j+1);
					board[i][j] = s;
					tempBoard[i][j] = s;
					rangeBoard[i][j] = false;
					if(s.equals(" ")) Pboard[i][j] = 0;
					else Pboard[i][j] = 1;
				}
				i++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	// File Reader
	
	public void visibleMap(){
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 49; j++) {
				rangeBoard[i][j] = true;
				if(board[i][j] instanceof Tree){
					Tree tree = (Tree) board[i][j];
					tree.setInvisible(true);
				}
			}
		}
		computer.getBase().setInvisible(true);
	}

	public void displayBagOfCengman(){ // Aktif cengmanin çantasý
		int c = 0;
		Stack temp = new Stack(8);
		while(!player.getActiveCeng().getBagPack().isEmpty()){
			temp.push(player.getActiveCeng().getBagPack().pop());
		}
		while(!temp.isEmpty()) {

			if(temp.peek() instanceof Food){
				cn.getTextWindow().setCursorPosition(53,16-c);
				cn.getTextWindow().output("F");
				c++;
				player.getActiveCeng().getBagPack().push(temp.pop());
			}
			else if(temp.peek() instanceof Cement){
				cn.getTextWindow().setCursorPosition(53,16-c);
				cn.getTextWindow().output("C");
				c++;
				player.getActiveCeng().getBagPack().push(temp.pop());
			}
			else if(temp.peek().equals("~")){
				cn.getTextWindow().setCursorPosition(53,16-c);
				cn.getTextWindow().output("~");
				c++;
				player.getActiveCeng().getBagPack().push(temp.pop());
			}

		}
	}
	
	public void displayTarget(){
		if(player.getActiveCeng() != null){
			if(player.getActiveCeng().getEnemy() != null){
				CengMAN target = player.getActiveCeng().getEnemy();
				cn.getTextWindow().setCursorPosition(75,8);
				cn.getTextWindow().output("Target");
				cn.getTextWindow().setCursorPosition(75,9);
				cn.getTextWindow().output("Life : " + target.getLife());
			}
			else if(player.getActiveCeng().getTargetTree() != null){
				Tree target = player.getActiveCeng().getTargetTree();
				cn.getTextWindow().setCursorPosition(75,8);
				cn.getTextWindow().output("Target");
				cn.getTextWindow().setCursorPosition(75,9);
				cn.getTextWindow().output("Life : " + target.getLife());
			}
			else if(player.getActiveCeng().getTargetWall() != null){
				Wall target = player.getActiveCeng().getTargetWall();
				cn.getTextWindow().setCursorPosition(75,8);
				cn.getTextWindow().output("Target");
				cn.getTextWindow().setCursorPosition(75,9);
				cn.getTextWindow().output("Life : " + target.getLife());
			}
			else if(player.getActiveCeng().getTargetBase() != null){
				Base target = player.getActiveCeng().getTargetBase();
				cn.getTextWindow().setCursorPosition(75,8);
				cn.getTextWindow().output("Target");
				cn.getTextWindow().setCursorPosition(75,9);
				cn.getTextWindow().output("Life : " + target.getLife());
			}
			
		}
	}

	

	public void displayBoard(){ // tahtanýn yazýlmasý
		TextAttributes a = new TextAttributes(Color.WHITE,Color.BLUE);
		TextAttributes a1 = new TextAttributes(Color.WHITE,Color.RED);
		TextAttributes a2 = new TextAttributes(Color.LIGHT_GRAY,Color.LIGHT_GRAY);
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 49; j++) {
				if(tempBoard[i][j].equals("#")){
					cn.getTextWindow().setCursorPosition(j,i);
					cn.getTextWindow().output(tempBoard[i][j].toString(),a2);
				}
				else if(board[i][j] instanceof CengMAN){
					CengMAN ceng = (CengMAN) board[i][j];
					if(ceng.isTeam()){
						cn.getTextWindow().setCursorPosition(j,i);
						cn.getTextWindow().output(tempBoard[i][j].toString(),a);
					}
					else if(!ceng.isTeam() && rangeBoard[i][j] == true){
						cn.getTextWindow().setCursorPosition(j,i);
						cn.getTextWindow().output(tempBoard[i][j].toString(),a1);
					}
				}
				else if(board[i][j] instanceof Tree){
					Tree tree = (Tree) board[i][j];
					if(tree.isInvisible() && tree.isTeam()){
						cn.getTextWindow().setCursorPosition(j,i);
						cn.getTextWindow().output(tempBoard[i][j].toString(),a);
					}
					else if(tree.isInvisible() && !tree.isTeam()){
						cn.getTextWindow().setCursorPosition(j,i);
						cn.getTextWindow().output(tempBoard[i][j].toString(),a1);
					}
				}
				else if(board[i][j] instanceof Base){
					Base base = (Base) board[i][j];
					if(base.isTeam()){
						cn.getTextWindow().setCursorPosition(j,i);
						cn.getTextWindow().output(tempBoard[i][j].toString(),a);
					}
					else if(!base.isTeam() && base.isInvisible()){
						cn.getTextWindow().setCursorPosition(j,i);
						cn.getTextWindow().output(tempBoard[i][j].toString(),a1);
					}
				}
				else if(tempBoard[i][j].equals(".")){
					cn.getTextWindow().setCursorPosition(j,i);
					cn.getTextWindow().output(tempBoard[i][j].toString());
				}
				else{
					if(rangeBoard[i][j] == true){
						cn.getTextWindow().setCursorPosition(j,i);
						cn.getTextWindow().output(tempBoard[i][j].toString());
					}
				}
			}
		}

		
		
		if(player.getActiveCeng() != null){
			TextAttributes a3 = new TextAttributes(Color.BLACK,Color.YELLOW);
			if(player.getActiveCeng().getState() == -1 || player.getActiveCeng().getState() == 1){
				cn.getTextWindow().setCursorPosition(54,20);
				cn.getTextWindow().output("G",a3);
			}
			else if(player.getActiveCeng().getState() == 2){
				cn.getTextWindow().setCursorPosition(60,20);
				cn.getTextWindow().output("E",a3);
			}
			else if(player.getActiveCeng().getState() == 3){
				cn.getTextWindow().setCursorPosition(66,20);
				cn.getTextWindow().output("F",a3);
			}
			else if(player.getActiveCeng().getState() == 4){
				cn.getTextWindow().setCursorPosition(72,20);
				cn.getTextWindow().output("W",a3);
			}
		}

		for (int k = 0; k < 19; k++) {
			for (int k2 = 0; k2 < 49; k2++) {
				rangeBoard[k][k2] = false;
			}
		}
		

	}

	public void displayGame(){ // Board dýþýndakilerin yazýlmasý
		TextAttributes a = new TextAttributes(Color.LIGHT_GRAY,Color.LIGHT_GRAY);
		displayTarget();
		cn.getTextWindow().setCursorPosition(59,0);
		cn.getTextWindow().output("Time : "+time);
		cn.getTextWindow().setCursorPosition(59,1);
		cn.getTextWindow().output("Base Mods");
		cn.getTextWindow().setCursorPosition(53,2);
		cn.getTextWindow().output("---   ---   ---   ---");
		cn.getTextWindow().setCursorPosition(53,3);
		cn.getTextWindow().output("|T|   |C|   |W|   |R|");
		cn.getTextWindow().setCursorPosition(53,4);
		cn.getTextWindow().output("---   ---   ---   ---");

		cn.getTextWindow().setCursorPosition(58,8);
		cn.getTextWindow().output("Active Unit");
		cn.getTextWindow().setCursorPosition(59,9);
		if(player.getActiveCeng() != null)
			cn.getTextWindow().output(player.getActiveCeng().getName()+player.getActiveCeng().getId());
		cn.getTextWindow().setCursorPosition(59,10);
		if(player.getActiveCeng() != null)
			cn.getTextWindow().output("Life : "+player.getActiveCeng().getLife());
		cn.getTextWindow().setCursorPosition(59,11);
		if(player.getActiveCeng() != null)
			cn.getTextWindow().output("Level : "+player.getActiveCeng().getLevel());

		for (int i = 0; i < 8; i++) {
			cn.getTextWindow().setCursorPosition(52,16-i);
			cn.getTextWindow().output("|",a);
			cn.getTextWindow().output(" ");
			cn.getTextWindow().output("|",a);
		}
		if(player.getActiveCeng() != null)
			displayBagOfCengman();
		cn.getTextWindow().setCursorPosition(52,17);
		cn.getTextWindow().output("|-|",a);
		
		

		cn.getTextWindow().setCursorPosition(56,18);
		cn.getTextWindow().output("Active Unit Mods");
		cn.getTextWindow().setCursorPosition(53,19);
		cn.getTextWindow().output("---   ---   ---   ---");
		cn.getTextWindow().setCursorPosition(53,20);
		cn.getTextWindow().output("|G|   |E|   |F|   |W|");
		cn.getTextWindow().setCursorPosition(53,21);
		cn.getTextWindow().output("---   ---   ---   ---");

		cn.getTextWindow().setCursorPosition(2,20);
		cn.getTextWindow().output("Production Queue --> ");
		player.displayQueue();
		cn.getTextWindow().setCursorPosition(2,22);
		cn.getTextWindow().output("Base Life : "+player.getBase().getLife());
	}

	public void gamePlay(){

		long t = System.currentTimeMillis();
		while(true){
			if(t+1000 <= System.currentTimeMillis()){
				
				if(visible) visibleMap();
				time++;
				player.routine();
				computer.routine();
				consoleClear();
				displayGame();
				displayBoard();
				pathClear();

				t = System.currentTimeMillis();
				if(player.getBase().getLife() <= 0){
					consoleClear();
					TestCengAWar.menu = true;
					break;
				}
				else if(computer.getBase().getLife()<= 0){
					consoleClear();
					TestCengAWar.menu = true;
					break;
				}
			}
		}


	}

	public void pathClear(){
		for (int i = 1; i < 18; i++) {
			for (int j = 1; j < 48; j++) {
				if(tempBoard[i][j].equals(".")){
					tempBoard[i][j] = " ";
					Pboard[i][j] = 0;
				}
			}
		}
	}

	public void consoleClear(){
		for (int i = 1; i < 18; i++) {
			for (int j = 1; j < 48; j++) {
				if(!tempBoard[i][j].equals("#")){
					cn.getTextWindow().setCursorPosition(j,i);
					cn.getTextWindow().output(" ");
				}
			}

		}
		for (int i = 0; i < 8; i++) {
			cn.getTextWindow().setCursorPosition(23+i,20);
			cn.getTextWindow().output(" ");
		}
		cn.getTextWindow().setCursorPosition(66,10);
		cn.getTextWindow().output("   ");
		cn.getTextWindow().setCursorPosition(67,11);
		cn.getTextWindow().output(" ");
		
		cn.getTextWindow().setCursorPosition(75,8);
		cn.getTextWindow().output("            ");
		cn.getTextWindow().setCursorPosition(75,9);
		cn.getTextWindow().output("            ");
	}



	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	

}

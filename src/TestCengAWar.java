import java.awt.Color;
import java.util.*;

import enigma.console.TextAttributes;
import enigma.core.Enigma;
public class TestCengAWar {
	public static boolean menu = false;
	public static void main(String[] args) throws Exception {
		
		enigma.console.Console cn = Enigma.getConsole("Ceng-A-War",120,30,true);
		
		CengAWar ceng = new CengAWar();
		
		Scanner scn = new Scanner(System.in);
		TextAttributes a1 = new TextAttributes(Color.WHITE,Color.RED);
		TextAttributes a2 = new TextAttributes(Color.WHITE,Color.BLUE);
		while(!menu){
			
			cn.getTextWindow().setCursorPosition(15,2);
			cn.getTextWindow().output("Ceng-A-War");
			cn.getTextWindow().setCursorPosition(8,3);
			System.out.println("1- Play with invisible map");
			cn.getTextWindow().setCursorPosition(8,4);
			System.out.println("2- Play with visible map");
			cn.getTextWindow().setCursorPosition(8,5);
			System.out.println("3- Exit");
			cn.getTextWindow().setCursorPosition(8,6);
			System.out.print("Choice : ");
			int Choice = scn.nextInt();
			
			
			
			switch(Choice){
			case 1:
				ceng.gamePlay();
				break;
				
			case 2:
				ceng.setVisible(true);
				ceng.gamePlay();
				break;
				
			case 3:
				menu = true;
				
			default:
			break;		
			}
		}
		
		
		
	}

}

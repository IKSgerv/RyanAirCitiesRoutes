package mx.ia.algorithms;

import java.util.Scanner;
import java.util.Vector;

public class TicTacToe {
	public static String MACHINE = "m";
	public static String OPPONENT = "o";
	public static String EMPTY = "e";
	
	public static void main(String[] args){
		Scanner keyboard;
		boolean play = true;
		String[][] board = {
				{"e","o","e"},
				{"e","e","e"},
				{"e","e","e"}
		};
		int i, j;
		
		TicTacToe game = new TicTacToe();
		
		if(play){
			keyboard = new Scanner(System.in);
			while(true){
				
				board = game.generateStates(board);
				
				for (int in = 0; in < 3; in++){
					for (int jn = 0; jn < 3; jn++){
						System.out.print(board[in][jn] + "|");
					}
					System.out.println();
				}
				if(game.gameOver(board) || game.win(board))
					break;
				do{
					System.out.println("Ingrese i");
					i = Integer.parseInt(keyboard.next());
					System.out.println("Ingrese j");
					j = Integer.parseInt(keyboard.next());
				}while(board[i][j] != EMPTY);
				board[i][j] = OPPONENT;
				
				if(game.gameOver(board) || game.win(board))
					break;
			}
		}else{
			String[][] testBoard = {
					{"o","e","e"},
					{"e","m","e"},
					{"e","e","o"}
			};
			
			testBoard = game.generateStates(testBoard);
			
			for (int in = 0; in < 3; in++){
				for (int jn = 0; jn < 3; jn++){
					System.out.print(testBoard[in][jn] + "|");
				}
				System.out.println();
			}
		}
		
		System.out.println("gameover");
//		testBoard = game.generateStates(testBoard);
	}
	
	public boolean gameOver(String[][] t){
		for (String[] strings : t) {
			for (String string : strings) {
				if(string == EMPTY)
					return false;
			}
		}
		return true;
	}
	public boolean win(String[][] t){
		int[] machine = {0,0,0,0,0,0,0,0};
		int[] opponent = {0,0,0,0,0,0,0,0};
		for (int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				machine[i] += t[i][j] == MACHINE ? 1 : 0;
				opponent[i] += t[i][j] == OPPONENT ? 1 : 0;
				
				machine[3 + i] += t[j][i] == MACHINE ? 1 : 0;
				opponent[3 + i] += t[j][i] == OPPONENT ? 1 : 0;
			}
			machine[6] += t[i][i] == MACHINE ? 1 : 0;
			opponent[6] += t[i][i] == OPPONENT ? 1 : 0;
			
			machine[7] += t[i][2 - i] == MACHINE ? 1 : 0;
			opponent[7] += t[i][2 - i] == OPPONENT ? 1 : 0;
		}
		for (int i : machine) {
			if (i == 3){
				System.out.println("Machine wins!");
				return true;
			}
		}
		for (int i : opponent) {
			if (i == 3){
				System.out.println("User wins!");
				return true;
			}
		}
		return false;
	}
	public String[][] generateStates(String[][] t){
		String[][] res = null;
		TicTacToeElement better;
		Vector<TicTacToeElement> states = new Vector<TicTacToeElement>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if(t[i][j] == EMPTY){
					states.add(new TicTacToeElement(i, j, t));
				}
			}
		}
		if(states.size() > 0){
			better = getBetter(states, t);
			res = better.getState();
			return res;
		}else{
			return t;
		}
	}
	private TicTacToeElement getBetter(Vector<TicTacToeElement> states, String[][] board){
		double ran;
		int random;
		TicTacToeElement res = null;
		Vector<TicTacToeElement> betters = new Vector<TicTacToeElement>();
		for (TicTacToeElement ticTacToeElement : states) {
			if(res == null)
				res = ticTacToeElement;
			else if(ticTacToeElement.getValue() > res.getValue())
				res = ticTacToeElement;
		}
		for(TicTacToeElement t : states){
			if(t.getValue() == res.getValue()){
				betters.addElement(t);
			}
		}
		ran = Math.random();
		random = (int) ((ran - 0.005 ) * betters.size());
		System.out.println("betters: " + betters.size() + " random: " + random + " ran: " + ran);
		res = betters.elementAt(random);
		return res;
	}
}
class TicTacToeElement{
	private int[][] priorities = {
			{0,		-10,	-100,	-1000},
			{11,	0,		10,		0},
			{101,	10,		0,		0},
			{1001,	0,		0,		0}
	};
	private int[][] heuristicStrategy = {
			{2,1,2},
			{1,0,1},
			{2,1,2}	
	};
	private static String MACHINE = "m";
	private static String OPPONENT = "o";
	private static String EMPTY = "e";
	
	private int i, j;
	private String[][] state = new String[3][3];
	private double value;
	TicTacToeElement(int i, int j, String[][] board){
		this.i = i;
		this.j = j;
		for (int in = 0; in < 3; in++){
			for (int jn = 0; jn < 3; jn++){
				if(in == i && jn == j)
					state[in][jn] = "m";
				else
					state[in][jn] = board[in][jn];
			}
		}
		value = heuricticValuer();
		System.out.println(this);
	}
	public double getValue(){
		return value;
	}
	public String[][] getState(){
		return state;
	}
	void setValue(double value){
		this.value = value;
	}
	public String toString(){
		String res = "";
		for (int in = 0; in < 3; in++){
			for (int jn = 0; jn < 3; jn++){
				if(in == i && jn == j)
					res += "*";
				else
					res += state[in][jn];
				res += " ";
			}
			res += "\n";
		}
		res += "(" + value + ")\n";
		return res;
	}
	private double heuricticValuer(){
		double res = 0.0;
		int[] values = {0,0,0,0,0,0,0,0};
		int[] machine = {0,0,0,0,0,0,0,0};
		int[] opponent = {0,0,0,0,0,0,0,0};
		int machineCount = 0, opponentCount = 0, hCount = 0;
		boolean strategy = false;
		String[][] board = this.getState();
		for (int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				machine[i] += board[i][j] == MACHINE ? 1 : 0;
				opponent[i] += board[i][j] == OPPONENT ? 1 : 0;
				machine[3 + i] += board[j][i] == MACHINE ? 1 : 0;
				opponent[3 + i] += board[j][i] == OPPONENT ? 1 : 0;
			}
			machine[6] += board[i][i] == MACHINE ? 1 : 0;
			opponent[6] += board[i][i] == OPPONENT ? 1 : 0;
			machine[7] += board[i][2 - i] == MACHINE ? 1 : 0;
			opponent[7] += board[i][2 - i] == OPPONENT ? 1 : 0;
		}
		
		for (int i = 0; i < 8; i++){
			System.out.print(machine[i] + "|");
			machineCount += machine[i];
		}
		System.out.print(machineCount);
		System.out.println();
		for (int i = 0; i < 8; i++){
			System.out.print(opponent[i] + "|");
			opponentCount += opponent[i];
		}
		System.out.print(opponentCount);
		System.out.println();
		
		//By strategy
		if(machineCount == 3 && opponentCount == 0){
			res += priorities[machineCount][opponentCount];
		}
		for (int i = 6; i < 8; i++){
			if(machine[i] == 2 && opponent[i] == 1 && this.i != 1 && this.j != 1){
				strategy = true;
				for (int j = 0; j < 6; j++){
					if(machine[j] + opponent[j] != 1){
						strategy = false;
					}
				}
			}else if(machine[i] == 1 && opponent[i] == 2 && board[1][1] == MACHINE){
				for(int in = 0; in < 3; in++){
					for(int jn = 0; jn < 3; jn++){
						if(board[in][jn] != EMPTY)
							hCount += heuristicStrategy[in][jn];
					}
				}
			}else if(machine[i] == 1 && opponent[i] == 0 && heuristicStrategy[this.i][this.j] == 2){
				if(machineCount == 6 && (opponentCount == 2 || opponentCount == 3))
					strategy = true;
			}
		}
		System.out.println("Strategy count: " + hCount);
				
		for (int i = 0; i < 8; i++){
			values[i] = priorities[machine[i]][opponent[i]];
			System.out.print(values[i] + "|");
			res += values[i];
		}
		if(strategy || hCount == 5){
			System.out.println("Strategy founded");
			res += 1000;//priorities[0][3];
		}
		if(opponentCount == 2 && machineCount == 3 && res == 12){
			res = res * heuristicStrategy[this.i][this.j];
		}
		System.out.println(res);
		return res;
	}
}
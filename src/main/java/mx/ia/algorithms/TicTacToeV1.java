package mx.ia.algorithms;

import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class TicTacToeV1 {
	public static String MACHINE = "m";
	public static String OPPONENT = "o";
	public static String EMPTY = "e";

	public static void main(String[] args) {
		boolean play = true;
		Scanner keyboard;
		int i, j;
		String[] board = { "e", "e", "e", "e", "e", "e", "e", "e", "e" };
		TicTacToeV1 game = new TicTacToeV1();

		if (play) {
			keyboard = new Scanner(System.in);
			while (true) {

				board = game.resolve(board);

				if (game.gameOver(board)) {
					System.out.println("game over");
					break;
				}

				do {
					System.out.println("Ingrese i");
					i = Integer.parseInt(keyboard.next());
					System.out.println("Ingrese j");
					j = Integer.parseInt(keyboard.next());
				} while (board[i + (j * 3)] != EMPTY);
				board[i + (j * 3)] = OPPONENT;

				if (game.gameOver(board)) {
					System.out.println("game over");
					break;
				}
			}
		}
	}

	public boolean gameOver(String[] t) {
		int oponentWin = 0, machineWin = 0, empty = 0;
		int[][] winnersPositions = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
				{ 0, 4, 8 }, { 6, 4, 2 } };
		for (int[] is : winnersPositions) {
			if (oponentWin != 3)
				oponentWin = 0;
			if (machineWin != 3)
				machineWin = 0;
			for (int i : is) {
				if (t[i].equals(MACHINE) && machineWin != 3)
					machineWin++;
				if (t[i].equals(OPPONENT) && oponentWin != 3)
					oponentWin++;
				if (t[i].equals(EMPTY))
					empty++;
			}
		}
		return machineWin == 3 || oponentWin == 3 || empty == 0;
	}

	public String[] resolve(String[] state) {
		Random r = new Random();
		TicTacToeV1Element res = null;
		Vector<TicTacToeV1Element> solutions = new Vector<TicTacToeV1Element>();
		double bestValue;

		TicTacToeV1Element root = new TicTacToeV1Element(state);

		generateAdyacents(root);
		for (TicTacToeV1Element element : root.getAdjacents()) {
			if (res == null || element.getValue() > res.getValue()) {
				res = element;
			}
		}
		if (res != null) {
			bestValue = res.getValue();
			for (TicTacToeV1Element element : root.getAdjacents()) {
				if (element.getValue() == bestValue) {
					solutions.addElement(element);
				}
			}
		} else
			res = root;
		if (solutions.size() > 1) {
			res = solutions.get(r.nextInt(solutions.size() - 1));
		}

		System.out.println("Solution: " + res.getValue());
		System.out.println(res);
		return res.getState();
	}

	void generateAdyacents(TicTacToeV1Element element) {
		TicTacToeV1Element generated;
		if (!gameOver(element.getState())) {
			for (int i = 0; i < element.getState().length; i++) {
				if (element.getState()[i].equals(EMPTY)) {
					generated = new TicTacToeV1Element(i, element);
					generateAdyacents(generated);
					element.getAdjacents().add(generated);
				}
			}
		}
	}

	boolean thereIsAnEmpty(String[] state) {
		for (String string : state)
			if (string.equals(EMPTY))
				return true;
		return false;
	}
}

class TicTacToeV1Element {
	private double value;
	private int position;
	private int depth;
	private String[] state = new String[9];
	private boolean miniMax;
	private Vector<TicTacToeV1Element> adjacents = new Vector<TicTacToeV1Element>();

	TicTacToeV1Element(String[] state) {
		this.state = state;
		this.miniMax = true;
		this.position = -1;
		this.depth = 0;
	}

	TicTacToeV1Element(int position, TicTacToeV1Element father) {
		this.position = position;
		this.state = father.getState().clone();
		this.miniMax = !father.isMiniMax();
		this.state[position] = father.isMiniMax() ? TicTacToe.MACHINE : TicTacToe.OPPONENT;
		this.depth = father.getDepth() + 1;
	}

	public boolean isMiniMax() {
		return miniMax;
	}

	public Vector<TicTacToeV1Element> getAdjacents() {
		return adjacents;
	}

	public void setAdjacents(Vector<TicTacToeV1Element> adjacents) {
		this.adjacents = adjacents;
	}

	public String[] getState() {
		return state;
	}

	public void setState(String[] state) {
		this.state = state;
	}

	public String toString() {
		String[] st = this.getState();
		String res = "";
		for (int i = 0; i < st.length; i++) {
			res += st[i];
			if ((i + 1) % 3 == 0) {
				res += "\n";
			} else {
				res += "|";
			}
		}
		res += "Depth ";
		res += Integer.toString(depth);
		return res;
	}

	public double doValue() {
		int oponentWin = 0, machineWin = 0;
		int[][] winnersPositions = { 
				{ 0, 1, 2 }, 
				{ 3, 4, 5 }, 
				{ 6, 7, 8 }, 
				{ 0, 3, 6 }, 
				{ 1, 4, 7 }, 
				{ 2, 5, 8 },
				{ 0, 4, 8 }, 
				{ 6, 4, 2 } };
		for (int[] is : winnersPositions) {
			if (machineWin != 3) {
				machineWin = 0;
			}
			if (oponentWin != 3) {
				oponentWin = 0;
			}
			for (int i : is) {
				if (this.getState()[i].equals(TicTacToe.MACHINE) && machineWin != 3) {
					machineWin++;
				}
				if (this.getState()[i].equals(TicTacToe.OPPONENT) && oponentWin != 3) {
					oponentWin++;
				}
			}
		}
		if (machineWin == 3) {
			return 10;
		}
		if (oponentWin == 3) {
			return -10;
		}
		return 0;
	}

	public double getValue() {
		double res = Double.MAX_VALUE, temp;
		if (adjacents.size() > 0) {
			for (TicTacToeV1Element ticTacToeV2Element : this.adjacents) {
				temp = ticTacToeV2Element.getValue();
				if (miniMax) {
					if (temp > res || res == Double.MAX_VALUE) {
						res = temp;
					}
				} else {
					if (temp < res || res == Double.MAX_VALUE) {
						res = temp;
					}
				}
			}
		} else {
			res = doValue();
		}
		this.value = res;
		return this.value;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}
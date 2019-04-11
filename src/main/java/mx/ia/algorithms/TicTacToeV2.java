package mx.ia.algorithms;

import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class TicTacToeV2 {
	private int limit = 6;

	public static void main(String[] args) {
		boolean play = true;
		Scanner keyboard;
		int i, j;
		TOKEN[] board = { //
				TOKEN.EMPTY, TOKEN.EMPTY, TOKEN.EMPTY, //
				TOKEN.EMPTY, TOKEN.EMPTY, TOKEN.EMPTY, //
				TOKEN.EMPTY, TOKEN.EMPTY, TOKEN.EMPTY };
		TicTacToeV2 game = new TicTacToeV2();

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
				} while (board[i + (j * 3)] != TOKEN.EMPTY);
				board[i + (j * 3)] = TOKEN.PLAYER;

				if (game.gameOver(board)) {
					System.out.println("game over");
					break;
				}
			}
		}
	}

	public void setLimit(int l) {
		this.limit = l;
	}

	public boolean gameOver(TOKEN[] board) {
		int oponentWin = 0, machineWin = 0, empty = 0;
		int[][] winnersPositions = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
				{ 0, 4, 8 }, { 6, 4, 2 } };
		for (int[] is : winnersPositions) {
			if (oponentWin != 3)
				oponentWin = 0;
			if (machineWin != 3)
				machineWin = 0;
			for (int i : is) {
				if (board[i] == TOKEN.MACHINE && machineWin != 3)
					machineWin++;
				if (board[i] == TOKEN.PLAYER && oponentWin != 3)
					oponentWin++;
				if (board[i] == TOKEN.EMPTY)
					empty++;
			}
		}
		return machineWin == 3 || oponentWin == 3 || empty == 0;
	}

	public TOKEN[] resolve(TOKEN[] state) {
		Random r = new Random();
		TicTacToeV2Element res = null;
		Vector<TicTacToeV2Element> solutions = new Vector<TicTacToeV2Element>();
		double bestValue;

		TicTacToeV2Element root = new TicTacToeV2Element(state);

		generateAdyacents(root);
		for (TicTacToeV2Element element : root.getAdjacents()) {
			if (res == null || element.getValue() > res.getValue()) {
				res = element;
			}
		}
		if (res != null) {
			bestValue = res.getValue();
			for (TicTacToeV2Element element : root.getAdjacents()) {
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

	void generateAdyacents(TicTacToeV2Element element) {
		TicTacToeV2Element generated;
		if (!gameOver(element.getState())) {
			for (int i = 0; i < element.getState().length; i++) {
				if (element.getState()[i] == TOKEN.EMPTY) {
					generated = new TicTacToeV2Element(i, element);
					if (generated.getDepth() < limit)
						generateAdyacents(generated);
					element.getAdjacents().add(generated);
				}
			}
		}
	}

	boolean thereIsAnEmpty(TOKEN[] state) {
		for (TOKEN token : state)
			if (token == TOKEN.EMPTY)
				return true;
		return false;
	}
}

class TicTacToeV2Element {
	private double value;
	private int position;
	private int depth;
	private TOKEN[] state = new TOKEN[9];
	private boolean miniMax;
	private Vector<TicTacToeV2Element> adjacents = new Vector<TicTacToeV2Element>();

	TicTacToeV2Element(TOKEN[] state) {
		this.state = state;
		this.miniMax = true;
		this.position = -1;
		this.depth = 0;
	}

	TicTacToeV2Element(int position, TicTacToeV2Element father) {
		this.position = position;
		this.state = father.getState().clone();
		this.miniMax = !father.isMiniMax();
		this.state[position] = father.isMiniMax() ? TOKEN.MACHINE : TOKEN.PLAYER;
		this.depth = father.getDepth() + 1;
	}

	public boolean isMiniMax() {
		return miniMax;
	}

	public Vector<TicTacToeV2Element> getAdjacents() {
		return adjacents;
	}

	public void setAdjacents(Vector<TicTacToeV2Element> adjacents) {
		this.adjacents = adjacents;
	}

	public TOKEN[] getState() {
		return state;
	}

	public void setState(TOKEN[] state) {
		this.state = state;
	}

	public String toString() {
		TOKEN[] st = this.getState();
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
		int oponentWin = 0, machineWin = 0, value = 0;
		int[] machine = { 0, 0, 0, 0, 0, 0, 0, 0 }, opponent = { 0, 0, 0, 0, 0, 0, 0, 0 };
		int[][] priorities = { { 0, -10, -100, -1000 }, { 11, 0, 10, 0 }, { 101, 10, 0, 0 }, { 1001, 0, 0, 0 } };

		int[][] winnersPositions = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
				{ 0, 4, 8 }, { 6, 4, 2 } };

		for (int i = 0; i < winnersPositions.length; i++) {
			for (int j = 0; j < 3; j++) {
				machine[i] += this.getState()[j] == TOKEN.MACHINE ? 1 : 0;
				machine[i] += this.getState()[j] == TOKEN.PLAYER ? 1 : 0;
			}
		}
		for (int i = 0; i < 8; i++) {
			value += priorities[machine[i]][opponent[i]];
		}

		for (int[] is : winnersPositions) {
			if (machineWin != 3) {
				machineWin = 0;
			}
			if (oponentWin != 3) {
				oponentWin = 0;
			}
			for (int i : is) {
				if (this.getState()[i] == TOKEN.MACHINE && machineWin != 3) {
					machineWin++;
				}
				if (this.getState()[i] == TOKEN.PLAYER && oponentWin != 3) {
					oponentWin++;
				}
			}
		}
		if (machineWin == 3) {
			value += 10000;
		}
		if (oponentWin == 3) {
			value -= 10000;
		}
		return value;
	}

	public double getValue() {
		double res = Double.MAX_VALUE, temp;
		if (adjacents.size() > 0) {
			for (TicTacToeV2Element ticTacToeV2Element : this.adjacents) {
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

enum TOKEN {
	MACHINE, PLAYER, EMPTY
}
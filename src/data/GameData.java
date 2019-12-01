package data;

import java.util.ArrayList;

import chess.Chess;
import constants.ChessBoardConstants;
import constants.ChessConstants;
import ui.ChessFile;
import ui.ChessLabel;

public class GameData implements ChessBoard, Runnable {

	private ChessLabel chosenChess; // �ѱ�ѡ�е�����
	private Player currentPlayer; // ��ǰ�����һ��
	private int location;// ��ǰ�����¼���±�
	private Player player1;// �췽
	private Player player2;// �ڷ�
	private ArrayList<LogSentence> logs;// �����¼
	private boolean alive;// ����Ƿ��ڽ��У�����������˸�߳��жϣ�

	public GameData(Player player1, Player player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		currentPlayer = player1;
		chosenChess = null;
		logs = new ArrayList<LogSentence>();
		location = -1;// -1��Ϊ��
		alive = true;
	}

	public ChessLabel getChosenChess() {
		return chosenChess;
	}

	public void setChosenChess(ChessLabel chosenChess) {
		this.chosenChess = chosenChess;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public int getLocation() {
		return location;
	}

	// ��ֽ�������ͣ
	public void die() {
		alive = false;
	}

	// ��ּ��������¿�ʼ
	public void birth() {
		alive = true;
	}

	public LogSentence[] getLogs() {
		return (LogSentence[]) logs.toArray(new LogSentence[logs.size()]);
	}

	// ���������¼
	public void addLog(LogSentence log) {
		logs.add(log);
	}

	public void addLog(ChessLabel moveChess, Position from, Position to, ChessLabel food) {
		this.addLog(new LogSentence(moveChess, from, to, food));
	}

	// �������¼����ʽת��Ϊ�ַ�����д���ļ�
	public void logToFile() {
		StringBuffer writeString = new StringBuffer();
		for (LogSentence log : logs) {
			writeString.append(log.toString());
			writeString.append('\n');
		}
		ChessFile.saveToFile(writeString.toString(), ".log");
	}

	// �������¼ת��Ϊ���ײ�д���ļ�
	public void standardLogToFile() {
		StringBuffer writeString = new StringBuffer();
		for (LogSentence log : logs) {
			writeString.append(log.toStandardString());
			if (log.getMoveChess().getChess().getOwner() == ChessBoardConstants.PLAYER1) {
				writeString.append(",\t");
			} else if (log.getMoveChess().getChess().getOwner() == ChessBoardConstants.PLAYER2) {
				writeString.append("\n");
			}
		}
		ChessFile.saveToFile(writeString.toString(), ".txt");
	}

	// ���ַ���ת��Ϊ�����¼
	public void stringToLog(String[] strs) {
		ChessLabel moveChess = null;
		Position from = null;
		Position to = null;
		ChessLabel food = null;
		int[] paras = new int[7];
		logs = new ArrayList<>();
		for (String s : strs) {
			String[] ints = s.split("[^0-9-]");
			int i = 0;
			for (String str : ints) {
				if (str.length() != 0) {
					paras[i] = Integer.parseInt(str);
					i++;
				}
			}
			for (ChessLabel cl1 : currentPlayer.getChesses()) {
				Chess c1 = cl1.getChess();
				if (c1.getType() == paras[0] && c1.getOwner() == paras[1] && c1.getPos().getX() == paras[2]
						&& c1.getPos().getY() == paras[3]) {
					moveChess = cl1;
				}
			}
			changeCurrentPlayer();
			if (paras[6] != ChessConstants.NULL) {
				for (ChessLabel cl2 : currentPlayer.getChesses()) {
					Chess c2 = cl2.getChess();
					if (c2.getType() == paras[6] && c2.getOwner() != paras[1] && c2.getPos().getX() == paras[4]
							&& c2.getPos().getY() == paras[5]) {
						food = cl2;
						food.die();
					}
				}
			}
			from = new Position(paras[2], paras[3]);
			to = new Position(paras[4], paras[5]);
			logs.add(new LogSentence(moveChess, from, to, food));
			location++;
			moveChess.getChess().setPos(new Position(paras[4], paras[5]));
			moveChess.update();
		}
	}

	// ������ǰ���巽
	public void changeCurrentPlayer() {
		if (currentPlayer == player1) {
			player1.getTime().setRunning(false);
			player2.getTime().setRunning(true);
			currentPlayer = player2;
		} else {
			player2.getTime().setRunning(false);
			player1.getTime().setRunning(true);
			currentPlayer = player1;
		}
	}

	// �õ�����λ��֮������������������ͬһֱ�����-1
	@Override
	public int getNumOfChessOnLine(Position from, Position to) {
		int count = 0;
		ArrayList<ChessLabel> chesses;
		if (from.getX() == to.getX()) {
			chesses = player1.getChesses();
			for (ChessLabel chessLabel : chesses) {
				Chess chess = chessLabel.getChess();
				if (chessLabel.isAlive() && chess.getPos().getX() == from.getX()
						&& ((chess.getPos().getY() < from.getY() && chess.getPos().getY() > to.getY())
								|| (chess.getPos().getY() > from.getY() && chess.getPos().getY() < to.getY()))) {
					count++;
				}
			}
			chesses = player2.getChesses();
			for (ChessLabel chessLabel : chesses) {
				Chess chess = chessLabel.getChess();
				if (chessLabel.isAlive() && chess.getPos().getX() == from.getX()
						&& ((chess.getPos().getY() < from.getY() && chess.getPos().getY() > to.getY())
								|| (chess.getPos().getY() > from.getY() && chess.getPos().getY() < to.getY()))) {
					count++;
				}
			}
		} else if (from.getY() == to.getY()) {
			chesses = player1.getChesses();
			for (ChessLabel chessLabel : chesses) {
				Chess chess = chessLabel.getChess();
				if (chessLabel.isAlive() && chess.getPos().getY() == from.getY()
						&& ((chess.getPos().getX() < from.getX() && chess.getPos().getX() > to.getX())
								|| (chess.getPos().getX() > from.getX() && chess.getPos().getX() < to.getX()))) {
					count++;
				}
			}
			chesses = player2.getChesses();
			for (ChessLabel chessLabel : chesses) {
				Chess chess = chessLabel.getChess();
				if (chessLabel.isAlive() && chess.getPos().getY() == from.getY()
						&& ((chess.getPos().getX() < from.getX() && chess.getPos().getX() > to.getX())
								|| (chess.getPos().getX() > from.getX() && chess.getPos().getX() < to.getX()))) {
					count++;
				}
			}
		} else {
			count = -1;
		}
		return count;
	}

	// �õ�ĳλ����Χ�İ˸�λ�õ�״̬������������1��������0
	@Override
	public int[][] getNeighbours(Position pos) {
		int[][] neighbours = new int[3][3];
		ArrayList<ChessLabel> chesses;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				neighbours[i + 1][j + 1] = 0;
				if (i != 0 || j != 0) {
					int x = pos.getX() + j;
					int y = pos.getY() + i;
					if (x < ChessConstants.MAX_X && x >= 0 && y < ChessConstants.MAX_Y && y >= 0) {
						chesses = player1.getChesses();
						for (ChessLabel chessLabel : chesses) {
							Chess chess = chessLabel.getChess();
							if (chessLabel.isAlive() && chess.getPos().getX() == x && chess.getPos().getY() == y) {
								neighbours[i + 1][j + 1] = 1;
								break;
							}
						}
						chesses = player2.getChesses();
						for (ChessLabel chessLabel : chesses) {
							Chess chess = chessLabel.getChess();
							if (chessLabel.isAlive() && chess.getPos().getX() == x && chess.getPos().getY() == y) {
								neighbours[i + 1][j + 1] = 1;
								break;
							}
						}
					}
				}
			}
		}
		return neighbours;
	}

	// �ж�ĳλ�������ķ���Χ��
	@Override
	public int getTerritoryOwner(Position pos) {
		if ((pos.getY() - ChessConstants.MAX_Y / 2) < 0) {
			return ChessBoardConstants.PLAYER2;
		} else {
			return ChessBoardConstants.PLAYER1;
		}
	}

	// �ж��Ƿ񳬳����̷�Χ
	@Override
	public boolean isOutOfBound(Position pos) {
		return pos.getX() < 0 || pos.getX() >= ChessConstants.MAX_X || pos.getY() < 0
				|| pos.getY() >= ChessConstants.MAX_Y;
	}

	// �ж��Ƿ񳬳��Ź���Χ
	@Override
	public boolean isOutOfPalace(Position pos, int player) {
		if (player == ChessBoardConstants.PLAYER1) {
			if (pos.getX() <= ChessConstants.MAX_X / 2 + 1 && pos.getX() >= ChessConstants.MAX_X / 2 - 1
					&& pos.getY() <= ChessConstants.MAX_Y - 1 && pos.getY() >= ChessConstants.MAX_Y - 3) {
				return false;
			} else {
				return true;
			}
		} else if (player == ChessBoardConstants.PLAYER2) {
			if (pos.getX() <= ChessConstants.MAX_X / 2 + 1 && pos.getX() >= ChessConstants.MAX_X / 2 - 1
					&& pos.getY() <= 2 && pos.getY() >= 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	// �����������¼�
	public void ClickPosition(int x, int y) {
		if (chosenChess != null) { // ֮ǰ��ѡ������
			Chess c = chosenChess.getChess();
			Position now = c.getPos();
			if (c.move(this, new Position(x, y))) {
				this.addLog(chosenChess, now, new Position(x, y), null);
				changeCurrentPlayer();
				chosenChess.update();
			}
		}
		chosenChess = null;
	}

	// �����������¼�
	public void ClickChess(ChessLabel chessLabel) {
		if (chosenChess == null) {
			for (ChessLabel c : currentPlayer.getChesses()) {
				if (c == chessLabel) {
					chosenChess = chessLabel;
					break;
				}
			}
		} else {
			Chess chosen = chosenChess.getChess();
			Chess food = chessLabel.getChess();
			Position now = chosen.getPos();
			if (chosen.eat(this, food)) {
				this.addLog(chosenChess, now, food.getPos(), chessLabel);
				changeCurrentPlayer();
				chessLabel.die();
				chosenChess.update();
			}
			chosenChess = null;
		}
	}

	// ��Ӧѡ��������˸�߳�
	@Override
	public void run() {
		while (true) {
			if (chosenChess != null && alive) {
				ChessLabel c = chosenChess;
				c.setVisible(false);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				c.setVisible(true);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// �������
	public void repent() {
		if (logs.size() > 0) {
			LogSentence log = logs.get(logs.size() - 1);
			log.getMoveChess().getChess().setPos(log.getFrom());
			log.getMoveChess().update();
			if (log.getFood() != null) {
				log.getFood().getChess().setPos(log.getTo());
				log.getFood().birth();
				log.getFood().update();
			}
			location--;
			changeCurrentPlayer();
			chosenChess = null;
			logs.remove(logs.size() - 1);
		}
	}

	// ��һ������
	public boolean forward() {
		if (location < logs.size() - 1) {
			location++;
			LogSentence log = logs.get(location);
			log.getMoveChess().getChess().setPos(log.getTo());
			log.getMoveChess().update();
			changeCurrentPlayer();
			if (log.getFood() != null) {
				log.getFood().die();
			}
			return true;
		}
		return false;
	}

	// ǰһ������
	public boolean backward() {
		if (location >= 0) {
			LogSentence log = logs.get(location);
			log.getMoveChess().getChess().setPos(log.getFrom());
			log.getMoveChess().update();
			changeCurrentPlayer();
			if (log.getFood() != null) {
				log.getFood().birth();
			}
			location--;
			return true;
		}
		return false;
	}

}
package game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import chess.*;
import constants.ChessBoardConstants;
import constants.ChessConstants;
import data.EventHandler;
import data.GameData;
import data.GameTime;
import data.Player;
import data.Position;
import events.MouseClickEvent;
import ui.ChessFile;
import ui.ChessLabel;
import ui.GameFrame;
import ui.TimeLabel;

public class Game implements EventHandler, GameOver, ReturnMainGame {

	private boolean isOver = true; // ��¼����Ƿ����

	private GameData gamedata; // ��Ϸ����
	private GameFrame gameframe; // ��Ϸ���
	private MouseClickEvent mouseEvent;// ������¼�

	private Thread chessFlash; // ������˸�߳�
	private Thread ttime1; // �췽ʱ�ӵ���ʱ�߳�
	private Thread ttime2; // �ڷ�ʱ�ӵ���ʱ�߳�

	private Player player1; // �췽
	private Player player2;// �ڷ�

	JPanel chessPanel;// ����/�������
	JPanel timePanel;// ʱ����
	JPanel buttonPanel;// ��ť��

	TimeLabel timeLabel1;// �췽ʱ���ǩ
	TimeLabel timeLabel2;// �ڷ�ʱ���ǩ

	JButton newGameButton;// �¿�һ�ְ�ť
	JButton repentButton;// ���尴ť
	JButton saveLogButton;// ������ְ�ť
	JButton saveStandardLogButton;// �������װ�ť
	JButton reviewButton;// �������װ�ť
	JButton player1LoseButton;// �췽���䰴ť
	JButton player2LoseButton;// �ڷ����䰴ť

	JLabel chessBoardLabel; // ���̱�ǩ

	public Game() {
		chessPanel = new JPanel();
		chessPanel.setLayout(null);
		chessPanel.setSize(ChessBoardConstants.BOARD_WIDTH, ChessBoardConstants.BOARD_HEIGHT);

		timePanel = new JPanel();
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
		timePanel.setBackground(Color.CYAN);

		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.PINK);

		gameframe = new GameFrame(chessPanel, buttonPanel, timePanel, "�й�����^_^", new ImageIcon("image\\�콫.GIF"));
		newGame();
	}

	public void newGame() {
		int t = initGameTime();

		// Panel����
		chessPanel.removeAll();
		timePanel.removeAll();
		buttonPanel.removeAll();

		player1 = new Player(new GameTime(t));
		player2 = new Player(new GameTime(t));

		gamedata = new GameData(player1, player2);

		mouseEvent = new MouseClickEvent(this);

		initChess(chessPanel, player1, player2, mouseEvent);

		// ����
		chessBoardLabel = new JLabel(new ImageIcon("image\\Main.GIF"));
		chessBoardLabel.setBounds(0, 0, ChessBoardConstants.BOARD_WIDTH, ChessBoardConstants.BOARD_HEIGHT);
		chessBoardLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessBoardLabel);

		// ʱ�ӱ�ǩ
		timeLabel1 = new TimeLabel(player1.getTime(), ChessBoardConstants.PLAYER1, this);
		timeLabel2 = new TimeLabel(player2.getTime(), ChessBoardConstants.PLAYER2, this);

		// ʱ���߳�
		ttime1 = new Thread(timeLabel1);
		ttime2 = new Thread(timeLabel2);
		ttime1.start();
		ttime2.start();

		// ������˸�߳�
		chessFlash = new Thread(gamedata);
		chessFlash.start();

		// ��ť��
		newGameButton = new JButton("�¿�һ��");
		newGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		buttonPanel.add(newGameButton);

		repentButton = new JButton("����");
		repentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gamedata.repent();
			}
		});
		buttonPanel.add(repentButton);

		saveLogButton = new JButton("�������");
		saveLogButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gamedata.logToFile();
			}
		});
		buttonPanel.add(saveLogButton);

		saveStandardLogButton = new JButton("��������");
		saveStandardLogButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gamedata.standardLogToFile();
			}
		});
		buttonPanel.add(saveStandardLogButton);

		reviewButton = new JButton("��������");
		reviewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timeLabel1.setRunning(false);
				timeLabel2.setRunning(false);
				String[] reviewMessage = ChessFile.readFile(".log");
				if (reviewMessage.length > 0) {
					gamedata.die();
					gameframe.setVisible(false);
					new Review(reviewMessage, Game.this);
				}
			}
		});
		buttonPanel.add(reviewButton);

		player1LoseButton = new JButton("�췽����");
		player1LoseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "�����Ҫ������?", "�췽����ȷ��", JOptionPane.YES_NO_OPTION) == 0) {
					gameOver(ChessBoardConstants.PLAYER1);
				}
			}
		});

		player2LoseButton = new JButton("�ڷ�����");
		player2LoseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "�����Ҫ������?", "�ڷ�����ȷ��", JOptionPane.YES_NO_OPTION) == 0) {
					gameOver(ChessBoardConstants.PLAYER2);
				}
			}
		});

		timePanel.add(timeLabel2);
		timePanel.add(player2LoseButton);
		timePanel.add(timeLabel1);
		timePanel.add(player1LoseButton);

		gameframe.dispose();
		gameframe = new GameFrame(chessPanel, buttonPanel, timePanel, "�й�����^_^", new ImageIcon("image\\�콫.GIF"));

		this.isOver = false;
	}

	public int initGameTime() {
		// ������ʱ���öԻ���
		final int MAX_TIME = 60;
		Object[] obj = new Object[MAX_TIME];
		for (int i = 0; i < MAX_TIME; i += 1) {
			obj[i] = "" + (i + 1);
		}
		String str_time = (String) JOptionPane.showInputDialog(null, "��ѡ�񵥷���ʱ�����ӣ�:\n", "��ʱ����",
				JOptionPane.PLAIN_MESSAGE, new ImageIcon("icon.png"), obj, "1");
		return Integer.parseInt(str_time) * 60; // ��ȡѡ��ķ�������ת��������
	}

	public static void initChess(JPanel chessPanel, Player player1, Player player2, MouseClickEvent mouseEvent) {
		Chess chess;
		ChessLabel chessLabel;

		// �ڳ�1
		chess = new Rook(ChessBoardConstants.PLAYER2, ChessConstants.ROOK, new Position(0, 0));
		chessLabel = new ChessLabel(chess, "image\\�ڳ�.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// �ڳ�2
		chess = new Rook(ChessBoardConstants.PLAYER2, ChessConstants.ROOK, new Position(8, 0));
		chessLabel = new ChessLabel(chess, "image\\�ڳ�.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����1
		chess = new Knight(ChessBoardConstants.PLAYER2, ChessConstants.KNIGHT, new Position(1, 0));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����2
		chess = new Knight(ChessBoardConstants.PLAYER2, ChessConstants.KNIGHT, new Position(7, 0));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����1
		chess = new Bishop(ChessBoardConstants.PLAYER2, ChessConstants.BISHOP, new Position(2, 0));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����2
		chess = new Bishop(ChessBoardConstants.PLAYER2, ChessConstants.BISHOP, new Position(6, 0));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ��ʿ1
		chess = new Guard(ChessBoardConstants.PLAYER2, ChessConstants.GUARD, new Position(3, 0));
		chessLabel = new ChessLabel(chess, "image\\��ʿ.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ��ʿ2
		chess = new Guard(ChessBoardConstants.PLAYER2, ChessConstants.GUARD, new Position(5, 0));
		chessLabel = new ChessLabel(chess, "image\\��ʿ.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// �ڽ�
		chess = new King(ChessBoardConstants.PLAYER2, ChessConstants.KING, new Position(4, 0));
		chessLabel = new ChessLabel(chess, "image\\�ڽ�.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����1
		chess = new Connon(ChessBoardConstants.PLAYER2, ChessConstants.CONNON, new Position(1, 2));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����2
		chess = new Connon(ChessBoardConstants.PLAYER2, ChessConstants.CONNON, new Position(7, 2));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����
		for (int i = 0; i < 9; i += 2) {
			chess = new Pawn(ChessBoardConstants.PLAYER2, ChessConstants.PAWN, new Position(i, 3));
			chessLabel = new ChessLabel(chess, "image\\����.GIF");
			player2.addChess(chessLabel);
			chessLabel.addMouseListener(mouseEvent);
			chessPanel.add(chessLabel);
		}

		// �쳵1
		chess = new Rook(ChessBoardConstants.PLAYER1, ChessConstants.ROOK, new Position(0, 9));
		chessLabel = new ChessLabel(chess, "image\\�쳵.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// �쳵2
		chess = new Rook(ChessBoardConstants.PLAYER1, ChessConstants.ROOK, new Position(8, 9));
		chessLabel = new ChessLabel(chess, "image\\�쳵.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����1
		chess = new Knight(ChessBoardConstants.PLAYER1, ChessConstants.KNIGHT, new Position(1, 9));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����2
		chess = new Knight(ChessBoardConstants.PLAYER1, ChessConstants.KNIGHT, new Position(7, 9));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����1
		chess = new Bishop(ChessBoardConstants.PLAYER1, ChessConstants.BISHOP, new Position(2, 9));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����2
		chess = new Bishop(ChessBoardConstants.PLAYER1, ChessConstants.BISHOP, new Position(6, 9));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ��ʿ1
		chess = new Guard(ChessBoardConstants.PLAYER1, ChessConstants.GUARD, new Position(3, 9));
		chessLabel = new ChessLabel(chess, "image\\��ʿ.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ��ʿ2
		chess = new Guard(ChessBoardConstants.PLAYER1, ChessConstants.GUARD, new Position(5, 9));
		chessLabel = new ChessLabel(chess, "image\\��ʿ.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// �콫
		chess = new King(ChessBoardConstants.PLAYER1, ChessConstants.KING, new Position(4, 9));
		chessLabel = new ChessLabel(chess, "image\\�콫.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����1
		chess = new Connon(ChessBoardConstants.PLAYER1, ChessConstants.CONNON, new Position(1, 7));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ����2
		chess = new Connon(ChessBoardConstants.PLAYER1, ChessConstants.CONNON, new Position(7, 7));
		chessLabel = new ChessLabel(chess, "image\\����.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// ���
		for (int i = 0; i < 9; i += 2) {
			chess = new Pawn(ChessBoardConstants.PLAYER1, ChessConstants.PAWN, new Position(i, 6));
			chessLabel = new ChessLabel(chess, "image\\����.GIF");
			player1.addChess(chessLabel);
			chessLabel.addMouseListener(mouseEvent);
			chessPanel.add(chessLabel);
		}
	}

	@Override
	public void gameOver() {
		if (gamedata.getPlayer1().isloser()) {
			this.gameOver(ChessBoardConstants.PLAYER1);
		} else if (gamedata.getPlayer2().isloser()) {
			this.gameOver(ChessBoardConstants.PLAYER2);
		}
	}

	public void gameOver(int loser) {
		player1LoseButton.setEnabled(false);
		player2LoseButton.setEnabled(false);
		repentButton.setEnabled(false);

		timeLabel1.setRunning(false);
		timeLabel2.setRunning(false);
		gamedata.die();
		if (loser == ChessBoardConstants.PLAYER1) {
			JOptionPane.showConfirmDialog(null, "����ʤ��", "��Ҷ�ʤ��", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
		} else if (loser == ChessBoardConstants.PLAYER2) {
			JOptionPane.showConfirmDialog(null, "����ʤ��", "���һʤ��", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showConfirmDialog(null, "ƽ��", "�����Ժ�", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void ClickPosition(int x, int y) {
		if (!isOver) {
			gamedata.ClickPosition(x, y);
		}
	}

	@Override
	public void ClickChess(ChessLabel chessLabel) {
		if (!isOver) {
			gamedata.ClickChess(chessLabel);
			this.gameOver();
		}
	}

	public static void main(String[] args) {
		new Game();
	}

	@Override
	public void returnMainGame() {
		gamedata.birth();
		gameframe.setVisible(true);
	}

}
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

	private boolean isOver = true; // 记录棋局是否结束

	private GameData gamedata; // 游戏数据
	private GameFrame gameframe; // 游戏框架
	private MouseClickEvent mouseEvent;// 鼠标点击事件

	private Thread chessFlash; // 棋子闪烁线程
	private Thread ttime1; // 红方时钟倒计时线程
	private Thread ttime2; // 黑方时钟倒计时线程

	private Player player1; // 红方
	private Player player2;// 黑方

	JPanel chessPanel;// 棋子/棋盘面板
	JPanel timePanel;// 时间盘
	JPanel buttonPanel;// 按钮盘

	TimeLabel timeLabel1;// 红方时间标签
	TimeLabel timeLabel2;// 黑方时间标签

	JButton newGameButton;// 新开一局按钮
	JButton repentButton;// 悔棋按钮
	JButton saveLogButton;// 保存棋局按钮
	JButton saveStandardLogButton;// 保存棋谱按钮
	JButton reviewButton;// 导入棋谱按钮
	JButton player1LoseButton;// 红方认输按钮
	JButton player2LoseButton;// 黑方认输按钮

	JLabel chessBoardLabel; // 棋盘标签

	public Game() {
		chessPanel = new JPanel();
		chessPanel.setLayout(null);
		chessPanel.setSize(ChessBoardConstants.BOARD_WIDTH, ChessBoardConstants.BOARD_HEIGHT);

		timePanel = new JPanel();
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
		timePanel.setBackground(Color.CYAN);

		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.PINK);

		gameframe = new GameFrame(chessPanel, buttonPanel, timePanel, "中国象棋^_^", new ImageIcon("image\\红将.GIF"));
		newGame();
	}

	public void newGame() {
		int t = initGameTime();

		// Panel清零
		chessPanel.removeAll();
		timePanel.removeAll();
		buttonPanel.removeAll();

		player1 = new Player(new GameTime(t));
		player2 = new Player(new GameTime(t));

		gamedata = new GameData(player1, player2);

		mouseEvent = new MouseClickEvent(this);

		initChess(chessPanel, player1, player2, mouseEvent);

		// 棋盘
		chessBoardLabel = new JLabel(new ImageIcon("image\\Main.GIF"));
		chessBoardLabel.setBounds(0, 0, ChessBoardConstants.BOARD_WIDTH, ChessBoardConstants.BOARD_HEIGHT);
		chessBoardLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessBoardLabel);

		// 时钟标签
		timeLabel1 = new TimeLabel(player1.getTime(), ChessBoardConstants.PLAYER1, this);
		timeLabel2 = new TimeLabel(player2.getTime(), ChessBoardConstants.PLAYER2, this);

		// 时钟线程
		ttime1 = new Thread(timeLabel1);
		ttime2 = new Thread(timeLabel2);
		ttime1.start();
		ttime2.start();

		// 棋子闪烁线程
		chessFlash = new Thread(gamedata);
		chessFlash.start();

		// 按钮盘
		newGameButton = new JButton("新开一局");
		newGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		buttonPanel.add(newGameButton);

		repentButton = new JButton("悔棋");
		repentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gamedata.repent();
			}
		});
		buttonPanel.add(repentButton);

		saveLogButton = new JButton("保存棋局");
		saveLogButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gamedata.logToFile();
			}
		});
		buttonPanel.add(saveLogButton);

		saveStandardLogButton = new JButton("导出棋谱");
		saveStandardLogButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gamedata.standardLogToFile();
			}
		});
		buttonPanel.add(saveStandardLogButton);

		reviewButton = new JButton("载入棋谱");
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

		player1LoseButton = new JButton("红方认输");
		player1LoseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "你真的要认输吗?", "红方认输确认", JOptionPane.YES_NO_OPTION) == 0) {
					gameOver(ChessBoardConstants.PLAYER1);
				}
			}
		});

		player2LoseButton = new JButton("黑方认输");
		player2LoseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "你真的要认输吗?", "黑方认输确认", JOptionPane.YES_NO_OPTION) == 0) {
					gameOver(ChessBoardConstants.PLAYER2);
				}
			}
		});

		timePanel.add(timeLabel2);
		timePanel.add(player2LoseButton);
		timePanel.add(timeLabel1);
		timePanel.add(player1LoseButton);

		gameframe.dispose();
		gameframe = new GameFrame(chessPanel, buttonPanel, timePanel, "中国象棋^_^", new ImageIcon("image\\红将.GIF"));

		this.isOver = false;
	}

	public int initGameTime() {
		// 弹出限时设置对话框
		final int MAX_TIME = 60;
		Object[] obj = new Object[MAX_TIME];
		for (int i = 0; i < MAX_TIME; i += 1) {
			obj[i] = "" + (i + 1);
		}
		String str_time = (String) JOptionPane.showInputDialog(null, "请选择单方限时（分钟）:\n", "限时设置",
				JOptionPane.PLAIN_MESSAGE, new ImageIcon("icon.png"), obj, "1");
		return Integer.parseInt(str_time) * 60; // 获取选择的分钟数并转化成秒数
	}

	public static void initChess(JPanel chessPanel, Player player1, Player player2, MouseClickEvent mouseEvent) {
		Chess chess;
		ChessLabel chessLabel;

		// 黑车1
		chess = new Rook(ChessBoardConstants.PLAYER2, ChessConstants.ROOK, new Position(0, 0));
		chessLabel = new ChessLabel(chess, "image\\黑车.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑车2
		chess = new Rook(ChessBoardConstants.PLAYER2, ChessConstants.ROOK, new Position(8, 0));
		chessLabel = new ChessLabel(chess, "image\\黑车.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑马1
		chess = new Knight(ChessBoardConstants.PLAYER2, ChessConstants.KNIGHT, new Position(1, 0));
		chessLabel = new ChessLabel(chess, "image\\黑马.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑马2
		chess = new Knight(ChessBoardConstants.PLAYER2, ChessConstants.KNIGHT, new Position(7, 0));
		chessLabel = new ChessLabel(chess, "image\\黑马.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑象1
		chess = new Bishop(ChessBoardConstants.PLAYER2, ChessConstants.BISHOP, new Position(2, 0));
		chessLabel = new ChessLabel(chess, "image\\黑象.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑象2
		chess = new Bishop(ChessBoardConstants.PLAYER2, ChessConstants.BISHOP, new Position(6, 0));
		chessLabel = new ChessLabel(chess, "image\\黑象.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑士1
		chess = new Guard(ChessBoardConstants.PLAYER2, ChessConstants.GUARD, new Position(3, 0));
		chessLabel = new ChessLabel(chess, "image\\黑士.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑士2
		chess = new Guard(ChessBoardConstants.PLAYER2, ChessConstants.GUARD, new Position(5, 0));
		chessLabel = new ChessLabel(chess, "image\\黑士.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑将
		chess = new King(ChessBoardConstants.PLAYER2, ChessConstants.KING, new Position(4, 0));
		chessLabel = new ChessLabel(chess, "image\\黑将.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑炮1
		chess = new Connon(ChessBoardConstants.PLAYER2, ChessConstants.CONNON, new Position(1, 2));
		chessLabel = new ChessLabel(chess, "image\\黑炮.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑炮2
		chess = new Connon(ChessBoardConstants.PLAYER2, ChessConstants.CONNON, new Position(7, 2));
		chessLabel = new ChessLabel(chess, "image\\黑炮.GIF");
		player2.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 黑卒
		for (int i = 0; i < 9; i += 2) {
			chess = new Pawn(ChessBoardConstants.PLAYER2, ChessConstants.PAWN, new Position(i, 3));
			chessLabel = new ChessLabel(chess, "image\\黑卒.GIF");
			player2.addChess(chessLabel);
			chessLabel.addMouseListener(mouseEvent);
			chessPanel.add(chessLabel);
		}

		// 红车1
		chess = new Rook(ChessBoardConstants.PLAYER1, ChessConstants.ROOK, new Position(0, 9));
		chessLabel = new ChessLabel(chess, "image\\红车.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红车2
		chess = new Rook(ChessBoardConstants.PLAYER1, ChessConstants.ROOK, new Position(8, 9));
		chessLabel = new ChessLabel(chess, "image\\红车.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红马1
		chess = new Knight(ChessBoardConstants.PLAYER1, ChessConstants.KNIGHT, new Position(1, 9));
		chessLabel = new ChessLabel(chess, "image\\红马.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红马2
		chess = new Knight(ChessBoardConstants.PLAYER1, ChessConstants.KNIGHT, new Position(7, 9));
		chessLabel = new ChessLabel(chess, "image\\红马.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红象1
		chess = new Bishop(ChessBoardConstants.PLAYER1, ChessConstants.BISHOP, new Position(2, 9));
		chessLabel = new ChessLabel(chess, "image\\红象.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红象2
		chess = new Bishop(ChessBoardConstants.PLAYER1, ChessConstants.BISHOP, new Position(6, 9));
		chessLabel = new ChessLabel(chess, "image\\红象.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红士1
		chess = new Guard(ChessBoardConstants.PLAYER1, ChessConstants.GUARD, new Position(3, 9));
		chessLabel = new ChessLabel(chess, "image\\红士.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红士2
		chess = new Guard(ChessBoardConstants.PLAYER1, ChessConstants.GUARD, new Position(5, 9));
		chessLabel = new ChessLabel(chess, "image\\红士.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红将
		chess = new King(ChessBoardConstants.PLAYER1, ChessConstants.KING, new Position(4, 9));
		chessLabel = new ChessLabel(chess, "image\\红将.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红炮1
		chess = new Connon(ChessBoardConstants.PLAYER1, ChessConstants.CONNON, new Position(1, 7));
		chessLabel = new ChessLabel(chess, "image\\红炮.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红炮2
		chess = new Connon(ChessBoardConstants.PLAYER1, ChessConstants.CONNON, new Position(7, 7));
		chessLabel = new ChessLabel(chess, "image\\红炮.GIF");
		player1.addChess(chessLabel);
		chessLabel.addMouseListener(mouseEvent);
		chessPanel.add(chessLabel);

		// 红兵
		for (int i = 0; i < 9; i += 2) {
			chess = new Pawn(ChessBoardConstants.PLAYER1, ChessConstants.PAWN, new Position(i, 6));
			chessLabel = new ChessLabel(chess, "image\\红卒.GIF");
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
			JOptionPane.showConfirmDialog(null, "黑棋胜利", "玩家二胜利", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
		} else if (loser == ChessBoardConstants.PLAYER2) {
			JOptionPane.showConfirmDialog(null, "红棋胜利", "玩家一胜利", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showConfirmDialog(null, "平局", "握手言和", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
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
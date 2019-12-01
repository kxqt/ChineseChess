package game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.ChessBoardConstants;
import data.EventHandler;
import data.GameData;
import data.Player;
import events.MouseClickEvent;
import ui.ChessLabel;
import ui.GameFrame;

public class Review implements EventHandler {
	ReturnMainGame mgame;//与Game交流的接口

	// 以下变量与Game类似，前缀rv代表复盘用
	GameFrame reviewFrame;

	Player rvP1;
	Player rvP2;
	GameData rvData;// 原本走棋记录
	GameData rvChange;// 变化走棋记录

	JPanel rvChessPanel;
	JPanel rvButtonPanel;

	JLabel rvChessBoardLabel;

	Thread rvChessFlash;

	MouseClickEvent rvMouseEvent;

	JButton forwardButton;// 后一步按钮
	JButton backwardButton;// 前一步按钮
	JButton toTopButton;// 跳至开局按钮
	JButton toButtomButton;// 跳至局末按钮
	JButton returnToTrackButton;// 退出变化按钮
	JButton exitButton;// 退出复盘按钮

	public Review(String[] reviewMessage, final ReturnMainGame mgame) {
		this.mgame = mgame;

		rvMouseEvent = new MouseClickEvent(this);

		rvP1 = new Player();
		rvP2 = new Player();
		rvData = new GameData(rvP1, rvP2);
		rvChange = new GameData(rvP1, rvP2);

		rvChessPanel = new JPanel();
		rvChessPanel.setLayout(null);
		rvChessPanel.setSize(ChessBoardConstants.BOARD_WIDTH, ChessBoardConstants.BOARD_HEIGHT);

		rvButtonPanel = new JPanel();
		rvButtonPanel.setBackground(Color.PINK);

		Game.initChess(rvChessPanel, rvP1, rvP2, rvMouseEvent);

		// 棋盘
		rvChessBoardLabel = new JLabel(new ImageIcon("image\\Main.GIF"));
		rvChessBoardLabel.setBounds(0, 0, ChessBoardConstants.BOARD_WIDTH, ChessBoardConstants.BOARD_HEIGHT);
		rvChessBoardLabel.addMouseListener(rvMouseEvent);
		rvChessPanel.add(rvChessBoardLabel);

		rvData.stringToLog(reviewMessage);

		// 按钮
		forwardButton = new JButton("后一步");
		forwardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rvChange.getLogs().length == 0) {
					rvData.forward();
				}
			}
		});
		rvButtonPanel.add(forwardButton);

		backwardButton = new JButton("前一步");
		backwardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rvChange.getLogs().length == 0) {
					rvData.backward();
				} else {
					rvChange.repent();
				}
			}
		});
		rvButtonPanel.add(backwardButton);

		toTopButton = new JButton("跳至开局");
		toTopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				while (rvChange.getLogs().length > 0) {
					rvChange.repent();
				}
				while (rvData.getLocation() >= 0) {
					rvData.backward();
				}
			}
		});
		rvButtonPanel.add(toTopButton);

		toButtomButton = new JButton("跳至局末");
		toButtomButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				while (rvChange.getLogs().length > 0) {
					rvChange.repent();
				}
				while (rvData.getLocation() < rvData.getLogs().length - 1) {
					rvData.forward();
				}
			}
		});
		rvButtonPanel.add(toButtomButton);

		returnToTrackButton = new JButton("退出变化");
		returnToTrackButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				while (rvChange.getLogs().length > 0) {
					rvChange.repent();
				}
			}
		});
		rvButtonPanel.add(returnToTrackButton);

		exitButton = new JButton("退出打谱");
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reviewFrame.dispose();
				mgame.returnMainGame();
			}
		});
		rvButtonPanel.add(exitButton);

		rvChessFlash = new Thread(rvChange);
		rvChessFlash.start();

		JPanel rvTimePanel = new JPanel();
		rvTimePanel.setBackground(Color.CYAN);
		reviewFrame = new GameFrame(rvChessPanel, rvButtonPanel, rvTimePanel, "复盘ing...",
				new ImageIcon("image\\红将.GIF"));
	}

	@Override
	public void ClickPosition(int x, int y) {
		rvChange.ClickPosition(x, y);
	}

	@Override
	public void ClickChess(ChessLabel chessLabel) {
		if (rvChange.getLogs().length == 0 && rvChange.getChosenChess() == null) {
			for (ChessLabel cl : rvData.getCurrentPlayer().getChesses()) {
				if (cl == chessLabel) {
					rvChange.setCurrentPlayer(rvData.getCurrentPlayer());
					rvChange.setChosenChess(chessLabel);
				}
			}
		} else {
			rvChange.ClickChess(chessLabel);
		}
	}

}
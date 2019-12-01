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
	ReturnMainGame mgame;//��Game�����Ľӿ�

	// ���±�����Game���ƣ�ǰ׺rv��������
	GameFrame reviewFrame;

	Player rvP1;
	Player rvP2;
	GameData rvData;// ԭ�������¼
	GameData rvChange;// �仯�����¼

	JPanel rvChessPanel;
	JPanel rvButtonPanel;

	JLabel rvChessBoardLabel;

	Thread rvChessFlash;

	MouseClickEvent rvMouseEvent;

	JButton forwardButton;// ��һ����ť
	JButton backwardButton;// ǰһ����ť
	JButton toTopButton;// �������ְ�ť
	JButton toButtomButton;// ������ĩ��ť
	JButton returnToTrackButton;// �˳��仯��ť
	JButton exitButton;// �˳����̰�ť

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

		// ����
		rvChessBoardLabel = new JLabel(new ImageIcon("image\\Main.GIF"));
		rvChessBoardLabel.setBounds(0, 0, ChessBoardConstants.BOARD_WIDTH, ChessBoardConstants.BOARD_HEIGHT);
		rvChessBoardLabel.addMouseListener(rvMouseEvent);
		rvChessPanel.add(rvChessBoardLabel);

		rvData.stringToLog(reviewMessage);

		// ��ť
		forwardButton = new JButton("��һ��");
		forwardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rvChange.getLogs().length == 0) {
					rvData.forward();
				}
			}
		});
		rvButtonPanel.add(forwardButton);

		backwardButton = new JButton("ǰһ��");
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

		toTopButton = new JButton("��������");
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

		toButtomButton = new JButton("������ĩ");
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

		returnToTrackButton = new JButton("�˳��仯");
		returnToTrackButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				while (rvChange.getLogs().length > 0) {
					rvChange.repent();
				}
			}
		});
		rvButtonPanel.add(returnToTrackButton);

		exitButton = new JButton("�˳�����");
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
		reviewFrame = new GameFrame(rvChessPanel, rvButtonPanel, rvTimePanel, "����ing...",
				new ImageIcon("image\\�콫.GIF"));
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
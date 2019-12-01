package ui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import chess.Chess;
import constants.ChessBoardConstants;
import constants.ChessConstants;

public class ChessLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private Chess chess;

	public ChessLabel(Chess chess, String ImageRoot) {
		super(new ImageIcon(ImageRoot));
		this.chess = chess;
		this.update();
	}

	public Chess getChess() {
		return chess;
	}

	// 棋子标签更新数据
	public void update() {
		super.setBounds(
				ChessBoardConstants.BOARD_BASE_X + this.chess.getPos().getX() * ChessBoardConstants.BOARD_STEP_X,
				ChessBoardConstants.BOARD_BASE_Y + this.chess.getPos().getY() * ChessBoardConstants.BOARD_STEP_Y,
				ChessConstants.CHESS_LABEL_WIDTH, ChessConstants.CHESS_LABEL_HEIGHT);
	}

	// 棋子复活
	public void birth() {
		setVisible(true);
	}

	// 棋子死亡
	public void die() {
		setVisible(false);
	}

	public boolean isAlive() {
		return isVisible();
	}
}

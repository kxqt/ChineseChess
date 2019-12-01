package data;

import ui.ChessLabel;

public interface EventHandler {
	public abstract void ClickPosition(int x, int y);
	
	public abstract void ClickChess(ChessLabel chessLabel);
}

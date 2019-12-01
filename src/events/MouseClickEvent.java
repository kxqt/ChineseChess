package events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import constants.ChessBoardConstants;
import constants.ChessConstants;
import data.EventHandler;
import ui.ChessLabel;

public class MouseClickEvent implements MouseListener {

	EventHandler handler;
	
	public MouseClickEvent(EventHandler handler) {
		super();
		this.handler = handler;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getSource() instanceof ChessLabel) {
			handler.ClickChess((ChessLabel)event.getSource());
		} else {
			double clickX = (double)(event.getX() - ChessBoardConstants.BOARD_BASE_X - ChessConstants.CHESS_LABEL_WIDTH / 2) / (double)ChessBoardConstants.BOARD_STEP_X;
			double clickY = (double)(event.getY() - ChessBoardConstants.BOARD_BASE_Y - ChessConstants.CHESS_LABEL_HEIGHT / 2) / (double)ChessBoardConstants.BOARD_STEP_Y;
			if ((clickX - (int)clickX < 0.3 || clickX - (int)clickX > 0.7) && (clickY - (int)clickY < 0.3 || clickY - (int)clickY > 0.7)) {
				int x = (int)(clickX + 0.5);
				int y = (int)(clickY + 0.5);
				handler.ClickPosition(x, y);
			}
		}
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) { }

	@Override
	public void mouseReleased(MouseEvent arg0) { }

}

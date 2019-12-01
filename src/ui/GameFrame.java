package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import constants.ChessBoardConstants;

public class GameFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public GameFrame(JPanel chessPanel, JPanel buttonPanel, JPanel timePanel, String string, ImageIcon icon) {
		super(string);
		super.setIconImage(icon.getImage());
		this.setResizable(false);
		this.setSize(ChessBoardConstants.FRAME_WIDTH, ChessBoardConstants.FRAME_HEIGHT);
		
		this.setLayout(new BorderLayout());
		
		this.add(BorderLayout.CENTER, chessPanel);
		this.add(BorderLayout.EAST, timePanel);
		this.add(BorderLayout.SOUTH, buttonPanel);
		
		//´°Ìå¾ÓÖÐ
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		
		if (frameSize.height > screenSize.height){
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width){
			frameSize.width = screenSize.width;
		}
		
		this.setLocation((screenSize.width - frameSize.width) / 2,(screenSize.height - frameSize.height ) / 2);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
}

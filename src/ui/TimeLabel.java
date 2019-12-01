package ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

import constants.ChessBoardConstants;
import data.GameTime;
import game.GameOver;

public class TimeLabel extends JLabel implements Runnable {
	
	private static final long serialVersionUID = 1L;
	GameTime time;
	GameOver gameOver;
	int player;

	public TimeLabel(GameTime time, int player, GameOver gameOver) {
		super();
		this.time = time;
		this.gameOver = gameOver;
		this.player = player;
		this.setPreferredSize(new Dimension(200, ChessBoardConstants.BOARD_HEIGHT / 2));
		this.setFont(new Font("宋体", Font.PLAIN, 28));
		this.update();
	}
	
	public void setRunning (boolean running) {
		time.setRunning(running);
	}
	
	// 时间标签更新
	public void update() {
		int hour = (time.getTime() / 3600) % 100;
		int minute = (time.getTime() / 60) % 60;
		int second = time.getTime() % 60;
		if (player == ChessBoardConstants.PLAYER1) {
			super.setText(String.format("<html><body><p><center>红方</center></p><br><p>%02d : %02d : %02d</p><body></html>", hour, minute, second));
		} else if (player == ChessBoardConstants.PLAYER2) {
			super.setText(String.format("<html><body><p><center>黑方</center></p><br><p>%02d : %02d : %02d</p><body></html>", hour, minute, second));
		} else {
			super.setText("player error!");
		}
	}

	// 响应时间倒计时线程
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(time.getTime() > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (time.isRunning()) {
				time.countdown();
				this.update();
			}
		}
		gameOver.gameOver();
	}
	
}

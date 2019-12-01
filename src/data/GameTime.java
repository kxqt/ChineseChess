package data;

public class GameTime {
	private int time;
	private boolean running;

	public GameTime(int hour, int minute, int second) {
		super();
		this.time = hour * 3600 + minute * 60 + second;
		running = false;
	}

	public GameTime(int time) {
		super();
		this.time = time;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getTime() {
		return time;
	}

	// µ¹Êı
	public boolean countdown() {
		if (time > 0) {
			time--;
			return true;
		} else {
			return false;
		}
	}

}

package com.example.hangoverr;

public class Settings {
	private int timeFreq;
	private int timeAsk;
	
	public Settings(){
		timeAsk = 0;
		timeFreq = 0;
	}

	public int getTimeFreq() {
		return timeFreq;
	}

	public void setTimeFreq(int timeFreq) {
		this.timeFreq = timeFreq;
	}

	public int getTimeAsk() {
		return timeAsk;
	}

	public void setTimeAsk(int timeAsk) {
		this.timeAsk = timeAsk;
	}

}

package com.eiw.client.dto;

public class IOData implements java.io.Serializable {

	private int io;
	private String ioname;
	private String max;
	private String min;

	public int getIO() {
		return io;
	}

	public void setIO(int io) {
		this.io = io;
	}

	public String getIoname() {
		return ioname;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMax() {
		return max;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMin() {
		return min;
	}

	public void setIoname(String ioname) {
		this.ioname = ioname;
	}
}

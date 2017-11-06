package com.SELC;

public class Timer {
	
	private float mspsCycle;
	
	private float exceedCycles;
	
	private boolean isPaused;
	
    private long lastUpgrade;
	
	private int slipCycle; 
	
	public Timer(float cyclesPerSecond) {
		setCSPS(cyclesPerSecond);
		reset();
	}
	
	public void setCSPS(float cyclesPerSecond) {
		this.mspsCycle = (1.0f / cyclesPerSecond) * 1000;
	}
	
	public void reset() {
		this.slipCycle = 0;
		this.exceedCycles = 0.0f;
		this.lastUpgrade = getCurrentTime();
		this.isPaused = false;
	}
	
	public void update() {
		long currUpdate = getCurrentTime();
		float delta = (float)(currUpdate - lastUpgrade) + exceedCycles;	
		if(!isPaused) {
			this.slipCycle += (int)Math.floor(delta / mspsCycle);
			this.exceedCycles = delta % mspsCycle;
		}
	
		this.lastUpgrade = currUpdate;
	}
	
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public boolean hasSlipCycle() {
		if(slipCycle > 0) {
			this.slipCycle--;
			return true;
		}
		return false;
	}
	
	public boolean peekSlipCycle() {
		return (slipCycle > 0);
	}
	
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);
	}
}

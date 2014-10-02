package com.techstorm.androidgame.kingdomdefender;

public class Counter {

	private int number;
	
	public Counter() {
		number = 0;
	}
	
	public Counter(int number) {
		this.number = number;
	}
	
	
	public void increase(int number) {
		this.number += number;
	}
	
	public int getNumber() {
		return this.number;
	}
}

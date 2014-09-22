package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

public class Wave {
	public List<Monster> monsters;
	
	public Wave() {
		monsters = new ArrayList<Monster>();
	}
	
	public void addMonster(Monster monster) {
		this.monsters.add(monster);
	}
}

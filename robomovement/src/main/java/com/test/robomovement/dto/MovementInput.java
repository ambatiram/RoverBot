package com.test.robomovement.dto;

import java.util.List;

public class MovementInput {
	
	public Position position;
	public List<Move> move;
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public List<Move> getMove() {
		return move;
	}
	public void setMove(List<Move> move) {
		this.move = move;
	}
	
	

}

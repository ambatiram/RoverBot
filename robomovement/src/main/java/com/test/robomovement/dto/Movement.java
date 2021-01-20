package com.test.robomovement.dto;

public enum Movement {

	FORWARD("F"), BACKWARD("B");
	
	private String movementText;

	Movement(String movementText){this.movementText = movementText;}

    public String getMovementText(){return this.movementText;}

    public static Movement fromMovementText(String movementText){
        for(Movement r : Movement.values()){
            if(r.getMovementText().equals(movementText)){
                return r;
            }
        }
        throw new IllegalArgumentException();
    }
}

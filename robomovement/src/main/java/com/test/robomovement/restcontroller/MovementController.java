package com.test.robomovement.restcontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.test.robomovement.dto.Move;
import com.test.robomovement.dto.MovementInput;
import com.test.robomovement.dto.Position;

@RestController
public class MovementController {
	
	@RequestMapping(value="/saveMovement", method=RequestMethod.POST)
	public Position saveMovement(@RequestBody(required=false) MovementInput movement) {
		
		Boolean status = saveMovements(movement);
		
		return getCurrentPosition();
	}
	
	private Position getCurrentPosition() {
		File file = new File("output.xml");
	    XmlMapper xmlMapper = new XmlMapper();
	    Position position = null;
	    String xml;
		try {
			xml = inputStreamToString(new FileInputStream(file));
		    position = xmlMapper.readValue(xml, Position.class);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return position;
	}

	private Boolean saveMovements(MovementInput movement) {
		
		Position position = updateCurrentPosition(movement);
		XmlMapper xmlMapper = new XmlMapper();
	    try {
			xmlMapper.writeValue(new File("input.xml"), movement);
			xmlMapper.writeValue(new File("output.xml"), position);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private String convertLeftString(String directionCode) {
		String directionText = "NESW";
		String firstHalf = directionText.substring(0, directionText.indexOf(directionCode)+1);
		String secondHalf = directionText.substring(directionText.indexOf(directionCode)+1, directionText.length());
		return (new StringBuilder(firstHalf).reverse().toString())+(new StringBuilder(secondHalf).reverse().toString());
	}

	private String convertRightString(String directionCode) {
		String directionText = "NESW";
		StringBuilder directionBuilder = new StringBuilder();
		directionBuilder.append(directionText);
		
		return directionBuilder.substring(directionBuilder.indexOf(directionCode))+directionText.substring(0, directionBuilder.indexOf(directionCode));
	
	}
	
	private Position updateCurrentPosition(MovementInput movement) {
		
		int currentX = movement.getPosition().getX();
		int currentY = movement.getPosition().getY();
		String currentDirection = movement.getPosition().getDirection();
		List<Move> moves = movement.getMove();
		List<Move> sortedMoves = moves.stream().sorted(Comparator.comparingInt(m -> Integer.valueOf(m.getO()))).collect(Collectors.toList());
		
		for(Move move : sortedMoves) {
			if(move.getL() != null) {
				String [] directionChars = convertLeftString(currentDirection).split("");
				currentDirection = directionChars[ (int)Math.round((  ((double)move.getL() % 360) / 90)) % 4 ];

				if(move.getF() != null) {
					currentX = currentX - move.getF();
					currentY = currentY + move.getF();
				}else if(move.getB() != null) {
					currentY = currentY + move.getB();
					currentX = currentX + move.getB();
				}
			}else if(move.getR() != null) {
				String [] directionChars = convertRightString(currentDirection).split("");
				currentDirection = directionChars[ (int)Math.round((  ((double)move.getR() % 360) / 90)) % 4 ];
				
				if(move.getB() != null) {
					currentX = currentX - move.getB();
					currentY = currentY + move.getB();
				}else if(move.getF() != null) {
					currentY = currentY + move.getF();
					currentX = currentX - move.getF();
				}
			}
		}
		
		
		Position pos = new Position();
		pos.setDirection(currentDirection);
		pos.setX(currentX);
		pos.setY(currentY);
		return pos;
	}
	
	private String inputStreamToString(InputStream is) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    String line;
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    while ((line = br.readLine()) != null) {
	        sb.append(line);
	    }
	    br.close();
	    return sb.toString();
	}

	@RequestMapping(value="/getCurrentPosition", method=RequestMethod.GET)
	public Position getPosition() {
		
		return getCurrentPosition();
	}
	

}

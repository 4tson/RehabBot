package mx.fortson.rehab.enums;

import java.awt.Color;

public enum ColorsEnum {
	
	OSRSORANGE(new Color(255,152,31,255)),
	;

	private Color color;

	public Color getColor() {
		return color;
	}

	private ColorsEnum(Color color) {
		this.color = color;
	}
	
	
}

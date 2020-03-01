import java.awt.Color;

public enum ByteFourColorEnum {

	BLACK(0x000000),
	BLUE(0x0000AA),
	GREEN(0x00AA00),
	CYAN(0x00AAAA),
	RED(0xAA0000),
	MAGENTA(0xAA00AA),
	BROWN(0xAA5500),
	LIGHTGRAY(0xAAAAAA),
	
	DARKGRAY(0x555555),
	LIGHTBLUE(0x5555FF),
	LIGHTGREEN(0x55FF55),
	LIGHTCYAN(0x55FFFF),
	LIGHTRED(0xFF5555),
	LIGHTMAGENTA(0xFF55FF),
	YELLOW(0xFFFF55),
	WHITE(0xFFFFFF);
	
	private int color;
	
	
	private ByteFourColorEnum() {};
	
	private ByteFourColorEnum(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	
	
	
	
}

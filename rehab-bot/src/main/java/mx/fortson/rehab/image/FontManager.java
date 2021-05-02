package mx.fortson.rehab.image;

import java.awt.Font;
import java.awt.Graphics;
import java.io.InputStream;

import mx.fortson.rehab.enums.FontsEnum;

public final class FontManager {
	
	public static final Font OSRS_FONT;
	
	static {
		OSRS_FONT = initFont(FontsEnum.OSRS.getFileName());
	}
	
	public static Font scaleFontToFit(String text, int width, Graphics g, Font pFont)
	{
	    float fontSize = pFont.getSize();
	    float fWidth = g.getFontMetrics(pFont).stringWidth(text);
	    if(fWidth <= width) {
	        return pFont;
	    }
	    fontSize = ((float)width / fWidth) * fontSize;
	    return pFont.deriveFont(fontSize);
	}

	private static Font initFont(String fontFileName) {
		try(InputStream is = InventoryImage.class.getResourceAsStream(fontFileName)){
			return Font.createFont(Font.TRUETYPE_FONT,is).deriveFont(15f);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

package mx.fortson.rehab.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import mx.fortson.rehab.bean.Degen;
import mx.fortson.rehab.bean.PagedImageMessageBean;
import mx.fortson.rehab.enums.ImagesEnum;
import mx.fortson.rehab.utils.FormattingUtils;

public class LeaderboardImage {

	public static PagedImageMessageBean generateLeaderboardImage(List<Degen> leaderBoard) throws IOException{
		BufferedImage leaderBoardBase;
		try(InputStream is = InventoryImage.class.getResourceAsStream(ImagesEnum.LEADERBOARD.getFileName())) {
			leaderBoardBase = ImageIO.read(is);	
		}
		
		PagedImageMessageBean result = new PagedImageMessageBean();
		List<Degen> leftover = new ArrayList<>(leaderBoard);
		
		BufferedImage combined = new BufferedImage(leaderBoardBase.getWidth(), leaderBoardBase.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics graphic = combined.getGraphics();
		graphic.drawImage(leaderBoardBase,0,0,null);
		
		
		Font titleFont = FontManager.scaleFontToFit("LEADERBOARD", leaderBoardBase.getWidth(), graphic, graphic.getFont().deriveFont(Font.BOLD,15f));
		FontMetrics metrics = graphic.getFontMetrics(titleFont);
		int xS = (leaderBoardBase.getWidth()/2) - metrics.stringWidth("LEADERBOARD")/2;
		graphic.setFont(titleFont);
		graphic.setColor(Color.BLACK);
		graphic.drawString("LEADERBOARD", xS, 45);
		
		
		int x = 25;
		int y = 65;
		
		graphic.setFont(graphic.getFont().deriveFont(13f));
		
		int maxY = combined.getHeight() - 65 - 35 / graphic.getFontMetrics().getHeight();
		
		int nameWidth = 90;
		int amountWidth = graphic.getFontMetrics().stringWidth("Bank") + 10;
		int farmWidth = graphic.getFontMetrics().stringWidth("99999") + 10;
		int timesFarmedWidth = graphic.getFontMetrics().stringWidth("Farmed") + 10;
		int winLossWidth = graphic.getFontMetrics().stringWidth("999/999") + 10;
		
		
		graphic.drawString("Name", x, y);
		
		x = x + nameWidth;
		
		graphic.drawString("Bank", x, y);
		
		x = x + amountWidth;
		
		graphic.drawString("Peak", x, y);
		
		x = x + amountWidth;
		
		graphic.drawString("Farms", x, y);
		
		x = x + farmWidth;
		graphic.drawString("Farmed",x,y);
		
		x = x + timesFarmedWidth;
		graphic.setFont(graphic.getFont().deriveFont(13f));
		graphic.drawString("W/L", x, y);
		
		x = x + winLossWidth;
		
		graphic.drawString("Lv.",x,y);
		
		graphic.dispose();
		
		for(Degen degen : leaderBoard) {
			x = 25;
			y = y+graphic.getFontMetrics().getHeight();
			if(y>=maxY) {
				break;
			}
			
			graphic = combined.getGraphics();
			if(degen.isIronman()) {
				BufferedImage ironIndicator;
				try(InputStream ironIndIS = InventoryImage.class.getResourceAsStream(ImagesEnum.IRON_INDICATOR.getFileName())){
					ironIndicator = ImageIO.read(ironIndIS);
				}
				graphic.drawImage(ironIndicator,10,y-ironIndicator.getHeight(), null);
			}
			
			graphic.setFont(graphic.getFont().deriveFont(13f));
			graphic.setColor(Color.BLACK);
			graphic.drawString(degen.getName(), x, y);
			
			x = x + nameWidth;
			
			graphic.drawString(FormattingUtils.format(degen.getBank(),false), x, y);
			
			x = x + amountWidth;
			
			graphic.drawString(FormattingUtils.format(degen.getPeak(),false), x, y);
			
			x = x + amountWidth;
			
			graphic.drawString(String.valueOf(degen.getFarmAttempts()), x, y);
			
			x = x + farmWidth;
			
			graphic.drawString(String.valueOf(degen.getTimesFarmed()),x,y);
			
			x = x + timesFarmedWidth;
			
			graphic.drawString(degen.getWins() + "/" + degen.getLosses(), x, y);
			
			x = x + winLossWidth;
			
			graphic.drawString(String.valueOf(degen.getLevel()),x,y);
			
			graphic.dispose();
			
			leftover.remove(degen);
		}
		
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			ImageIO.write(combined, "png", baos);
			result.setLeftOverRecords(leftover);
			result.setImageBytes(baos.toByteArray());
		}
		
		return result;
	}

}

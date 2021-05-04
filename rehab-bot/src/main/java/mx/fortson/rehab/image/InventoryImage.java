package mx.fortson.rehab.image;

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

import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.PagedImageMessageBean;
import mx.fortson.rehab.enums.ColorsEnum;
import mx.fortson.rehab.enums.ImagesEnum;
import mx.fortson.rehab.utils.FormattingUtils;

public class InventoryImage {

	public static PagedImageMessageBean generateInventoryImage(List<ItemBean> inventory, String name) throws IOException {
		BufferedImage inventoryBase;
		try(InputStream is = InventoryImage.class.getResourceAsStream(ImagesEnum.INVENTORY.getFileName())) {
			inventoryBase = ImageIO.read(is);	
		}
		
		PagedImageMessageBean result = new PagedImageMessageBean();
		List<ItemBean> leftover = new ArrayList<>(inventory);
		
		BufferedImage combined = new BufferedImage(inventoryBase.getWidth(), inventoryBase.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics graphic = combined.getGraphics();
		graphic.drawImage(inventoryBase,0,0,null);
		graphic.dispose();
		
		String titleString = name + "'s inventory";
		addTitle(titleString,inventoryBase.getWidth(),combined.getGraphics());
		
		int xmax = inventoryBase.getWidth() -20;
		int ymax = inventoryBase.getHeight() - 100;
		int sizesX = xmax / 5;
		int sizesY = ymax / 4;
		int x = 10;
		int y = 44;
		
		Long totalValue = 0L;
		for(ItemBean item : inventory) {
			if(item.isForSale()) {
				addForSaleIndicator(combined.getGraphics(),x, y, sizesX, sizesY);
			}
			addItemId(combined.getGraphics(),String.valueOf(item.getItemID()),x,y,sizesX,item.isService(),sizesY);
			
			Long value = item.getValue();
			totalValue = totalValue + value;
			
			graphic = combined.getGraphics();
			BufferedImage itemImage;
			try(InputStream imageIS = InventoryImage.class.getResourceAsStream("/items" + item.getImageName().toLowerCase())){
				itemImage = ImageIO.read(imageIS);
			}
			int xIValue = (x + (sizesX/2)) - (itemImage.getWidth()/2);
			int yIValue = (y + (sizesY/2)) - (itemImage.getHeight()/2);
			graphic.drawImage(itemImage,xIValue,yIValue, null);
			
			String descriptor = null;
			if(item.isService()) {
				graphic.setFont(FontManager.OSRS_FONT.deriveFont(12f));
				String level = String.valueOf(item.getRequiredLevel());
				int xServiceLevel = (x + (sizesX/2))- graphic.getFontMetrics(graphic.getFont()).stringWidth(level)/2;
				int yServiceLevel = (y + (sizesY/2))- graphic.getFontMetrics(graphic.getFont()).getHeight()/2;
				graphic.drawString(level, xServiceLevel, yServiceLevel + 10);
				
				descriptor = item.getShortName();
				graphic.setFont(FontManager.OSRS_FONT.deriveFont(9f));
			}else {
				descriptor = FormattingUtils.format(value,false);
				graphic.setFont(FontManager.OSRS_FONT.deriveFont(13f));
			}
			
			int xSValue = (x + (sizesX/2))- graphic.getFontMetrics(graphic.getFont()).stringWidth(descriptor)/2;
			graphic.drawString(descriptor, xSValue, y+sizesY);
			graphic.dispose();
			x = x + sizesX;
			if(x>=xmax) {
				x = 10;
				y = y + sizesY;
			}
			leftover.remove(item);
			if(y >= ymax) {
				break;
			}
		 }
		String totalString = "Value(" + FormattingUtils.format(totalValue) + ")";
		addValue(combined.getGraphics(), totalString,inventoryBase.getWidth(),inventoryBase.getHeight());
		
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			ImageIO.write(combined, "png", baos);
			result.setLeftOverRecords(leftover);
			result.setImageBytes(baos.toByteArray());
		}
		
		return result;
	}

	private static void addValue(Graphics graphic, String totalString, int width, int height) {
		graphic.setFont(FontManager.OSRS_FONT);
		graphic.setColor(ColorsEnum.OSRSORANGE.getColor());
		graphic.drawString(totalString, (width/2) - graphic.getFontMetrics(FontManager.OSRS_FONT).stringWidth(totalString)/2, height - 15);
		graphic.dispose();
	}

	private static void addItemId(Graphics graphics, String idString,int x, int y, int sizesX,boolean service, int sizesY) {
		int xIDValue = 0;
		int yIDValue = 0 ;
		FontMetrics metrics = graphics.getFontMetrics(FontManager.OSRS_FONT.deriveFont(12f));
		graphics.setFont(FontManager.OSRS_FONT.deriveFont(12f));
		xIDValue=(x + (sizesX/2))- metrics.stringWidth(idString)/2;
		yIDValue = (y + (sizesY/2))- metrics.getHeight()/2 - 10;
		
		graphics.drawString(idString, xIDValue, yIDValue - 2);
		graphics.dispose();
	}

	private static void addTitle(String titleString, int width, Graphics graphic) {
		Font titleFont = FontManager.scaleFontToFit(titleString, width-20, graphic, FontManager.OSRS_FONT);
		FontMetrics metrics = graphic.getFontMetrics(titleFont);
		int xS = (width/2) - metrics.stringWidth(titleString)/2;
		graphic.setFont(titleFont);
		graphic.setColor(ColorsEnum.OSRSORANGE.getColor());
		graphic.drawString(titleString, xS, 20);
		graphic.dispose();
	}

	private static void addForSaleIndicator(Graphics graphics, int x, int y, int sizesX, int sizesY) throws IOException {
		BufferedImage shopIndicator;
		try(InputStream shopIndIS = InventoryImage.class.getResourceAsStream(ImagesEnum.SHOP_INDICATOR.getFileName())){
			shopIndicator = ImageIO.read(shopIndIS);
		}
		graphics.setColor(ColorsEnum.OSRSORANGE.getColor());
		graphics.drawRect(x, y, sizesX, sizesY);
		graphics.drawImage(shopIndicator,x,y, null);
		graphics.dispose();
	}

}

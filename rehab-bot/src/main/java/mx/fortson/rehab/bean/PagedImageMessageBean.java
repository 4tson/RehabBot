package mx.fortson.rehab.bean;

public class PagedImageMessageBean extends PagedMessageBean{

	private byte[] imageBytes;
	private String imageName;
	
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
}

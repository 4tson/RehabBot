package mx.fortson.rehab.bean;

public class PredeterminedServiceSaleBean {

	private boolean sale;
	private String flavourText;
	private ServiceBean service;
	
	public String getFlavourText() {
		return flavourText;
	}
	public void setFlavourText(String flavourText) {
		this.flavourText = flavourText;
	}
	public boolean isSale() {
		return sale;
	}
	public void setSale(boolean sale) {
		this.sale = sale;
	}
	public ServiceBean getService() {
		return service;
	}
	public void setService(ServiceBean service) {
		this.service = service;
	}
	
}

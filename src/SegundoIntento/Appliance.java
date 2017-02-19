package SegundoIntento;

import java.io.Serializable;

public class Appliance implements Serializable{

	/* 
	 * Class for representing appliances that will be exchanged between restlets.
	 * Note that we are talking about a Serializable class, and its data is going to be 
	 * transmitted over a link between two or more machines.
	 */
	
	private static final long serialVersionUID = 1L;
	
	private String type;
	
	private int status; //ON-OFF, OPENED-CLOSE and so on...
	
	private double range; //If we need to configure some continuous variable for the element
	
	//If we need more... private double range2;
	
	private String uniqueID;
	
	
	/* 
	 * We need this default constructor to be able to return the HTML response that uses 
	 * this class to represent the Address instance data into a template. Without this constructor
	 * I was receiving errors
	 */
	public Appliance(){
		super();
	}
	
	public Appliance(String type, int status, double range, String uniqueID){
		super();
		this.type = type;
		this.status = status;
		this.range = range;
		this.uniqueID = uniqueID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}

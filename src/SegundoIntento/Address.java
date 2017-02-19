package SegundoIntento;

import java.io.Serializable;

/* 
 * Class for representing addresses that will be stored inside the contacts data.
 * Note that we are talking about a Serializable class, and its data is going to be 
 * transmitted over a link between two or more machines.
 */

public class Address implements Serializable{
	
	/* 
	 * We need this default constructor to be able to return the HTML response that uses 
	 * this class to represent the Address instance data into a template. Without this constructor
	 * I was receiving errors
	 */
	
    public Address(){
    	super();
    }
	
	public Address(String line1, String line2, String zipCode, String city,
            String country) {
        super();
        this.line1 = line1;
        this.line2 = line2;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }
	
	private static final long serialVersionUID = 1L;

	private String line1;

    private String line2;

    private String zipCode;

    private String city;

    private String country;
    
    public String getLine1() {
  		return line1;
  	}

  	public void setLine1(String line1) {
  		this.line1 = line1;
  	}

  	public String getLine2() {
  		return line2;
  	}

  	public void setLine2(String line2) {
  		this.line2 = line2;
  	}

  	public String getZipCode() {
  		return zipCode;
  	}

  	public void setZipCode(String zipCode) {
  		this.zipCode = zipCode;
  	}

  	public String getCity() {
  		return city;
  	}

  	public void setCity(String city) {
  		this.city = city;
  	}

  	public String getCountry() {
  		return country;
  	}

  	public void setCountry(String country) {
  		this.country = country;
  	}
  	

}

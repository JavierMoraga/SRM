package SegundoIntento;

import java.io.Serializable;

public class Contact implements Serializable {

	/* 
	 * Class for representing contacts that will be stored inside the server.
	 * Note that we are talking about a Serializable class, and its data is going to be 
	 * transmitted over a link between two or more machines.
	 */
	private static final long serialVersionUID = 1L;
	
    private String firstName;

    private String lastName;

    private Address homeAddress;

    private int age;
    
    private String uniqueID;
    
    /* 
	 * We need this default constructor to be able to return the HTML response that uses 
	 * this class to represent the Contact instance data into a template. Without this constructor
	 * I was receiving errors
	 */
    public Contact(){
    	super();
    }
    
    public Contact(String firstName, String lastName, Address homeAddress,
            int age, String id) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeAddress = homeAddress;
        this.age = age;
        this.uniqueID = id;
    }


    public String getUniqueID() {
    	return uniqueID;
    }
    
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}

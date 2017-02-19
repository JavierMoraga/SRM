package SegundoIntento;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Contacts {
	
	/*
	 * With this class we define and manage the list of contacts that will be used inside
	 * our restlet service. Their methods are synchronized to prevent concurrency errors, such as memory inconsistency or thread interference.
	 */
	
	private Map<String, Contact> uniqueIDcontacts = new ConcurrentHashMap<String,Contact>(); //List of contacts, indexed by their uniqueID
	
	private static Contacts theInstance = new Contacts(); //Singleton instance at definition class to be sure that we only have one instance in our server, from which get contact information
		
	private Contacts(){
		//do nothing, we just want one instance of this class so we ensure with this that no other constructor is able to create one.
	}
	
	public void inicializa(){
		/*Valores iniciales*/
		Contact contact = new Contact("Pedro","Peterson",new Address("Plaza españa","-","46003","Valencia","España"),30,"PP30Val");
		Contact contact1 = new Contact("Marta","Perez",new Address("Xátiva","-","46007","Valencia","España"),25,"MP25Val");
		Contact contact2 = new Contact("Inma","Michaels",new Address("Colón","-","46004","Valencia","España"),23,"IM23Val");
		Contact contact3 = new Contact("Michael","Martins",new Address("La paz","-","45932","Madrid","España"),20,"MM20Mad");
		Contacts.getInstance().addContact(contact1);
		Contacts.getInstance().addContact(contact2);
		Contacts.getInstance().addContact(contact3);
		Contacts.getInstance().addContact(contact);
		/*Fin valores iniciales*/
	}
	
	/** 
	 * With this class method we can access to the only instance of this class in our server, so it will be used very often to obtain or modify
	 * data associated to contacts.
	 */
	public static Contacts getInstance(){
		return theInstance;
	}
	
	/**
	   * Add a new contact to our repository.
	   * Note that if a contact with the same unique ID is already present, it is overridden. 
	   * @param contact The contact to be added.
	   */
	  public synchronized void addContact(Contact contact) {
	    uniqueIDcontacts.put(contact.getUniqueID(), contact);
	  }
	  
	  /**
	   * Returns the Contact instance associated with uniqueID if one is present, or null if not 
	   * found.
	   * @param uniqueID The uniqueID to search for. 
	   * @return The Contact instance or null.
	   */
	  public synchronized Contact getContact(String uniqueID) {
	    return uniqueIDcontacts.get(uniqueID);
	  }
	  
	  /**
	   * Ensures the Contact represented by uniqueID is no longer in the database. 
	   * @param uniqueID The uniqueID to remove, if present. 
	   */
	  public synchronized void deleteContact(String uniqueID) {
		  if(uniqueIDcontacts.containsKey(uniqueID)) {
			  uniqueIDcontacts.remove(uniqueID);
		  }
	  }


	  /**
	   * Return a snapshot of the collection at the point in time that this was called.
	   * @return A collection of Contact instances.  
	   */
	  public synchronized Collection<Contact> getContacts () {
	    return uniqueIDcontacts.values();
	  }
}

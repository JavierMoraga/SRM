package SegundoIntento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Appliances {
	
	/*
	 * With this class we define and manage the list of floors(each containing at the same time a list of appliances) that will be used inside
	 * our restlet service. Their methods are synchronized to prevent concurrency errors, such as memory inconsistency or thread interference.
	 */
	
	private Map<String, Map<String,Appliance>> uniqueIDappliances = new ConcurrentHashMap<String,Map<String,Appliance>>(); //List of floors, each floor with a list of appliances
	
	private static Appliances theInstance = new Appliances(); //Singleton instance at definition class, retrieved from restlet components that need data from floors and appliances
		
	private Appliances(){
		//do nothing, we just want one instance of this class in our server so we ensure with this that no other constructor is able to create one.
	}
	
	/** 
	 * With this class method we can access to the only instance of this class in our server, so it will be used very often to obtain or modify
	 * data associated to our smarthome floors and appliances
	 */
	public static Appliances getInstance(){
		return theInstance;
	}
	
	/**To be able to initialize our list of appliances in order to test our application*/
	public void inicializa(){
		/*Valores para pruebas*/
		Appliance app1 = new Appliance("Luces",1,1,"LuzComedor");
		Appliance app2 = new Appliance("Persianas",1,1,"LuzComedor");
		Appliance app3 = new Appliance("Calefacción",1,1,"CalefaccionHogar");
		Appliance app4 = new Appliance("Puertas",1,1,"CerraduraEntrada");
		Appliances.getInstance().addAppliance("PrimerPiso", app1);
		Appliances.getInstance().addAppliance("SegundoPiso", app1);
		Appliances.getInstance().addAppliance("TercerPiso", app1);
		Appliances.getInstance().addAppliance("PrimerPiso", app2);
		Appliances.getInstance().addAppliance("SegundoPiso", app4);
		Appliances.getInstance().addAppliance("TercerPiso", app2);
		Appliances.getInstance().addAppliance("PrimerPiso", app3);
		Appliances.getInstance().addAppliance("SegundoPiso", app3);
		Appliances.getInstance().addAppliance("TercerPiso", app4);
		/*Fin valores para pruebas*/
	}
	
	/**
	   * Add a new Appliance to our repository.
	   * Note that if an appliance with the same unique ID is already present in the same floor, it is overridden. 
	   * @param floor The floor where to add the appliance.
	   * @param appliance The appliance to be added
	   */
	  public synchronized void addAppliance(String floor, Appliance appliance) {
		  Map<String,Appliance> aux = new ConcurrentHashMap<String,Appliance>(); //Auxiliar variable that will be used to retrieve a list of appliances in the given floor.
		  
		  if(uniqueIDappliances.containsKey(floor)){
			  aux = uniqueIDappliances.get(floor); //Returns null if no floor found, in this case we have ensured that the floor exists
			  aux.put(appliance.getUniqueID(), appliance); //We put the appliance inside the list, if it already exists it will be updated
			  uniqueIDappliances.put(floor, aux); //We update the list for that floor with our updated aux list.
		  } else {
			  aux.put(appliance.getUniqueID(), appliance); //If the floor didn't exist, we just have to add the appliance to our aux list
			  uniqueIDappliances.put(floor, aux); //Then we will put that list as a new floor in our list of floors.
		  }
	  }
	  
	  /**
	   * Returns the Appliance instance associated in the given floor with the given uniqueID if one is present, or null if not 
	   * found.
	   * @param floor The floor where to search.
	   * @param uniqueID The uniqueID to search in the given floor.
	   * @return The Appliance instance or null.
	   */
	  public synchronized Appliance getAppliance(String floor, String uniqueID) {
	    return uniqueIDappliances.get(floor).get(uniqueID); //We get the list for the floor we are searching in, inside of the returned list we search the appliance we need.
	  }
	  
	  /**
	   * Ensures the Appliance represented by uniqueID is no longer in the given floor. 
	   * @param floor The floor where to remove the Appliance.
	   * @param uniqueID The uniqueID to remove, if present. 
	   */
	  public synchronized void deleteAppliance(String floor, String uniqueID) {
		  Map<String,Appliance> aux = uniqueIDappliances.get(floor); //We get the list regarding to the floor marked
		  aux.remove(uniqueID); //We update it removing the appliance needed
		  uniqueIDappliances.put(floor, aux); //Put replaces under new occurrency, so the list will be correctly updated.
	  }
	  
	  /**
	   * I created this method to be able to delete a given floor if we empty 
	   * that floor from appliances, so it won't have any useful information
	   * @param floor Floor to be removed.*/
	  public synchronized void deleteFloor(String floor){
		  uniqueIDappliances.remove(floor);
	  }


	  /**
	   * Return a snapshot of the collection at the point in time that this was called.
	   * @param floor Floor where to get the collection of appliances
	   * @return A collection of Appliances for the given floor.  
	   */
	  public synchronized Collection<Appliance> getAppliances(String floor) {
		  if(uniqueIDappliances.isEmpty()) {
			  return new ArrayList<Appliance>(); //We return an empty list if no appliances are installed in our system
		  } else if (!uniqueIDappliances.containsKey(floor)){
			  return new ArrayList<Appliance>(); //We check that we have a floor in our system, if not, return an empty list
		  }
		  return uniqueIDappliances.get(floor).values(); //Finally, if we have appliances on the given floor we must return their uniqueIDs.
	  }
	  
	  /**
	   * Return a snapshot of the current floors that we have in our system
	   * @return A collection of floor names
	   * */
	  public synchronized Collection<String> getFloors(){
		  return uniqueIDappliances.keySet(); //We just get the list of keys representing the floors in our list
	  }
}

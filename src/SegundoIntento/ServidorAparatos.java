package SegundoIntento;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
/*Las dos siguientes para template HTML*/
import org.restlet.ext.freemarker.*;

/**With this resource we represent the Appliances that we host in our restlet application and how we interact with them
 * using REST
 * */
public class ServidorAparatos extends ServerResource implements ResourceApp {

	/**GET method from our interface, that tells the server what to do when it receives a get request
	 * @return The HTML representation or empty JSON representation (if the requested is not found)*/
	public Representation getResource() {
		/*Getting resource identifiers from the URI that originated the request*/
		String uniqueID = (String)this.getRequestAttributes().get("uniqueID");
		String floor = (String)this.getRequestAttributes().get("planta");
		System.out.println("Appliance requested: "+"floor "+floor+" uniqueID "+uniqueID);
		
		Appliance app = Appliances.getInstance().getAppliance(floor,uniqueID); //We check in our Appliances instance if we have one representation for this uniqueID. If no appliance is found it returns null

		Representation aux = new JacksonRepresentation<Appliance>(app); //We prepare our representation and we initialize it
		try {
			aux = toHTML(app); //We convert it to a representation that can be presented on a web browser, using freeMarker extension we can use a template to generate the html presented
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aux; //We return the representation to the requester.
	}
	
	/**
	 * To create the html document representing the Appliance
	 * @param app An instance of the appliance to be transformed in an HTML representation
	 * @return HTML representation or empty representation*/
	public Representation toHTML(Appliance app) throws Exception{
		if(app==null){
			return new JacksonRepresentation<Appliance>(app); //If the appliance didn't exist we return an empty representation
			}
		Map<String,Object> dataModel = new HashMap<String,Object>(); //If not we create the data model for our freemarker template
		dataModel.put("app", app); //We put the current appliance into the data model to be able to generate the html using its data
		dataModel.put("urlAnterior",this.getRequest().getHostRef().toString()+"/smarthome/"+(String)this.getRequestAttributes().get("planta")); //We also create a string that will allow us to go to the previous level on the URI
		Representation applianceTemplate = new ClientResource(LocalReference.createClapReference(getClass().getPackage())+ "/applianceTemplate").get(); //We create the resource using applianceTemplate
		return new TemplateRepresentation(applianceTemplate, dataModel, MediaType.TEXT_HTML);
	}

	/**
	 * The server will use this function everytime it receives a PUT request to this resource. It will store the Appliance received
	 * @param rep Representation of the resource obtained from a client that wants to store it
	 * */
	public void storeResource(Representation rep) throws IOException{
		JacksonRepresentation<Appliance> applianceRep = new JacksonRepresentation<Appliance>(rep,Appliance.class); //We transform the JSON representation into an Appliance using Jackson extension
		Appliance appliance = applianceRep.getObject(); //We get the appliance from the representation
		Appliances.getInstance().addAppliance((String)this.getRequestAttributes().get("planta"), appliance); //We store the representation received, if it already exists it will be overwritten.
		System.out.println("Appliance stored in floor "+(String)this.getRequestAttributes().get("planta")+" with uniqueID "+(String)this.getRequestAttributes().get("uniqueID"));
	}

	/**
	 * The server will use this function everytime it receives a DELETE request to this resource. It will delete the associated appliance from the appliance list
	 * @param uniqueID The id of the appliance to delete*/
	public void deleteResource(String uniqueID) {
		Appliances.getInstance().deleteAppliance((String)this.getRequestAttributes().get("planta"),(String)this.getRequestAttributes().get("uniqueID")); //Delete the appliance getting the floor and uniqueID from request URI
		System.out.println("Appliance deleted in floor "+(String)this.getRequestAttributes().get("planta")+" with uniqueID "+(String)this.getRequestAttributes().get("uniqueID"));
		if(Appliances.getInstance().getAppliances((String)this.getRequestAttributes().get("planta")).isEmpty()){ //Check if this made the floor become empty, if yes, delete also the floor from floor list.
			Appliances.getInstance().deleteFloor((String)this.getRequestAttributes().get("planta"));
			System.out.println("Floor "+(String)this.getRequestAttributes().get("planta")+" empty, we delete it");
		}
		
	}
	
	/**
	 * The server will use this function everytime it receives a POST request to refresh an appliance.
	 * @param entity Represents the refreshed appliance with parameters introduced in a form*/
	@Post
	public void postApplication(Representation entity){
		Form form = new Form(entity); //We transform the representation into a form instance
		/*We get the values associated to that form*/
		String uniqueID = (String)this.getRequestAttributes().get("uniqueID"); 
		String tipo = form.getValues("Type");
		int estado = Integer.parseInt(form.getValues("Estado"));
		double rango = Double.parseDouble(form.getValues("Rango"));
		/*We create the refreshed appliance*/
		Appliance aux = new Appliance(tipo,estado,rango,uniqueID);
		Appliances.getInstance().addAppliance((String)this.getRequestAttributes().get("planta"), aux); //We refresh that appliance
		System.out.println("Appliance updated in floor "+(String)this.getRequestAttributes().get("planta")+" with uniqueID "+(String)this.getRequestAttributes().get("uniqueID"));
		
	}
}

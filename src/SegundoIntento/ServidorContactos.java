package SegundoIntento;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.jackson.*;

public class ServidorContactos extends ServerResource implements ResourceApp{
	
	/**GET method from our interface, that tells the server what to do when it receives a get request
	 * @return The HTML representation or empty JSON representation (if the requested is not found)*/
	public Representation getResource() {
		String uniqueID = (String)this.getRequestAttributes().get("uniqueID"); //We see which contact is requested by looking in the request attributes
		Contact	contact = Contacts.getInstance().getContact(uniqueID); //We get the contact (null if not found).
		
		try {
			System.out.println("Delivered contact representation for: "+uniqueID);
			return toHTML(contact);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JacksonRepresentation<Contact>(contact);
	}
	
	/**
	 * To create the html document representing the Contact
	 * @param app An instance of the contact to be transformed in an HTML representation
	 * @return HTML representation or empty representation*/
	public Representation toHTML(Contact app) throws Exception{
		if(app==null){
			return new JacksonRepresentation<Contact>(app); //We see if the contact didn't exist, so we return an empty representation
			}
		Map<String,Object> dataModel = new HashMap<String,Object>(); //We create the datamodel for the template
		dataModel.put("app", app); //We put the contact instance into the data model 
		dataModel.put("urlAnterior",this.getRequest().getHostRef().toString()+"/contacts"); //We put the url reference that points to the previous level of the URI
		Representation applianceTemplate = new ClientResource(LocalReference.createClapReference(getClass().getPackage())+ "/contactTemplate").get(); //Use the contactTemplate to generate the html using freemarker extension and the data from the contact instance
		return new TemplateRepresentation(applianceTemplate, dataModel, MediaType.TEXT_HTML);
	}

	/**
	 * The server will use this function everytime it receives a PUT request to this resource. It will store the contact received
	 * @param rep Representation of the resource obtained from a client that wants to store it
	 * */
	public void storeResource(Representation rep) throws IOException{
		JacksonRepresentation<Contact> contactRep = new JacksonRepresentation<Contact>(rep,Contact.class); //We create the instance of a jacksonRepresentation using the JSON object received
		Contact contacto = contactRep.getObject(); //We transform it into a contact
		Contacts.getInstance().addContact(contacto); //We add that contact into our contact list
		System.out.println("Stored contact: "+contacto.getUniqueID());
	}

	/**
	 * The server will use this function everytime it receives a DELETE request to this resource. It will delete the associated contact from the appliance list
	 * @param uniqueID The id of the contact to delete*/
	public void deleteResource(String uniqueID) {
		Contacts.getInstance().deleteContact((String)this.getRequestAttributes().get("uniqueID")); //We delete the contacto from our contact list
		System.out.println("Deleted contact: "+uniqueID);
	}
	
	/**
	 * The server will use this function everytime it receives a POST request to refresh a contact
	 * @param entity Represents the refreshed contact with parameters introduced in a form*/
	@Post
	public void postContacto(Representation entity){
		Form form = new Form(entity); //We transform the representation into a form instance
		/*We get the values associated to that form*/
		String nombre = form.getValues("Nombre");
		String apellidos = form.getValues("Apellidos");
		String calleUno = form.getValues("Calle 1");
		String calleDos = form.getValues("Calle 2");
		String codigoPostal = form.getValues("Codigo Postal");
		String ciudad = form.getValues("Ciudad");
		String pais = form.getValues("Pais");
		int edad = Integer.parseInt(form.getValues("Edad"));
		String uniqueID = (String)this.getRequestAttributes().get("uniqueID");
		/*We create the refreshed contact*/
		Contact aux = new Contact(nombre,apellidos,new Address(calleUno,calleDos,codigoPostal,ciudad,pais),edad,uniqueID);
		Contacts.getInstance().addContact(aux); //We refresh that contact
		System.out.println("Updated contact: "+aux.getUniqueID());
		
	}

}

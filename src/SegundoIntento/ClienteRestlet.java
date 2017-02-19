package SegundoIntento;

import java.io.Console;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ClienteRestlet {
	  /**
	   * Connect to a running restlet service and perform the requested operation (we just use this for adding or removing elements). 
	   * @param args The first arg is the host (such as "http://localhost:8112/").
	   * The second arg is the operation, either "put", "get", or "delete". 
	   * The third arg is the unique ID of the contact.
	   * If the operation is a "put", then there must be three more arguments for 
	   * first name, last name, and info. 
	   * @throws Exception If problems occur.
	   */
	  public static void main(String[] args) throws Exception {
	    // Make sure we were at least passed the minimum number of arguments. 
	    if (args.length < 4) {
	      System.out.println("Usage: <host> put contacts <uniqueID> <firstName> <lastName> <age> <line1> <line2> <zipcode> <city> <country>");
	      System.out.println("Another usages: <host> put smarthome <floor> <uniqueID> <type> <status> <range>");
	      System.out.println("<host> delete contacts <uniqueID>");
	      System.out.println("<host> delete smarthome <floor> <uniqueID>");
	      System.out.println("Example host: http://localhost:8112");
	      return;
	    }
	    // (1) Figure out the URL from the args and make the ClientResource.
	    String host = args[0];
	    String operation = args[1];
	    String app = args[2];
	    String first = args[3];
	    String second ="";
	    String url;
	    if(app.equals("contacts")){
	    	url = host+"/contacts/"+first;
	    } else if(app.equals("smarthome")){
	    	second = args[4];
	    	url = host+"/smarthome"+"/"+first+"/"+second;
	    } else {
	    	url = host;
	    	System.out.println("Invalid URL, valid ones are:"
	    	+ "\nhttp://<host:port>/contacts/{uniqueID}"+"\nhttp://<host:port>/smarthome/{floor}/{uniqueID}");
	    	return;
	    }
	    
	    /*User credential parts*/
	    Console console = System.console();
	    System.out.println("Introduzca el usuario con el que conectar al servicio");
	    String usuario = console.readLine();
	    System.out.println("Introduzca la contraseña");
	    char[] password = console.readPassword();
	    
	    /*Try to access the client resource requested*/
	    ClientResource client = new ClientResource(url);
	    client.setChallengeResponse(new ChallengeResponse(ChallengeScheme.HTTP_BASIC, usuario, password)); //We set the authentication response for the server with the parameters requested using consoleio
	    try {
	    	if(app.equals("contacts")){
	    		/*
	    		if ("get".equals(operation)) { //If we want to perform a get
	    	        JacksonRepresentation<Contact> representation = new JacksonRepresentation<Contact>(client.get(),Contact.class);
	    	        Contact contact = representation.getObject();
	    	        System.out.println("Contacto obtenido"+contact.toString());
	    	      }*/
	    	      if ("delete".equals(operation)) {
	    	        client.delete();
	    	      }
	    	      else if ("put".equals(operation)) {
	    	        Contact contact = new Contact(args[4], args[5], new Address(args[7],args[8],args[9],args[10],args[11]),
	    	        		Integer.parseInt(args[6]),first);
	    	        JacksonRepresentation<Contact> representation = new JacksonRepresentation<Contact>(contact);
	    	        client.put(representation);
	    	      } else {
	    	    	  System.out.println("Bad operation, valids are put and delete");
	    	    	  return;
	    	    	  }
	    		
	    	} else if(app.equals("smarthome")){
	    		/*if ("get".equals(operation)) {
	    	        JacksonRepresentation<Appliance> representation = new JacksonRepresentation<Appliance>(client.get(),Appliance.class);
	    	        Appliance appliance = representation.getObject();
	    	        System.out.println("Appliance obtenido: "+appliance.toString());
	    	      }*/
	    	      if ("delete".equals(operation)) {
	    	        client.delete();
	    	      }
	    	      else if ("put".equals(operation)) {
	    	        Appliance appliance = new Appliance(args[5], Integer.parseInt(args[6]),Double.parseDouble(args[7]),second);
	    	        JacksonRepresentation<Appliance> representation = new JacksonRepresentation<Appliance>(appliance);
	    	        client.put(representation);
	    	      } else {
	    	    	  System.out.println("Bad operation, valids are put and delete");
	    	    	  return;
	    	    	  }
	    		
	    	} else {
	    		System.out.println("Bad service, valids are contacts and smarthome");
	    		return;
	    		}
	    	 
	      
	    }
	    catch (ResourceException e) {
	      //If the operation didn't succeed, indicate why here.
	      System.out.println("Error: " + e.getStatus());
	    }
	  }
	}



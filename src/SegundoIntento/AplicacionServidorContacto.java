package SegundoIntento;

import java.net.Inet4Address;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;

/*
 * Main application in this project, as we see it extends from a restlet Application
 * being able to start the restlet environment and to control all the resources that
 * this platform provides.
 */

public class AplicacionServidorContacto extends Application{
	
	/* Router uses a Finder to handle the instances of the resources created concurrently,
	 * this finder isn't directly manipulated, we implicitly create it in calls like
	 * new Server(Protocol.HTTP,8180,ServidorContactos.class) or router.attach("URI",class of the resource to instantiate)
	 * You can see in figure 2.11 from the book "Restlet in action" how the Finder works with those resources in each call
	 */
	
	/*Capitulo 4 del libro aparecen los tipos de representaciones que se tratan
	 * setExisting(false) para decir que el recurso no existe
	 */
	
	/* Representation class para consumir representaciones de los resources
	 * We normally use one of the subclasses of representations that narrow the scope
	 * of the representation given.
	 * Page 105 talks about how to produce and consume resources in a JSON representation
	 */
	
	/* Jackson extension to transform instances of the classes that we want to send in JSON notation
	 * directly, and to store it back into its object representation, recommended to use.
	 */
	
	/* Freemarker extension (page 110) to dynamically generate the preceding HTML representation for the JSON objects
	 * retrieved from our server. 
	 * With this we can set the template and we can point to another variables using links in our client
	 * You point to the template with a Configuration instance. This template can be reused
	 */
	
	/* In order to provide a resource state we can use a filesystem, combination of other resources, physical sensors 
	 * and so on.
	 * REST communications are stateless but REST resources are stateful so there are some options like RESTful databases
	 * like Apache CouchDB.
	 */
	
	/* ROA/D as a methodology that ensures you won't miss important steps during RESTful web projects,
	 * this is really valuable during inception and elaboration phases. 
	 * page 373 with figure D2 to see steps:
	 * -Requirements gathering
	 * -Requirements analysis
	 * -Solution design
	 * -Design implementation
	 * -Implementation testing
	 * 
	 * Inception: definition of the project vision producing a business case and the initial requirements.
	 * Define the overall feature scope and main use cases.
	 * In the business scope we must evaluate its feasibility.
	 * 
	 * Elaboration: Identify requirements, define a domain model, detail logical architecture and reduce the main risks
	 * by ensuring that the team is properly trained and organized.
	 * Several iterations due to the unstable state of the project. 
	 * Time for workshops and technical prototypes that can be reused subsets on the final system.
	 * At the end: Have an idea of the remaining effort and the resources needed to complete the project.
	 * 
	 * Construction: Active coding and steady progress because we should have all the uncertainties dissapeared and the team
	 * should be able to implement the remaining features. At the end of this the features should have been completely developed
	 * and the quality level should be good.
	 * 
	 * Transitioning the project: Bug fixing before production. Fixes only made after peer review because no new feature should be
	 * added (code should be frozen in this phase).
	 */
	
	/* Authentication, classes from org.restlet.security package used.
	 * ChallengeRequest: Authentication challenge sent by an origin server to a client.
	 * ChallengeResponse: Authentication response sent by a client to an origin server. HTTP header mapped
	 * is Authorization.
	 * ChallengeScheme: Challenge mechanism used to authenticate remote clients.
	 * 
	 * setChallengeResponse(challengeScheme,identifier,secret)
	 * */
	
	/* Virtual hosting allows you to bind the same IP address to several internet domain names.
	 * You will still be able to bind the same IP address to several internet domain names and still
	 * retail the ability to server each domain name with a separate Restlet application.
	 * You can also listen several IP addresses at the same time (for load-balancing or fail-over purposes).
	 */
	
	/* Utilizaré una máquina virtual para testear la comunicación remota dentro de la misma red.
	 * Para ello utilizo Ubuntu 12.04.5 bajo el entorno de máquina virtual que me proporciona VirtualBox.
	 * Si quiero comunicar mi máquina host con mi máquina virtual necesito configurar la conexión a Internet
	 * como adaptador de tipo bridged dentro de la VM. Por defecto esta viene como NAT, por ello cambio este
	 * hecho. Regenerare la MAC del adaptador virtual para evitar conflictos entre adaptadores de red.
	 * 
	 * */
	
	
	/* 
	 * We have this two static variables for our application representing in each moment the host and port 
	 * attached to our restlet server
	 * */
	static String host = "";
	static int port = 0;
		
	public static void main(String[] args) throws Exception{
		/*Parte virtual hosting (NO FUNCIONA)
		VirtualHost Vhost = new VirtualHost();
		Vhost.setHostDomain("www.srmrestletserver.com");
		Vhost.setServerAddress(Inet4Address.getLocalHost().getHostAddress());
		Vhost.setServerPort(Integer.toString(port));
		Vhost.attachDefault(new AplicacionServidorContacto());
		Vhost.start();	*/
		
		
		port = Integer.parseInt(args[0]); //We get the port where we want to offer the service from command line
		host = Inet4Address.getLocalHost().getHostAddress(); //We get the IPv4 address where to bound our server
		Server contactServer = new Server(Protocol.HTTP, port); //We create the HTTP server connector, and we bind it to the port obtained from command line
		contactServer.setNext(new AplicacionServidorContacto()); //We transfer all the calls we receive on this server to one instance of this restlet application
		contactServer.setAddress(host); //We set the IPv4 address where we want our server to listen. In this case we obtained it from our localhost using the Inet4Address class methods
		contactServer.start(); //We finally start our server, that will receive the incoming requests and will route them into their intended destination.
		System.out.println("Restlet server initiated in: "+contactServer.getAddress()+":"+contactServer.getPort());
		
		
		Contacts.getInstance().inicializa(); //We initialize our contacts resource, to initially have a default set of elements to test our service
		Appliances.getInstance().inicializa(); //We do the same with our appliances resource
	}
	
	@Override
	public Restlet createInboundRoot(){
		//Blocker blocker = new Blocker(getContext()); With this we can do that one or various client hosts won't be able to obtain data from our restlet Server
		Router router = new Router(getContext()); //We create a router instance, this restlet is in charge of redirecting the requests to their appropiate resources
		//blocker.setNext(router);		
		
		router.attach("http://"+host+":"+port+"/",ServidorPaginaPrincipal.class);
		router.attach("http://"+host+":"+port+"/contacts/{uniqueID}",ServidorContactos.class);
		router.attach("http://"+host+":"+port+"/contacts",ServidorListaContactos.class);
		router.attach("http://"+host+":"+port+"/smarthome",ServidorListaPlantas.class);
		router.attach("http://"+host+":"+port+"/smarthome/{planta}",ServidorListaAparatosPlanta.class);
		router.attach("http://"+host+":"+port+"/smarthome/{planta}/{uniqueID}",ServidorAparatos.class);
		
		/*Basic authentication system for our server*/
		ChallengeAuthenticator authenticator = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC, "My Realm"); //We set in here the method for authentication
		MapVerifier verifier = new MapVerifier(); //With this we will be able to verify that the data introduced corresponds to the correct user:password combination
		verifier.getLocalSecrets().put("UsuarioSRM", "password".toCharArray()); //Here we set the user and password needed to access the system using this verifier
		authenticator.setVerifier(verifier); //We attach the verifier to the authenticator created before
		authenticator.setNext(router); //After a correct authentication the router will be able to dispatch resources. In case of a bad authentication no access will be granted to the client host.
		
		return authenticator;
	}
}

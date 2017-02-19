package SegundoIntento;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;

/*
 * Preprocessing filter after receiving something in our server, if this filter is passed succesfully (your address isn't in the blacklist)
 *you will be able to access the next restlet(in our case the restlet resource).
 */

public class Blocker extends org.restlet.routing.Filter{
	
	private final Set<String> blockedAddresses;
	
	public Blocker (Context context){
		super(context);
		this.blockedAddresses = new CopyOnWriteArraySet<String>();
		//blockedAddresses.add("0:0:0:0:0:0:0:1"); //Bloqueo de mi propia IPv6 para prueba en local
	}
	
	
	/*Como en todos los Restlet, la entrada del filtro es el final handle(Request,Response) declarado en la interface Uniform.
	 * Este metodo tiene 3 pasos: 
	 * 
	 * -beforeHandle(Request, Response) es el primero y deja que el filtro preprocese la llamada. Su flag result indica si acceder
	 * o no al siguiente restlet.
	 * 
	 * -Si se pasa el filtro se realiza el doHandle(Request, Response) que invoca el siguiente restlet (si no hay Restlet asociado 
	 * se lanza "Error 500").
	 * 
	 * -Cuando sale del Restlet asociado se llama a afterHandle(Request, Response) que permite postproceso de la llamada (Un ejemplo
	 * comprimir la representación recibida de un HTTP GET).
	 */
	@Override
	protected int beforeHandle (Request request, Response response){
		int result = STOP;
		System.out.println("Entro en blocker, mi IP es "+request.getClientInfo().getAddress());
		if(getBlockedAddresses().contains(request.getClientInfo().getAddress())) {
			response.setStatus(Status.CLIENT_ERROR_FORBIDDEN,"Your IP was blocked");
		} else {
			result = CONTINUE;
		}
		return result;
	}
	
	public Set<String> getBlockedAddresses(){
		return blockedAddresses;
	}
}

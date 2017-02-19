package SegundoIntento;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;

public class ServidorPaginaPrincipal extends ServerResource implements ResourceApp{
	
	public Representation getResource() {
		try {
			System.out.println("Requested home page");
			return toHTML("Contacts y smarthome");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JacksonRepresentation<String>("No representations");
	}
	
	public Representation toHTML(String app) throws Exception{
		if(app==null){
			return new JacksonRepresentation<String>(app);
			}
		Map<String,Object> dataModel = new HashMap<String,Object>();
		dataModel.put("app", app);
		dataModel.put("url",this.getRequest().getHostRef().toString());
		Representation applianceTemplate = new ClientResource(LocalReference.createClapReference(getClass().getPackage())+ "/paginaInicioServicio").get();
		return new TemplateRepresentation(applianceTemplate, dataModel, MediaType.TEXT_HTML);
	}
	
	public void storeResource(Representation rep) throws IOException{
		; //No queremos añadir una lista de contactos nueva
	}
	
	public void deleteResource(String uniqueID){
		; //No queremos borrar la lista de contactos
	}
}

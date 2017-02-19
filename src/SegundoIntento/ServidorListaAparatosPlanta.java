package SegundoIntento;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;

public class ServidorListaAparatosPlanta extends ServerResource implements ResourceApp{
	
	public Representation getResource() {
		String floor = (String)this.getRequestAttributes().get("planta");
		try {
			System.out.println("Requested appliance list for floor "+floor);
			return toHTML(Appliances.getInstance().getAppliances(floor),floor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JacksonRepresentation<Collection<Appliance>>(Appliances.getInstance().getAppliances(floor));
	}
	
	public Representation toHTML(Collection<Appliance> app, String floor) throws Exception{
		if(app==null){
			return new JacksonRepresentation<Collection<Appliance>>(app);
			}
		Map<String,Object> dataModel = new HashMap<String,Object>();
		dataModel.put("app", app);
		dataModel.put("urlAnterior",this.getRequest().getHostRef().toString()+"/smarthome");
		dataModel.put("floor", floor);
		Representation applianceTemplate = new ClientResource(LocalReference.createClapReference(getClass().getPackage())+ "/applianceListTemplate").get();
		return new TemplateRepresentation(applianceTemplate, dataModel, MediaType.TEXT_HTML);
	}
	
	public void storeResource(Representation rep) throws IOException{
		; //No queremos añadir una lista de contactos nueva
	}
	
	public void deleteResource(String uniqueID){
		; //No queremos borrar la lista de contactos
	}
}

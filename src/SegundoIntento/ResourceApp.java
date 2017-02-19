package SegundoIntento;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/*The state of a resource is composed by:
 * -Call request (we can use getRequest() to see what we can obtain from the request -Table 2.4 "Restlet in action")
 * -Call response (we can use getResponse() to see what we can obtain or do with the response)
 * -Parents application context
 * -User provided state (Persistent bean or Database result set)
 */

/*Three lifecycle methods: 
 * -init(Context, Request, Response) to provide its initial state
 * -handle() to trigger the processing of the associated call
 * -release() to prepare the resource for garbage collection
 */

/* Use HTTP methods according to the semantics mandated by HTTP.
 * Example: Use get only with the purpose of getting information, that states for its original purpose.
 * Safe methods: for pure information retrieval.
 * Idempotent methods: the side effects of multiple identical requests are the same as a single request. (Example: Deleting 
 * 3 resources results the same as deleting one, in both the target resources are deleted)
 * GET,PUT,POST,DELETE,OPTIONS(to describe communications options available).
 * 
 */

/**With this interface we ensure that all our resources are able to perform the 
 * three basic REST operations GET,PUT,DELETE*/
public interface ResourceApp{
	
	@Get
	public Representation getResource();
	
	@Put
	public void storeResource(Representation rep) throws IOException;
	
	@Delete
	public void deleteResource(String uniqueID);

}

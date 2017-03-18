import com.google.gson.JsonObject;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;

/**
 * Created by bakerally on 3/17/17.
 */
public class SPARQL {
    public static String generateSPARQLQuery(JsonObject queryObject, JsonObject mappings){
        System.out.println(queryObject+" "+mappings.toString());
        String resourceNamespace = mappings.get("resourceNamespace").getAsString();
        Node mainSubjectNode = NodeFactory.createBlankNode();
        if (queryObject.has("_id")){
            String mainSubjectIRI = resourceNamespace + queryObject.get("_id").getAsString();
            mainSubjectNode = NodeFactory.createURI(mainSubjectIRI);
        }
        System.out.println("MainSubjectNode"+mainSubjectNode);
        String query = "";
        return query;
    }
}

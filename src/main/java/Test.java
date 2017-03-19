/**
 * Created by bakerally on 3/17/17.
 */

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.opensensingcity.GJQL.Factory.MappingFactory;
import fr.opensensingcity.GJQL.Factory.QResourceFactory;
import fr.opensensingcity.GJQL.QResource.QResource;
import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.query.Query;

public class Test {
    public static void main (String [] args){
       String mappingStr = "{\n" +
               "  \"resourceNamespace\":\"http://opensensingcity.emse.fr/Parking/resource/\",\n" +
               "  \"classNamespace\":\"http://opensensingcity.emse.fr/ontology/\",\n" +
               "  \"prefixes\":{\n" +
               "    \"osc\":\"http://opensensingcity.emse.fr/ontology/\",\n" +
               "    \"pk\":\"http://opensensingcity.emse.fr/Parking/resource/\"\n" +
               "  },\n" +
               "  \"resourceExceptions\":{\n" +
               "    \"name\":\"http://www.w3.org/2000/01/rdf-schema#label\",\n" +
               "    \"lat\":\"http://www.w3.org/2003/01/geo/wgs84_pos#lat\",\n" +
               "    \"long\":\"http://www.w3.org/2003/01/geo/wgs84_pos#long\"\n" +
               "  }\n" +
               "}";
        JsonParser parser = new JsonParser();
        JsonObject mappingObject = parser.parse(mappingStr).getAsJsonObject();
        //System.out.println(mappingObject);

        Mapping simpleMapping = MappingFactory.generateSimpleMappingFromJSON(mappingObject);

        String queryStr = "{\n" +
                "  \"_id\":\"P570\",\n" +
                "  \"_type\":\"Parking\",\n" +
                "  \"_fields\":[\"name\"]\n" +
                "}";
        QResource resource = QResourceFactory.
                loadSimpleQResourceFromJSON(parser.parse(queryStr).getAsJsonObject());

        Query q = simpleMapping.generateSPARQLQuery(resource);
        System.out.println(q.serialize());

    }


}

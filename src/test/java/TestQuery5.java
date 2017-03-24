/**
 * Created by bakerally on 3/17/17.
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.opensensingcity.GJQL.GraphUtils;
import fr.opensensingcity.GJQL.factory.MappingFactory;
import fr.opensensingcity.GJQL.factory.QResourceFactory;
import fr.opensensingcity.GJQL.mapping.Mapping;
import fr.opensensingcity.GJQL.qresource.QResource;
import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class TestQuery5 {
    String message = "Robert";


    @Test
    public void testGenerateSPARQLFromJson() throws IOException, URISyntaxException {
        JsonParser parser = new JsonParser();


        //get mapping representation
        String queryMappings = TestUtils.getFileContentFromResource(this,"mappings.json");
        JsonObject queryMappingObject = parser.parse(queryMappings).getAsJsonObject();
        Mapping simpleMapping = MappingFactory.generateSimpleMappingFromJSON(queryMappingObject);



        //get resource representation
        String queryJSON =TestUtils.getFileContentFromResource(this,"query.json");
        JsonObject queryObject = parser.parse(queryJSON).getAsJsonObject();
        QResource resource = QResourceFactory.loadSimpleQResourceFromJSON(queryObject);


        //generate query
        Query query = simpleMapping.generateSPARQLQuery(resource);
        query.setQueryResultStar(false);
        System.out.println(query.serialize());
        /*

        //generate results from query
        String modelIRI = getClass().getResource("/"+getClass().getSimpleName()).toString()+"/graph.ttl";
        ResultSet queryResults = GraphUtils.executeSPARQL(query, modelIRI);

        //serialize results from the query
        String result = resource.serializeResult(queryResults);
        JsonElement generatedResultObject = parser.parse(result);

        //load original result
        String originalResult = TestUtils.getFileContentFromResource(this,"result.json");
        JsonElement originalResultObject = parser.parse(originalResult);

        //System.out.println(generatedResultObject);
        //System.out.println(originalResultObject);

        //check generated result vs original result
        assertTrue(generatedResultObject.equals(originalResultObject));*/

    }
}

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
import org.apache.jena.query.QueryFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class TestQuery6 {
    String message = "Robert";


    @Test
    public void testGenerateSPARQLFromJson() throws IOException, URISyntaxException {
        JsonParser parser = new JsonParser();

        System.out.println("#####Test6####");

        //get resource representation
        String queryJSON =TestUtils.getFileContentFromResource(this,"query.json");
        JsonObject queryObject = parser.parse(queryJSON).getAsJsonObject();
        QResource resource = QResourceFactory.loadSimpleQResourceFromJSON(queryObject);

        /*
        //get mapping representation
        String queryMappings = TestUtils.getFileContentFromResource(this,"mappings.json");
        JsonObject queryMappingObject = parser.parse(queryMappings).getAsJsonObject();
        Mapping simpleMapping = MappingFactory.generateSimpleMappingFromJSON(queryMappingObject);



        //generate query
        Query query = simpleMapping.generateSPARQLQuery(resource);
        query.setQueryResultStar(false);


        String oqueryStr  = TestUtils.getFileContentFromResource(this,"query.rq");
        Query originalQuery = QueryFactory.create(oqueryStr);
        //assertTrue(originalQuery.getQueryPattern().equalTo(query.getQueryPattern(),null));

        //generate results from query
        String modelIRI = getClass().getResource("/"+getClass().getSimpleName()).toString()+"/graph.ttl";
        String result = resource.serializeResult(GraphUtils.executeSPARQL(query,modelIRI));
       *//* System.out.println("GEnerated Result:"+result);*//*

        JsonElement generatedResultObject = parser.parse(result);

        //load original result
        String originalResult = TestUtils.getFileContentFromResource(this,"result.json");
        JsonElement originalResultObject = parser.parse(originalResult);

        //System.out.println(generatedResultObject);
        //System.out.println(originalResultObject);

        assertTrue(generatedResultObject.equals(originalResultObject));*/

    }
}
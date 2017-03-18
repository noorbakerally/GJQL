/**
 * Created by bakerally on 3/17/17.
 */

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.opensensingcity.GJQL.SPARQL;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class TestQuery2 {
    String message = "Robert";


    @Test
    public void testGenerateSPARQLFromJson() throws IOException, URISyntaxException {
        JsonParser parser = new JsonParser();
        String queryJSON =TestUtils.getFileContentFromResource(this,"query.json");
        JsonObject queryObject = parser.parse(queryJSON).getAsJsonObject();
        String queryMappings = TestUtils.getFileContentFromResource(this,"mappings.json");
        JsonObject queryMappingObject = parser.parse(queryMappings).getAsJsonObject();
        Query query = SPARQL.generateSPARQLQuery(queryObject,queryMappingObject);
        String oqueryStr  = TestUtils.getFileContentFromResource(this,"query.rq");
        Query originalQuery = QueryFactory.create(oqueryStr);
        assertTrue(originalQuery.getQueryPattern().equalTo(query.getQueryPattern(),null));
    }
}

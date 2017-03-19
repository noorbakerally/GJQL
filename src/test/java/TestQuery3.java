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
import org.apache.jena.query.QueryFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class TestQuery3 {
    String message = "Robert";


    @Test
    public void testGenerateSPARQLFromJson() throws IOException, URISyntaxException {
        JsonParser parser = new JsonParser();

        //get resource representation
        String queryJSON =TestUtils.getFileContentFromResource(this,"query.json");
        JsonObject queryObject = parser.parse(queryJSON).getAsJsonObject();
        QResource resource = QResourceFactory.
                loadSimpleQResourceFromJSON(queryObject);

        //get mapping representation
        String queryMappings = TestUtils.getFileContentFromResource(this,"mappings.json");
        JsonObject queryMappingObject = parser.parse(queryMappings).getAsJsonObject();
        Mapping simpleMapping = MappingFactory.generateSimpleMappingFromJSON(queryMappingObject);

        //generate query
        Query query = simpleMapping.generateSPARQLQuery(resource);
        query.setQueryResultStar(false);
        //System.out.println(query.serialize());

        String oqueryStr  = TestUtils.getFileContentFromResource(this,"query.rq");
        Query originalQuery = QueryFactory.create(oqueryStr);
        assertTrue(originalQuery.getQueryPattern().equalTo(query.getQueryPattern(),null));
    }
}

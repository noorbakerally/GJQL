/**
 * Created by bakerally on 3/17/17.
 */
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.opensensingcity.GJQL.GraphUtils;
import fr.opensensingcity.GJQL.factory.MappingFactory;
import fr.opensensingcity.GJQL.factory.QResourceFactory;
import fr.opensensingcity.GJQL.qresource.QResource;
import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.junit.Test;
import testutils.TestUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestQuery11 {
    String message = "Robert";


    @Test
    public void testGenerateSPARQLFromJson() throws IOException, URISyntaxException {
        JsonParser parser = new JsonParser();

        //get resource representation
        String queryJSON = TestUtils.getFileContentFromResource(this,"query.json");
        JsonObject queryObject = parser.parse(queryJSON).getAsJsonObject();
        QResource resource = QResourceFactory.
                loadSimpleQResourceFromJSON(queryObject);

        //get mapping representation
        String queryMappings = TestUtils.getFileContentFromResource(this,"mappings.json");
        JsonObject queryMappingObject = parser.parse(queryMappings).getAsJsonObject();
        Mapping simpleMapping = MappingFactory.generateSimpleMappingFromJSON(queryMappingObject);

        //generate query
        BasicPattern expression = resource.generateExpression(simpleMapping,null,null,null);
        Op op = new OpBGP(expression) ;
        Query query = OpAsQuery.asQuery(op);
        query.setQueryResultStar(false);
        for (String prefix:simpleMapping.getPrefixes().keySet()){
            query.setPrefix(prefix,simpleMapping.getPrefixes().get(prefix));
        }



        String oqueryStr  = TestUtils.getFileContentFromResource(this,"query.rq");

        //assertTrue(originalQuery.getQueryPattern().equalTo(query.getQueryPattern(),null));

        //generate results from query
        String modelIRI = getClass().getResource("/"+getClass().getSimpleName()).toString()+"/graph.ttl";
        ResultSet resultBindings = GraphUtils.executeSPARQL(query, modelIRI);
        List<QuerySolution> querySolutions = new ArrayList<QuerySolution>();
        while (resultBindings.hasNext()){
            QuerySolution qs = resultBindings.next();
            querySolutions.add(qs);
        }

        String result = resource.serializeResult(querySolutions);

        JsonObject generatedResultObject = parser.parse(result).getAsJsonObject();

        //load original result
        String originalResult = TestUtils.getFileContentFromResource(this,"result.json");
        JsonObject originalResultObject = parser.parse(originalResult).getAsJsonObject();

       /* System.out.println(generatedResultObject);
        System.out.println(originalResultObject);*/


        assertTrue(generatedResultObject.equals(originalResultObject));
    }
}

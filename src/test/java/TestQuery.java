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
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestQuery {
    String message = "Robert";


    @Test
    public void testGenersateSPARQLFromJson() throws IOException, URISyntaxException {

        int testPass =0;
        int testfailed =0;
        for (int i=1;i<=11;i++){
            System.out.println("##########TestQuery"+i);
            if (performithTest(i)){
                System.out.println("Test Passed");
                testPass++;
            } else {
                System.out.println("Test Failed");
                testfailed++;
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("Test Passed:"+testPass);
        System.out.println("Test Failed:"+testfailed);
    }

    public boolean performithTest(int ithTest) throws IOException, URISyntaxException {

        //get absolute path to test folder
        URL location = TestQuery.class.getProtectionDomain().getCodeSource().getLocation();
        String path = location.getPath();
        String testClassPath = path.substring(0, path.lastIndexOf("/"))+"/TestQuery"+ithTest;

        JsonParser parser = new JsonParser();
        //get resource representation
        String queryJSON = getFileContentFromResource(ithTest,"query.json");
        JsonObject queryObject = parser.parse(queryJSON).getAsJsonObject();
        QResource resource = QResourceFactory.
                loadSimpleQResourceFromJSON(queryObject);

        //get mapping representation
        String queryMappings = getFileContentFromResource(ithTest,"mappings.json");
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

        //generate results from query
        String modelIRI = testClassPath+"/graph.ttl";
        ResultSet resultBindings = GraphUtils.executeSPARQL(query, modelIRI);
        List<QuerySolution> querySolutions = new ArrayList<QuerySolution>();
        while (resultBindings.hasNext()){
            QuerySolution qs = resultBindings.next();
            querySolutions.add(qs);
        }

        String result = resource.serializeResult(querySolutions);
        //System.out.println(result);

        JsonElement generatedResultObject = parser.parse(result);

        //load original result
        String originalResult = getFileContentFromResource(ithTest,"result.json");
        JsonElement originalResultObject = parser.parse(originalResult);


        return (generatedResultObject.equals(originalResultObject));
    }

    public static String getFileContentFromResource(int ithTest,String filename) throws URISyntaxException, IOException {
        //get absolute path to test folder
        URL location = TestQuery.class.getProtectionDomain().getCodeSource().getLocation();
        String path = location.getPath();
        String testClassPath = path.substring(0, path.lastIndexOf("/"))+"/TestQuery"+ithTest;

        File file = new File(testClassPath+"/"+filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data);
        return str;
    }
}

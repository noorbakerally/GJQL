/**
 * Created by bakerally on 3/17/17.
 */

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.opensensingcity.GJQL.factory.MappingFactory;
import fr.opensensingcity.GJQL.factory.QResourceFactory;
import fr.opensensingcity.GJQL.qresource.QResource;
import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;

public class Test {
    public static void main (String [] args){
        Model inputGraph = RDFDataMgr.loadModel("/home/bakerally/Documents/repositories/github/GJQL/target/test-classes/TestQuery4/graph.ttl");

        String queryStr = "PREFIX  osc:  <http://opensensingcity.emse.fr/ontology/>\n" +
                "PREFIX  pk:   <http://opensensingcity.emse.fr/Parking/resource/>\n" +
                "\n" +
                "SELECT  ?name ?lat ?long\n" +
                "WHERE\n" +
                "  { _:b0  <http://www.w3.org/2000/01/rdf-schema#label>  ?name ;\n" +
                "          <http://www.w3.org/2003/01/geo/wgs84_pos#lat>  ?lat ;\n" +
                "          <http://www.w3.org/2003/01/geo/wgs84_pos#long>  ?long\n" +
                "  }\n";
        Query originalQuery = QueryFactory.create(queryStr);
        QueryExecution qexec = QueryExecutionFactory.create(originalQuery, inputGraph);
        ResultSet rs = qexec.execSelect() ;

    }


}

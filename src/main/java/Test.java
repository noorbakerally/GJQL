/**
 * Created by bakerally on 3/17/17.
 */

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.jena.datatypes.xsd.XSDDatatype ;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.algebra.Transformer;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.expr.E_LessThan;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.NodeValue;

public class Test {
    public static void main (String [] args){
       String mappingStr = "{\n" +
               "  \"resourceNamespace\":\"http://opensensingcity.emse.fr/Parking/resource/\",\n" +
               "  \"classNamespace\":\"http://opensensingcity.emse.fr/ontology/\",\n" +
               "  \"prefix\":{\n" +
               "    \"osc\":\"http://opensensingcity.emse.fr/ontology/\",\n" +
               "    \"pk\":\"http://opensensingcity.emse.fr/Parking/resource/\"\n" +
               "  },\n" +
               "  \"resourceException\":{\n" +
               "    \"name\":\"http://www.w3.org/2000/01/rdf-schema#label\",\n" +
               "    \"lat\":\"http://www.w3.org/2003/01/geo/wgs84_pos#lat\",\n" +
               "    \"long\":\"http://www.w3.org/2003/01/geo/wgs84_pos#long\"\n" +
               "  }\n" +
               "}";
        JsonParser parser = new JsonParser();
        JsonObject queryObject = parser.parse(mappingStr).getAsJsonObject();
        System.out.println(queryObject);

    }


}

package fr.opensensingcity.GJQL;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.rdf.model.Model;

/**
 * Created by bakerally on 3/19/17.
 */
public class GraphUtils {
    public static ResultSet executeSPARQL(Query query, String modelURI){
        Model inputGraph = RDFDataMgr.loadModel(modelURI);
        Query originalQuery = QueryFactory.create(query);
        QueryExecution qexec = QueryExecutionFactory.create(originalQuery, inputGraph);
        ResultSet rs = qexec.execSelect() ;
        return rs;
    }
}

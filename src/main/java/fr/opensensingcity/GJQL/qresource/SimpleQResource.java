package fr.opensensingcity.GJQL.qresource;

import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.query.Query;

import java.util.ArrayList;

/**
 * Created by bakerally on 3/19/17.
 */
public class SimpleQResource extends QResource {
    public SimpleQResource(){
        atomicFields = new ArrayList<String>();
    }
    public Query generateSPARQL(Mapping mapping) {
        return null;
    }
}
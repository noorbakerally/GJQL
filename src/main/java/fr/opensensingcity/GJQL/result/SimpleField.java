package fr.opensensingcity.GJQL.result;

import org.apache.jena.rdf.model.RDFNode;

/**
 * Created by bakerally on 3/19/17.
 */
public class SimpleField extends Field {

    public SimpleField(String fieldName, RDFNode nodeField) {
        super(fieldName, nodeField);
    }
}

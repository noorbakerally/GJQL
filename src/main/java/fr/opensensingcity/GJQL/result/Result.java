package fr.opensensingcity.GJQL.result;

import org.apache.jena.query.QuerySolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bakerally on 3/26/17.
 */
public abstract class Result {
    Map<String,Field> fields;
    public  Result(){
        fields = new HashMap <String,Field> ();
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public void setFields(Map<String, Field> fields) {
        this.fields = fields;
    }

    public  String getField(String fieldName){
        Field field = fields.get(fieldName);
        return field.getFieldValue();
    }
}

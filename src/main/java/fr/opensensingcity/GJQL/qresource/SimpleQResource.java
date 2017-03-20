package fr.opensensingcity.GJQL.qresource;

import com.google.gson.*;
import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.query.Query;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.ResultBinding;

import java.util.ArrayList;
import java.util.Iterator;

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

    public String serializeResult(ResultSet bindings) {
        JsonArray arrResult = new JsonArray();
        JsonObject result = new JsonObject();

        while (bindings.hasNext()){
            result = new JsonObject();
            if (hasId()){
                result.addProperty("_id",rId);
            }

            if (hasType()){
                result.addProperty("_type",rType);
            }

            if (atomicFields.size() > 0) {
                JsonArray jsonArray = new JsonArray();
                for (String atomicField:atomicFields){
                    jsonArray.add(atomicField);
                }
                result.add("_fields",jsonArray);
            }

            QuerySolution binding = bindings.next();
            for (String atomicField:atomicFields){
                result.addProperty(atomicField,binding.get(atomicField).asLiteral().getLexicalForm());
            }
            if (!hasId()){
                arrResult.add(result);
            }
        }

        if (hasId()) {
            System.out.println(result.toString());
            return result.toString();
        }
        System.out.println(arrResult.toString());
        return arrResult.toString();

    }
}

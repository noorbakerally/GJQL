package fr.opensensingcity.GJQL.result;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.opensensingcity.GJQL.qresource.QResource;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bakerally on 3/26/17.
 */
public class ResultSerializer {
    public void insertResult(ResultSet resultBindings, Map<String,QResource> qresources){

        List<QuerySolution> querySolutionList = new ArrayList<QuerySolution>();

        while (resultBindings.hasNext()){
            querySolutionList.add(resultBindings.next());
        }

        for (String qid:qresources.keySet()){
            QResource qResource = qresources.get(qid);

            for (QuerySolution solution:querySolutionList){
                String idName = "Id_"+qResource.getQid();
                //System.out.println(idName + " "+solution.get(idName).isResource()+" Solution:"+solution);
                String idValue = solution.get(idName).toString();

                if (qResource.getResults().get(idValue) == null) {
                    Result r1 = new SimpleResult();

                    for (String atomicField:qResource.getAtomicFields()){
                         if (atomicField.contains(".")){
                             atomicField = atomicField.replace(".","");
                         }
                         String vName = atomicField+"_"+qResource.getQid();
                         Field fx = new SimpleField(atomicField,solution.get(vName));
                         r1.fields.put(atomicField,fx);
                    }
                    qResource.getResults().put(idValue,r1);
                }
            }
        }
    }

    public JsonElement JSONSerializer(QResource resource) {

        boolean isArray = false;
        JsonElement results = new JsonObject ();
        if (resource.getResults().keySet().size() > 1 || !resource.hasId()){
            results = new JsonArray();
            isArray = true;
        }


        for (String rid:resource.getResults().keySet()){
            Result currentResult = resource.getResults().get(rid);
            JsonObject result = new JsonObject();
            result.addProperty("_type",resource.getrType());

            JsonArray fields = new JsonArray();
            if (resource.hasId()){
                result.addProperty("_id",resource.getrId());
            }
            if (resource.hasType()){
                result.addProperty("_type",resource.getrType());
            }

            //adding a property for every field in the
            //json object and setting a value
            for (String afield:resource.getAtomicFields()){

                result.addProperty(afield,currentResult.getField(afield));
                fields.add(afield);
            }
            result.add("_fields",fields);

            for (String childType:resource.getqResources().keySet()){
                QResource currentQResource = resource.getqResources().get(childType);
                result.add(childType,JSONSerializer(currentQResource));
            }

            if (isArray){
                results.getAsJsonArray().add(result);
            } else {
                return result;
            }
        }
        return  results;
    }
}

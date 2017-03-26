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
    public static String insertResult(ResultSet resultBindings, Map<String,QResource> qresources){

        List<QuerySolution> querySolutionList = new ArrayList<QuerySolution>();

        while (resultBindings.hasNext()){
            querySolutionList.add(resultBindings.next());
        }

        for (String qid:qresources.keySet()){
            QResource qResource = qresources.get(qid);

            for (QuerySolution solution:querySolutionList){
                String idName = "Id_"+qResource.getQid();
                String idValue = solution.get(idName).toString();

                if (qResource.getResults().get(idValue) == null) {
                    Result r1 = new SimpleResult();

                    for (String atomicField:qResource.getAtomicFields()){
                         String vName = atomicField+"_"+qResource.getQid();
                         Field fx = new SimpleField(atomicField,solution.get(vName).toString());
                         r1.fields.put(atomicField,fx);
                    }
                    qResource.getResults().put(idValue,r1);
                }
            }
        }



        return "";
    }

    public static JsonElement JSONSerializer(QResource resource) {

        boolean isArray = false;
        JsonElement results = new JsonObject ();
        if (resource.getResults().keySet().size() > 1){
            results = new JsonArray();
            isArray = true;
        }


        for (String rid:resource.getResults().keySet()){
            Result currentResult = resource.getResults().get(rid);
            JsonObject result = new JsonObject();
            result.addProperty("_type",resource.getrType());
            if (resource.hasId()){
                result.addProperty("_id",resource.getrId());
            }
            if (resource.hasType()){
                result.addProperty("_type",resource.getrType());
            }
            for (String afield:resource.getAtomicFields()){
                result.addProperty(afield,currentResult.getField(afield));
            }

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

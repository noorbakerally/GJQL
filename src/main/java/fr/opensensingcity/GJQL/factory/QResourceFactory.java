package fr.opensensingcity.GJQL.factory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.opensensingcity.GJQL.qresource.QResource;
import fr.opensensingcity.GJQL.qresource.SimpleQResource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by bakerally on 3/19/17.
 */
public class QResourceFactory {
    public static Map<String,QResource> qresources = new HashMap<String, QResource>();

    public static QResource loadSimpleQResourceFromJSON(String type, JsonObject queryObject){
        QResource simpleQResource = new SimpleQResource();


        simpleQResource.setrType(type);


        if (queryObject.has("_id")){
            simpleQResource.setrId(queryObject.get("_id").getAsString());
        }



        if (queryObject.has("_fields")) {
            //get the _fields iterator
            JsonElement elements = queryObject.get("_fields");
            Iterator<JsonElement> fields = elements.getAsJsonArray().iterator();

            //iterate on every element of the fields
            //check its type
            //if atomic then insert into atomic list
            //else insert as compound object
            while (fields.hasNext()) {
                JsonElement currentField = fields.next();
                simpleQResource.addAtomicFields(currentField.getAsString());
            }
        }



        Iterator<Map.Entry<String, JsonElement>> remainingElements = queryObject.entrySet().iterator();
        while (remainingElements.hasNext()){
            Map.Entry<String, JsonElement> currentRemainingElement = remainingElements.next();
            if (currentRemainingElement.getValue().isJsonObject()){
                String rtype = currentRemainingElement.getKey();
                JsonElement rObject = currentRemainingElement.getValue();
                JsonObject currentFieldAsJsonObject = currentRemainingElement.getValue().getAsJsonObject();
                QResource newQResource = QResourceFactory.loadSimpleQResourceFromJSON(rtype,rObject.getAsJsonObject());
                simpleQResource.addQResource(currentRemainingElement.getKey(),newQResource);
                qresources.put(newQResource.getQid(),newQResource);
            }
        }

        return simpleQResource;
    }
}

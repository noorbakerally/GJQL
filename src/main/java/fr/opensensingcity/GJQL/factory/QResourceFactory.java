package fr.opensensingcity.GJQL.factory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.opensensingcity.GJQL.qresource.QResource;
import fr.opensensingcity.GJQL.qresource.SimpleQResource;
import java.util.Iterator;

/**
 * Created by bakerally on 3/19/17.
 */
public class QResourceFactory {
    public static QResource loadSimpleQResourceFromJSON(JsonObject queryObject){
        QResource simpleQResource = new SimpleQResource();

        if (queryObject.has("_id")){
            simpleQResource.setrId(queryObject.get("_id").getAsString());
        }

        if (queryObject.has("_type")){
            simpleQResource.setrType(queryObject.get("_type").getAsString());
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
                simpleQResource.addAtomicFields(fields.next().getAsString());
            }
        }
        return simpleQResource;
    }
}

package fr.opensensingcity.GJQL.mapping;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by bakerally on 3/19/17.
 */
public class SimpleMapping extends Mapping {


    public void loadMapping(Object mappingObject) {
        JsonObject mappingJsonObject = (JsonObject) mappingObject;
        Mapping simpleMapping = new SimpleMapping();

        defaultResourceNamespace = mappingJsonObject.get("resourceNamespace").getAsString();
        defaultClassNamespace = mappingJsonObject.get("classNamespace").getAsString();

        if (mappingJsonObject.has("prefixes")){
            JsonObject prefixObject = mappingJsonObject.get("prefix").getAsJsonObject();
            Iterator<Map.Entry<String, JsonElement>> prefixIterator = prefixObject.entrySet().iterator();
            while (prefixIterator.hasNext()){
                Map.Entry<String, JsonElement> currentPrefix = prefixIterator.next();
                prefixes.put(currentPrefix.getKey(),currentPrefix.getValue().getAsString());
            }
        }

        if (mappingJsonObject.has("resourceExceptions")){
            JsonObject resourceExceptionsObject = mappingJsonObject.get("resourceExceptions").getAsJsonObject();
            Iterator<Map.Entry<String, JsonElement>> reIterator = resourceExceptionsObject.entrySet().iterator();
            while (reIterator.hasNext()){
                Map.Entry<String, JsonElement> currentPrefix = reIterator.next();
                resourceExceptions.put(currentPrefix.getKey(),currentPrefix.getValue().getAsString());
            }
        }
    }
}

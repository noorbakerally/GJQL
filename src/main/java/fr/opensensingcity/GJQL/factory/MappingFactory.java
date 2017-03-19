package fr.opensensingcity.GJQL.factory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.opensensingcity.GJQL.mapping.Mapping;
import fr.opensensingcity.GJQL.mapping.SimpleMapping;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by bakerally on 3/19/17.
 */
public class MappingFactory {
    public static Mapping generateSimpleMappingFromJSON(JsonObject mappingJsonObject){

        Mapping simpleMapping = new SimpleMapping();
        simpleMapping.setDefaultResourceNamespace(mappingJsonObject.get("resourceNamespace").getAsString());
        simpleMapping.setDefaultClassNamespace(mappingJsonObject.get("classNamespace").getAsString());


        if (mappingJsonObject.has("prefixes")){
            JsonObject prefixObject = mappingJsonObject.get("prefixes").getAsJsonObject();
            Iterator<Map.Entry<String, JsonElement>> prefixIterator = prefixObject.entrySet().iterator();
            while (prefixIterator.hasNext()){
                Map.Entry<String, JsonElement> currentPrefix = prefixIterator.next();
                simpleMapping.getPrefixes().put(currentPrefix.getKey(),currentPrefix.getValue().getAsString());
            }
        }

        if (mappingJsonObject.has("resourceExceptions")){
            JsonObject resourceExceptionsObject = mappingJsonObject.get("resourceExceptions").getAsJsonObject();
            Iterator<Map.Entry<String, JsonElement>> reIterator = resourceExceptionsObject.entrySet().iterator();
            while (reIterator.hasNext()){
                Map.Entry<String, JsonElement> currentPrefix = reIterator.next();
                simpleMapping.getResourceExceptions().put(currentPrefix.getKey(),currentPrefix.getValue().getAsString());
            }
        }
        return simpleMapping;
    }
}
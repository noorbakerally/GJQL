package fr.opensensingcity.GJQL.mapping;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by bakerally on 3/19/17.
 */
public abstract class Mapping {
    String defaultClassNamespace;
    String defaultResourceNamespace;
    Map<String,String> prefixes;
    Map <String,String> resourceExceptions;



    public String getDefaultClassNamespace() {
        return defaultClassNamespace;
    }

    public void setDefaultClassNamespace(String defaultClassNamespace) {
        this.defaultClassNamespace = defaultClassNamespace;
    }

    public void addPrefix(String prefix, String Uri){
        prefixes.put(prefix,Uri);
    }

    public void addResourceException(String resource, String Uri){
        resourceExceptions.put(resource,Uri);
    }


    public abstract Mapping loadMapping(Object mappingObject);


}

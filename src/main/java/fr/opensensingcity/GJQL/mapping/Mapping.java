package fr.opensensingcity.GJQL.mapping;

import fr.opensensingcity.GJQL.qresource.QResource;
import org.apache.jena.query.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bakerally on 3/19/17.
 */
public abstract class Mapping {
    String defaultClassNamespace;
    String defaultResourceNamespace;
    Map<String,String> prefixes;
    Map <String,String> resourceExceptions;

    public  Mapping(){
        prefixes = new HashMap<String, String>();
        resourceExceptions = new HashMap<String, String>();
    }

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


    public String getDefaultResourceNamespace() {
        return defaultResourceNamespace;
    }

    public void setDefaultResourceNamespace(String defaultResourceNamespace) {
        this.defaultResourceNamespace = defaultResourceNamespace;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(Map<String, String> prefixes) {
        this.prefixes = prefixes;
    }

    public Map<String, String> getResourceExceptions() {
        return resourceExceptions;
    }

    public void setResourceExceptions(Map<String, String> resourceExceptions) {
        this.resourceExceptions = resourceExceptions;
    }

    public abstract Query generateSPARQLQuery(QResource resource);

}

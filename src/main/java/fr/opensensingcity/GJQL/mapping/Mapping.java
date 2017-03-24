package fr.opensensingcity.GJQL.mapping;

import fr.opensensingcity.GJQL.qresource.QResource;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.Var;

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
    Map <String, Map <String,String>> classResourceExceptions;


    public  Mapping(){
        prefixes = new HashMap<String, String>();
        resourceExceptions = new HashMap<String, String>();
        classResourceExceptions = new HashMap<String, Map<String, String>>();
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

    public Map<String, Map<String, String>> getClassResourceExceptions() {
        return classResourceExceptions;
    }

    public void setClassResourceExceptions(Map<String, Map<String, String>> classResourceExceptions) {
        this.classResourceExceptions = classResourceExceptions;
    }
    public Node getVNode(String field, QResource qResource){
        if (field.contains(".")){
            field = field.replace(".","");
        }
        return Var.alloc(field+qResource.getQid());
    }

    public String getResourceIRI(String field){
        if (field.contains(":")){
            String [] prefixParts = field.split(":");
            return prefixes.get(prefixParts[0])+prefixParts[1];
        } else {
            return resourceExceptions.get(field);
        }
    }

    //utility methods
    public Node getNode(String type,String field){
        System.out.println("Generating Predicate:"+field+ " for "+type);
        String predicateIRI;
        if (type!=null){
            if (getClassResourceExceptions().containsKey(type)){
                Map<String, String> classPrefixes = getClassResourceExceptions().get(type);
                if (classPrefixes.containsKey(field)){
                    return NodeFactory.createURI(getResourceIRI(classPrefixes.get(field)));
                }
            }
        }

        if (resourceExceptions.containsKey(field)){
            predicateIRI = getResourceIRI(field);
        } else {
            predicateIRI = defaultClassNamespace + field;
        }
        Node predicateNode = NodeFactory.createURI(predicateIRI);
        return predicateNode;
    }
}

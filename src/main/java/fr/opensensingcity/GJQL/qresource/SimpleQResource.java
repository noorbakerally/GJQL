package fr.opensensingcity.GJQL.qresource;

import com.google.gson.*;
import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.ResultBinding;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.vocabulary.RDF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by bakerally on 3/19/17.
 */
public class SimpleQResource extends QResource {
    public SimpleQResource(){
        atomicFields = new ArrayList<String>();
        qResources = new HashMap<String, QResource>();
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
            return result.toString();
        }
        return arrResult.toString();

    }

    public BasicPattern generateBasicPattern(Mapping mapping, Node mainSubjectNode, Node linkNode, Node predicateLinkNode) {

        BasicPattern bp = new BasicPattern() ;

        //creating an identified subject if there is an Id
        if (hasId() && mainSubjectNode !=null){
            String resourceIRI = mapping.getDefaultResourceNamespace() + getrId();
            mainSubjectNode = NodeFactory.createURI(resourceIRI);
        }

        //creating a type for the resource
        if (hasType()){
            String mainTypeIRI = mapping.getDefaultClassNamespace() + getrType();
            Node mainSubjectType = NodeFactory.createURI(mainTypeIRI);
            bp.add(new Triple(mainSubjectNode, RDF.Nodes.type , mainSubjectType)) ;
        }

        //generating a triple pattern for every atomic fields
        for (String field:getAtomicFields()){
            String predicateIRI;
            if (mapping.getResourceExceptions().containsKey(field)){
                predicateIRI = mapping.getResourceExceptions().get(field);
            } else {
                predicateIRI = mapping.getDefaultClassNamespace() + field;
            }
            Node predicateNode = NodeFactory.createURI(predicateIRI);
            Var variableNode = Var.alloc(field);
            bp.add(new Triple(mainSubjectNode, predicateNode ,variableNode)) ;
        }
        bp.add(new Triple(linkNode,predicateLinkNode,mainSubjectNode));

        //to add composite here
        for (String field:getqResources().keySet()){
            String predicateIRI;
            if (mapping.getResourceExceptions().containsKey(field)){
                predicateIRI = mapping.getResourceExceptions().get(field);
            } else {
                predicateIRI = mapping.getDefaultClassNamespace() + field;
            }
            Node predicateNode = NodeFactory.createURI(predicateIRI);
            //check if qresource has id
            Node subjectNode = NodeFactory.createBlankNode();
            QResource currentQResource = qResources.get(field);
            if (currentQResource.hasId()){
                String resourceIRI = mapping.getDefaultResourceNamespace() + getrId();
                subjectNode = NodeFactory.createURI(resourceIRI);
            }
            BasicPattern newBPs = currentQResource.generateBasicPattern(mapping,subjectNode,mainSubjectNode,predicateNode);
            bp.addAll(newBPs);
        }


        return bp;

    }
}

package fr.opensensingcity.GJQL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.vocabulary.RDF;

import java.util.*;

/**
 * Created by bakerally on 3/17/17.
 */
public class SPARQL {
    public static Query generateSPARQLQuery(JsonObject queryObject, JsonObject mappings){




        String resourceNamespace = mappings.get("resourceNamespace").getAsString();
        String classNamespace = mappings.get("classNamespace").getAsString();

        List<Var> projectVars = new ArrayList<Var>();
        List <String> vars = new ArrayList<String>();


        BasicPattern bp = new BasicPattern() ;
        Node mainSubjectNode = NodeFactory.createBlankNode();
        if (queryObject.has("_id")){
            String mainSubjectIRI = resourceNamespace + queryObject.get("_id").getAsString();
            mainSubjectNode = NodeFactory.createURI(mainSubjectIRI);
        }

        if (queryObject.has("_type")){
            String mainTypeIRI = classNamespace + queryObject.get("_type").getAsString();
            Node mainSubjectType = NodeFactory.createURI(mainTypeIRI);
            bp.add(new Triple(mainSubjectNode, RDF.Nodes.type , mainSubjectType)) ;
        }

        if (queryObject.has("_fields")){
            JsonElement elements = queryObject.get("_fields");
            Iterator<JsonElement> fields = elements.getAsJsonArray().iterator();
            while (fields.hasNext()){
                JsonElement currentElement = fields.next();
                String elementName = currentElement.getAsString();

                String predicateIRI = null;
                if (mappings.has("resourceException")){
                    JsonObject resourcesExceptions = mappings.get("resourceException").getAsJsonObject();
                    if (resourcesExceptions.has(elementName)){
                        predicateIRI = resourcesExceptions.get(elementName).getAsString();
                    }
                } else {
                    predicateIRI = classNamespace + currentElement.getAsString();
                }
                Node predicateNode = NodeFactory.createURI(predicateIRI);

                Var variableNode = Var.alloc(elementName);
                vars.add(elementName);
                bp.add(new Triple(mainSubjectNode, predicateNode ,variableNode)) ;
                projectVars.add(variableNode);
            }

        }

        Op op = new OpBGP(bp) ;

        Query q = OpAsQuery.asQuery(op);



        Map <String,String> prefixMap = new HashMap<String, String>();
        if (mappings.has("prefix")){
            JsonObject prefixObject = mappings.get("prefix").getAsJsonObject();
            Iterator<Map.Entry<String, JsonElement>> prefixIterator = prefixObject.entrySet().iterator();
            while (prefixIterator.hasNext()){
                Map.Entry<String, JsonElement> currentPrefix = prefixIterator.next();
                prefixMap.put(currentPrefix.getKey(),currentPrefix.getValue().getAsString());

                q.setPrefix(currentPrefix.getKey(),currentPrefix.getValue().getAsString());
            }
        }



        q.setQueryResultStar(false);
        String query = q.serialize();


        //System.out.println("Generated Query:"+query);

        return q;
    }
}

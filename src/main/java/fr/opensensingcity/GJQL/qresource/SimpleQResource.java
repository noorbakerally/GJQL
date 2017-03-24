package fr.opensensingcity.GJQL.qresource;

import com.google.gson.*;
import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.ResultBinding;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.util.ResultSetUtils;
import org.apache.jena.vocabulary.RDF;

import java.util.*;

/**
 * Created by bakerally on 3/19/17.
 */
public class SimpleQResource extends QResource {
    public SimpleQResource(){
        atomicFields = new ArrayList<String>();
        qResources = new HashMap<String, QResource>();
        Random random = new Random();
        qid = String.valueOf(Math.abs(random.nextLong()));
    }

    public Query generateSPARQLQuery(Mapping mapping) {


        Node mainSubjectNode = NodeFactory.createBlankNode();
        BasicPattern bp = new BasicPattern() ;

        //creating an identified subject if there is an Id
        if (hasId()){
            String resourceIRI =  mapping.getDefaultResourceNamespace() + getrId();
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

            if (!field.contains(".")){
                //i.e. purely atomic field
                Node predicateNode =  mapping.getNode(getrType(),field);
                Var variableNode = Var.alloc(field+getQid());
                bp.add(new Triple(mainSubjectNode, predicateNode ,variableNode)) ;

            } else {
                //create property path

                //split fields to get all parts involved
                List<String> fields = new LinkedList<String>(Arrays.asList(field.split("\\.")));

                //treat the first element and connect it to main node
                String iri1 = fields.get(0);
                Node p1  = mapping.getNode(getrType(),iri1);
                Node pBNode = NodeFactory.createBlankNode();
                bp.add(new Triple(mainSubjectNode, p1 ,pBNode)) ;

                //treat remaining elements and create the chain
                //with everything intermediary being blank nodes
                int i=1;
                while (i<fields.size()){
                    Node currentField = mapping.getNode(fields.get(i-1),fields.get(i));
                    if (i==fields.size()-1){
                        bp.add(new Triple(pBNode, currentField ,mapping.getVNode(field,this))) ;
                    } else {
                        bp.add(new Triple(pBNode,currentField,pBNode = NodeFactory.createBlankNode()));
                    }
                    i++;
                }


            }
        }

        //generating the query graph patterns for every composite fields
        Map<String, QResource> currentQResources = getqResources();
        for (String field:currentQResources.keySet()){
            //LOGGER.info("Composite Resource:"+field);
            QResource currentQResource = currentQResources.get(field);
            currentQResource.setrType(field);
            Node predicateNode = mapping.getNode(getrType(),field);

            //check if qresource has id
            Node subjectNode = NodeFactory.createBlankNode();

            if (currentQResource.hasId()){
                String resourceIRI = mapping.getDefaultResourceNamespace() + getrId();
                subjectNode = NodeFactory.createURI(resourceIRI);
            }
            BasicPattern newBPs = currentQResource.generateBasicPattern(mapping,subjectNode,mainSubjectNode,predicateNode);
            bp.addAll(newBPs);
        }

        Op op = new OpBGP(bp) ;
        Query q = OpAsQuery.asQuery(op);

        for (String prefix: mapping.getPrefixes().keySet()){
            q.setPrefix(prefix,mapping.getPrefixes().get(prefix));
        }
        return q;
    }
    public String serializeResult(Object bindings) {
        JsonParser parser = new JsonParser();
        JsonArray arrResult = new JsonArray();
        JsonObject result = new JsonObject();

        QuerySolution binding = null;
        ResultSet currentResultSet=null;
        if (!bindings.getClass().equals(ResultBinding.class)){
            currentResultSet = ((ResultSet) bindings);
            binding = ((ResultSet) bindings).next();
        } else {
            binding = (QuerySolution) bindings;
        }

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



        for (String atomicField:atomicFields){
            result.addProperty(atomicField,binding.get(getVName(atomicField)).asLiteral().getLexicalForm());
        }

        //serializing QResources
        for (String qResourceKey:qResources.keySet()){
            QResource currentQResource = qResources.get(qResourceKey);
            String strQResource = currentQResource.serializeResult(binding);

            JsonElement jsonQResource = parser.parse(strQResource);
            result.add(qResourceKey,jsonQResource);
        }


        if (!hasId()){
            arrResult.add(result);
            if (currentResultSet!=null){
                if (currentResultSet.hasNext()){
                    String tempResult = serializeResult(currentResultSet);
                    JsonElement queryObject = parser.parse(tempResult);
                    arrResult.add(queryObject);
                }
            }
        }


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (hasId()) {
            //return result.toString();
            return gson.toJson(result);
        }
        //return arrResult.toString();
        return gson.toJson(arrResult);

    }

    String getVName(String field){
        if (field.contains(".")){
            field = field.replace(".","");
        }
        return (field+getQid());
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
            Node predicateNode = mapping.getNode(getrType(),field);
            bp.add(new Triple(mainSubjectNode, predicateNode ,mapping.getVNode(field,this))) ;
        }
        bp.add(new Triple(linkNode,predicateLinkNode,mainSubjectNode));

        //to add composite here
        for (String field:getqResources().keySet()){
            QResource currentQResource = qResources.get(field);
            currentQResource.setrType(field);
            Node predicateNode = mapping.getNode(getrType(),field);
            //check if qresource has id
            Node subjectNode = NodeFactory.createBlankNode();

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

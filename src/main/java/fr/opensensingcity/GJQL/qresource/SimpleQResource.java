package fr.opensensingcity.GJQL.qresource;

import com.google.gson.*;
import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.*;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.ResultBinding;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.path.PathFactory;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementBind;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.util.ResultSetUtils;
import org.apache.jena.vocabulary.RDF;

import java.util.*;

/**
 * Created by bakerally on 3/19/17.
 */
public class SimpleQResource extends QResource {
    public SimpleQResource(){
        super();
    }

    public ElementGroup generateExpression(Mapping mapping, Node subjectNode, Node  linkNode, Node predicateNode) {

        Node mainSubjectNode = Var.alloc("Id_"+qid);
        ElementGroup bp = new ElementGroup() ;

        //creating an identified subject if there is an Id
        if (hasId()){
            String resourceIRI =  mapping.getDefaultResourceNamespace() + getrId();
            mainSubjectNode = NodeFactory.createURI(resourceIRI);
            ElementBind x = new ElementBind(Var.alloc("Id_"+qid), NodeValue.makeNode(mainSubjectNode));
            bp.addElement(x);

        }

        //creating a type for the resource
        if (hasType()){
            String mainTypeIRI = mapping.getDefaultClassNamespace() + getrType();
            Node mainSubjectType = NodeFactory.createURI(mainTypeIRI);
            bp.addTriplePattern(new Triple(mainSubjectNode, RDF.Nodes.type , mainSubjectType)); ;
        }

        //generating a triple pattern for every atomic fields
        for (String field:getAtomicFields()){

            if (!field.contains(".")){
                //i.e. purely atomic field
                predicateNode =  mapping.getNode(getrType(),field);
                Var variableNode = Var.alloc(field+"_"+getQid());
                bp.addTriplePattern(new Triple(mainSubjectNode, predicateNode ,variableNode)) ;

            } else {
                //create property path

                //split fields to get all parts involved
                List<String> fields = new LinkedList<String>(Arrays.asList(field.split("\\.")));

                String field0 = fields.get(0);
                Node p0  = mapping.getNode(getrType(),field0);

                String field1 = fields.get(1);
                Node p1  = mapping.getNode(fields.get(0),field1);

                Path finalPath = PathFactory.pathSeq(PathFactory.pathLink(p0),PathFactory.pathLink(p1));

                int i=2;
                while (i<fields.size()){
                    String fieldFinal = fields.get(i);
                    Node pFinal  = mapping.getNode(fields.get(i-1),fieldFinal);
                    finalPath = PathFactory.pathSeq(finalPath,PathFactory.pathLink(pFinal));
                    i++;
                }

                field = field.replace(".","");
                TriplePath t = new TriplePath(mainSubjectNode,finalPath,mapping.getVNode(field,this));
                ElementPathBlock tp = new ElementPathBlock();
                tp.addTriplePath(t);

                bp.addElement(tp);

            }
        }

        //generating the query graph patterns for every composite fields
        Map<String, QResource> currentQResources = getqResources();
        for (String field:currentQResources.keySet()){
            //LOGGER.info("Composite Resource:"+field);
            QResource currentQResource = currentQResources.get(field);
            currentQResource.setrType(field);
            predicateNode = mapping.getNode(getrType(),field);

            //check if qresource has id
            subjectNode = Var.alloc("Id_"+currentQResource.qid);

            if (currentQResource.hasId()){
                String resourceIRI = mapping.getDefaultResourceNamespace() + currentQResource.getrId();
                subjectNode = NodeFactory.createURI(resourceIRI);
            }
            ElementGroup newBPs = currentQResource.generateExpression(mapping,subjectNode,mainSubjectNode,predicateNode);
            for (Element element:newBPs.getElements()){
                bp.addElement(element);
            }
            bp.addTriplePattern(new Triple(mainSubjectNode,predicateNode,subjectNode));
        }
        return bp;

    }


    public JsonElement serializeSolution(QuerySolution solution){
        JsonParser parser = new JsonParser();
        JsonObject result = new JsonObject();

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
            result.addProperty(atomicField,solution.get(getVName(atomicField)).asLiteral().getLexicalForm());
        }

        //serializing QResources
        for (String qResourceKey:qResources.keySet()){
            QResource currentQResource = qResources.get(qResourceKey);
            //System.out.println(currentQResource.toStr());

            if (currentQResource.hasId()){
                JsonElement jsonQResource = currentQResource.serializeSolution(solution);
                result.add(qResourceKey,jsonQResource);
            } else {
                JsonArray arrQResource = new JsonArray();
                arrQResource.add(currentQResource.serializeSolution(solution));
                result.add(qResourceKey,arrQResource);
            }

        }
        return result;
    }

    public String serializeResult(ResultSet solutions) {
        JsonParser parser = new JsonParser();
        JsonArray arrResult = new JsonArray();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement result = new JsonObject();



        QuerySolution querySolution;
        if (solutions.hasNext()){
            querySolution = solutions.next();
            result = serializeSolution(querySolution);
        }

        if (solutions.hasNext()){
            arrResult.add(result);
            while (solutions.hasNext()){
                querySolution = solutions.next();
                result = serializeSolution(querySolution);
                arrResult.add(result);
            }
            return gson.toJson(arrResult);
        } else {
            return gson.toJson(result);
        }

    }


    String getVName(String field){
        if (field.contains(".")){
            field = field.replace(".","");
        }
        return (field+getQid());
    }
}

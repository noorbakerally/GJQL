package fr.opensensingcity.GJQL.mapping;

import fr.opensensingcity.GJQL.qresource.QResource;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.path.PathFactory;
import org.apache.jena.vocabulary.RDF;

import java.util.*;

/**
 * Created by bakerally on 3/19/17.
 */
public class SimpleMapping extends Mapping {

    public SimpleMapping(){
        super();
    }


    public Query generateSPARQLQuery(QResource resource) {
        Node mainSubjectNode = NodeFactory.createBlankNode();
        BasicPattern bp = new BasicPattern() ;

        //creating an identified subject if there is an Id
        if (resource.hasId()){
            String resourceIRI = defaultResourceNamespace + resource.getrId();
            mainSubjectNode = NodeFactory.createURI(resourceIRI);
        }

        //creating a type for the resource
        if (resource.hasType()){
            String mainTypeIRI = defaultClassNamespace + resource.getrType();
            Node mainSubjectType = NodeFactory.createURI(mainTypeIRI);
            bp.add(new Triple(mainSubjectNode, RDF.Nodes.type , mainSubjectType)) ;
        }

        //generating a triple pattern for every atomic fields
        for (String field:resource.getAtomicFields()){

            if (!field.contains(".")){
                String predicateIRI;
                if (resourceExceptions.containsKey(field)){
                    predicateIRI = resourceExceptions.get(field);
                } else {
                    predicateIRI = defaultClassNamespace + field;
                }
                Node predicateNode = NodeFactory.createURI(predicateIRI);
                Var variableNode = Var.alloc(field+resource.getQid());
                bp.add(new Triple(mainSubjectNode, predicateNode ,variableNode)) ;
            } else {
                //create property path
                List<String> fields = new LinkedList<String>(Arrays.asList(field.split("\\.")));


                String iri1 = fields.remove(0);
                Node p1  = getNode(iri1);
                Node pBNode = NodeFactory.createBlankNode();
                bp.add(new Triple(mainSubjectNode, p1 ,pBNode)) ;

                for (String currentField:fields){
                    Node px  = getNode(currentField);
                }
                int i=0;
                while (i<fields.size()){
                    Node currentField = getNode(fields.get(i));
                    if (i==fields.size()-1){
                        bp.add(new Triple(pBNode, currentField ,getVNode(field,resource))) ;
                    } else {
                        bp.add(new Triple(pBNode,currentField,pBNode = NodeFactory.createBlankNode()));
                    }
                    i++;
                }
            }
        }

        //generating the query graph patterns for every composite fields
        Map<String, QResource> currentQResources = resource.getqResources();
        for (String field:currentQResources.keySet()){
               Node predicateNode = getNode(field);

               //check if qresource has id
               Node subjectNode = NodeFactory.createBlankNode();
               QResource currentQResource = currentQResources.get(field);
               if (currentQResource.hasId()){
                   String resourceIRI = defaultResourceNamespace + resource.getrId();
                   subjectNode = NodeFactory.createURI(resourceIRI);
               }
               BasicPattern newBPs = currentQResource.generateBasicPattern(this,subjectNode,mainSubjectNode,predicateNode);
               bp.addAll(newBPs);

        }

        Op op = new OpBGP(bp) ;

        Query q = OpAsQuery.asQuery(op);


        for (String prefix:prefixes.keySet()){
            q.setPrefix(prefix,prefixes.get(prefix));
        }
        return q;
    }
    Node getNode(String field){
        String predicateIRI;
        if (resourceExceptions.containsKey(field)){
            predicateIRI = resourceExceptions.get(field);
        } else {
            predicateIRI = defaultClassNamespace + field;
        }
        Node predicateNode = NodeFactory.createURI(predicateIRI);
        return predicateNode;
    }

    Node getVNode(String field,QResource qResource){
        if (field.contains(".")){
            field = field.replace(".","");
        }
        return Var.alloc(field+qResource.getQid());
    }

}

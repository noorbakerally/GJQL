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
import org.apache.jena.sparql.core.Var;
import org.apache.jena.vocabulary.RDF;

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
            String predicateIRI;
            if (resourceExceptions.containsKey(field)){
                predicateIRI = resourceExceptions.get(field);
            } else {
                predicateIRI = defaultClassNamespace + field;
            }
            Node predicateNode = NodeFactory.createURI(predicateIRI);
            Var variableNode = Var.alloc(field);
            bp.add(new Triple(mainSubjectNode, predicateNode ,variableNode)) ;
        }

        Op op = new OpBGP(bp) ;
        Query q = OpAsQuery.asQuery(op);

        for (String prefix:prefixes.keySet()){
            q.setPrefix(prefix,prefixes.get(prefix));
        }
        return q;
    }
}

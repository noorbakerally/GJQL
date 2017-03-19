/**
 * Created by bakerally on 3/17/17.
 */

import org.apache.jena.datatypes.xsd.XSDDatatype ;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.algebra.Transformer;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.expr.E_LessThan;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.NodeValue;

public class Test {
    public static void main (String [] args){
        String query1= "SELECT ?name where { <http://www.example.com/P500> a <http://www.example.com/Parking>; " +
                "<http://www.example.com/name> ?name .}";

        String query2= "SELECT ?name where { <http://www.example.com/P500> <http://www.example.com/name> ?name;" +
                " a <http://www.example.com/Parking> .}";

        Query q1 = QueryFactory.create(query1);
        Query q2 = QueryFactory.create(query2);



        System.out.println("Compare query pattern");
        System.out.println(q1.getQueryPattern().equalTo(q2.getQueryPattern(),null));

        Op qop1 = Algebra.compile(q1) ;
        Op qop2 = Algebra.compile(q2) ;

        System.out.println("Compare query operator");
        System.out.println(qop1.equalTo(qop2,null));

    }


}

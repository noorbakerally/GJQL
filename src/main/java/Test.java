/**
 * Created by bakerally on 3/17/17.
 */

import org.apache.jena.datatypes.xsd.XSDDatatype ;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
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
        String BASE = "http://example/" ;
        BasicPattern bp = new BasicPattern() ;
        Var var_x = Var.alloc("x") ;
        Var var_z = Var.alloc("z") ;

        // ---- Build expression
        bp.add(new Triple(var_x, NodeFactory.createURI(BASE+"p"), var_z)) ;
        Op op = new OpBGP(bp) ;
        //Expr expr = ExprUtils.parse("?z < 2 ") ;
        Expr expr = new E_LessThan(new ExprVar(var_z), NodeValue.makeNodeInteger(2)) ;
        op = OpFilter.filter(expr, op) ;



        // ---- Example setup
        Model m = makeModel() ;
        m.write(System.out, "TTL") ;
       /* System.out.println("--------------") ;
        System.out.print(op) ;
        System.out.println("--------------") ;*/

        // ---- Execute expression
        QueryIterator qIter = Algebra.exec(op, m.getGraph()) ;

        Query q = OpAsQuery.asQuery(op);
        Query q1 = OpAsQuery.asQuery(op);



        System.out.println(op);
    }

    private static Model makeModel()
    {
        String BASE = "http://example/" ;
        Model model = ModelFactory.createDefaultModel() ;
        model.setNsPrefix("", BASE) ;
        Resource r1 = model.createResource(BASE+"r1") ;
        Resource r2 = model.createResource(BASE+"r2") ;
        Property p1 = model.createProperty(BASE+"p") ;
        Property p2 = model.createProperty(BASE+"p2") ;
        RDFNode v1 = model.createTypedLiteral("1", XSDDatatype.XSDinteger) ;
        RDFNode v2 = model.createTypedLiteral("2", XSDDatatype.XSDinteger) ;

        r1.addProperty(p1, v1).addProperty(p1, v2) ;
        r1.addProperty(p2, v1).addProperty(p2, v2) ;
        r2.addProperty(p1, v1).addProperty(p1, v2) ;

        return model  ;
    }
}

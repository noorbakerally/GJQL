package fr.opensensingcity.GJQL.qresource;

import com.google.gson.JsonElement;
import fr.opensensingcity.GJQL.mapping.Mapping;
import fr.opensensingcity.GJQL.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.ResultBinding;
import org.apache.jena.sparql.syntax.ElementGroup;

import java.util.*;

/**
 * Created by bakerally on 3/19/17.
 */
public abstract class QResource {
    String qid;

    QResource parent;

    String rId;
    String rType;
    List<String> atomicFields;
    Map<String, QResource> qResources;
    Map <String,Result> results;

    QResource rootParent;

    public QResource(){
        atomicFields = new ArrayList<String>();
        qResources = new HashMap<String, QResource>();
        Random random = new Random();
        qid = String.valueOf(Math.abs(random.nextLong()));
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }
    public boolean hasId(){ return rId!=null;}
    public boolean hasType(){return rType !=null;}
    public String getrType() {
        return rType;
    }

    public void setrType(String rType) {
        this.rType = rType;
    }

    public List<String> getAtomicFields() {
        return atomicFields;
    }

    public void addAtomicFields(String field){
        atomicFields.add(field);
    }
    public void addQResource(String key,QResource qResource){
        qResource.parent = this;
        qResources.put(key,qResource);
    }
    public void setAtomicFields(List<String> atomicFields) {
        this.atomicFields = atomicFields;
    }
    public abstract  String serializeResult(ResultSet bindings);

    public abstract ElementGroup generateExpression(Mapping mapping, Node subjectNode, Node  linkNode, Node predicateNode);
    public abstract JsonElement serializeSolution(QuerySolution solution);

    public Map<String, QResource> getqResources() {
        return qResources;
    }

    public void setqResources(Map<String, QResource> qResources) {
        this.qResources = qResources;
    }
    public String getQid() {
        return qid;
    }


    public void setQid(String qid) {
        this.qid = qid;
    }

    public String toStr(){
        String pqid = null;
        String space = "     ";
        int numParents = 1;

        QResource currentParent = parent;
        while (currentParent !=null){
            numParents++;
            currentParent = currentParent.parent;
        }

        if (parent!=null){
            pqid = parent.qid;
        }
        space = StringUtils.repeat(space,numParents);
        String strDetails =  space + "Parent QId:"+pqid+" QId:"+qid+"\n"+space+"=============== \n"+space+"    "+"Id:"+getrId()+" Type:"+getrType();


        if (atomicFields.size() > 0){
            strDetails = strDetails +" \n"+space+"    "+"Atomic fields:\n"+space+"    "+"--------------";
            for (String field:atomicFields){
                strDetails = strDetails + "\n"+space+"   "+"    "+"- "+field+"\n";
            }
        }

        if (qResources.keySet().size() > 0){
            strDetails = strDetails +" \n"+space+"    "+"Child Resources:\n"+space+"    "+"----------------";
            strDetails = strDetails + "\n";
            for (String strQResource:qResources.keySet()){
                strDetails = strDetails + qResources.get(strQResource).toStr();
            }
        }



        return strDetails;
    }

}

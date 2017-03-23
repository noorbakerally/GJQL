package fr.opensensingcity.GJQL.qresource;

import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.ResultBinding;

import java.util.List;
import java.util.Map;

/**
 * Created by bakerally on 3/19/17.
 */
public abstract class QResource {
    String qid;



    String rId;
    String rType;
    List<String> atomicFields;
    Map<String, QResource> qResources;

    public abstract Query generateSPARQL(Mapping mapping);

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
        qResources.put(key,qResource);
    }
    public void setAtomicFields(List<String> atomicFields) {
        this.atomicFields = atomicFields;
    }
    public abstract  String serializeResult(Object results);

    public abstract BasicPattern generateBasicPattern(Mapping mapping, Node subjectNode,Node  linkNode,Node predicateNode);

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
}

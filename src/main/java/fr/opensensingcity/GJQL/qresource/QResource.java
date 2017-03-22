package fr.opensensingcity.GJQL.qresource;

import fr.opensensingcity.GJQL.mapping.Mapping;
import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.ResultBinding;

import java.util.List;

/**
 * Created by bakerally on 3/19/17.
 */
public abstract class QResource {
    String rId;
    String rType;
    List<String> atomicFields;
    List <QResource> qResources;
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
    public void setAtomicFields(List<String> atomicFields) {
        this.atomicFields = atomicFields;
    }
    public abstract  String serializeResult(ResultSet results);
}

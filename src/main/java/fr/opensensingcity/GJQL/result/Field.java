package fr.opensensingcity.GJQL.result;

/**
 * Created by bakerally on 3/19/17.
 */
public abstract  class Field {
    String fieldName;
    String fieldValue;
    String datatypeIRI;

    public Field(String fieldName,String fieldValue){
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getDatatypeIRI() {
        return datatypeIRI;
    }

    public void setDatatypeIRI(String datatypeIRI) {
        this.datatypeIRI = datatypeIRI;
    }


}

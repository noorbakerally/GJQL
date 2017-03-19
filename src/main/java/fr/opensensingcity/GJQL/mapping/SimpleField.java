package fr.opensensingcity.GJQL.mapping;

/**
 * Created by bakerally on 3/19/17.
 */
public class SimpleField extends Field {

    public SimpleField(String rawExpression){
        this.rawExpression = rawExpression;
    }
    public Object evaluate() {
        return null;
    }
}

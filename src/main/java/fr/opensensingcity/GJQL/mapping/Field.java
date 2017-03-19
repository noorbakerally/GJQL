package fr.opensensingcity.GJQL.mapping;

/**
 * Created by bakerally on 3/19/17.
 */
public abstract  class Field {
    String rawExpression;
    public  abstract Object evaluate();
}

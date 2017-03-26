package fr.opensensingcity.GJQL.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bakerally on 3/26/17.
 */
public abstract class Result {
    List<Field> fields;
    public  Result(){
        fields = new ArrayList<Field>();
    }
}

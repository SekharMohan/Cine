package com.cine.utils;

/**
 * Created by sekhar on 23/03/17.
 */

public class ValidationUtil {

    public static boolean checkEmptyFields(String...fields){
        for(String field:fields){
            if(field.isEmpty()){
                return true;
            }
        }
        return false;
    }
}

package com.roadtrippies.app.roadtrippies;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.api.util.StringUtils;

/**
 * Created by jesse on 26-6-2017.
 */

public class Parser {

    //String example = "where is the festival?";


    public static void ParseResult (String result){
        ArrayList<String>  keywords = new ArrayList<>();
        keywords.add("what");
        keywords.add("where");
        keywords.add("when");
        List<String> strings = java.util.Arrays.asList(result.split(" |,|\\."));

        for (String s : strings){
            Boolean found = keywords.contains(s.toLowerCase());
            if (found){
                System.out.println("Found : " + s);
            }
            else{
                System.out.println("Not found");
            }
        }
    }
}

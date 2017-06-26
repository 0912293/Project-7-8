package com.roadtrippies.app.roadtrippies;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.api.util.StringUtils;

/**
 * Created by jesse on 26-6-2017.
 */

public class Parser {

    //String example = "where is the festival?";


    public static void ParseResult(String string_to_parse) {
//        ArrayList<String> keywords = new ArrayList<>();
//        keywords.add("what");
//        keywords.add("where");
//        keywords.add("when");

        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile("\".*?\"").matcher(string_to_parse);

        while (m.find())

        {
            allMatches.add(m.group());

        }

        Iterator<String> iterator = allMatches.iterator();
        while (iterator.hasNext())

        {
            System.out.println(iterator.next());
        }
    }
}

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

        ArrayList<String> stringMatches = new ArrayList<>();
        ArrayList<Integer> intMatches = new ArrayList<>();
        Matcher m = Pattern.compile("\".*?\"").matcher(string_to_parse);
        Matcher m2 = Pattern.compile("\\d+").matcher(string_to_parse);

        while (m.find())
        {
            stringMatches.add(m.group());
        }

        while (m2.find())
        {
            intMatches.add(Integer.parseInt(m2.group()));
        }
        ;
        showStrings(deleteUnwanted(stringMatches));
        showInts(intMatches);
    }

    private static void showStrings (List<String> list){
        for (String s : list) {

            System.out.println(s);
        }
    }

    private static void showInts (List<Integer> list){
        for (Integer i : list) {
            System.out.println(i);
        }
    }

    private static ArrayList<String> deleteUnwanted (ArrayList<String> list){
        ArrayList<String> words = new ArrayList<>();
        words.add("amount");
        words.add("unit");

        for (String s: list) {
            if (words.contains(s)){
                list.remove(s);
            }
        }

        return list;
    }
}

package com.nokia.ices.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {
    public static void main(String[] args){
        Pattern pattern = Pattern.compile("^admin$");
        
        Matcher m = pattern.matcher("aadmin");

        boolean b= m.matches();
        System.out.println(b);

    }
}

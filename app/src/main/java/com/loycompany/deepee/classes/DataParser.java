package com.loycompany.deepee.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lloyd on 19/01/2018.
 */

public class DataParser {
    public static String getCategories(String cats, int selectIndex){
        int openBrackets = 0, closeBrackets = 0;
        boolean searching_for_close_bracket = false;

        String append_to = "";

        int index = 0;

        for (int i = 0; i < cats.length(); i++){
            if (cats.substring(i, i+1).equals("(")){
                ++openBrackets;
                searching_for_close_bracket = true;
            } else if (cats.substring(i, i+1).equals(")")){
                ++closeBrackets;
            }

            if (searching_for_close_bracket){
                append_to = append_to + cats.substring(i, i+1);

                if (openBrackets == closeBrackets){
                    searching_for_close_bracket = false;
                    if (index != selectIndex){
                        append_to = "";
                        ++index;
                    } else {
                        i = cats.length();
                    }
                }
            }
        }

        return append_to.substring(1,append_to.length() - 1);
    }

    public static String getCategories(String cats){
        int openBrackets = 0;
        int closeBrackets = 0;

        boolean searching_for_close_bracket = false;

        String append_to = "";

        for (int i = 0; i < cats.length(); i++){
            if (cats.substring(i, i+1).equals("(")){
                ++openBrackets;

                searching_for_close_bracket = true;

            } else if (cats.substring(i, i+1).equals(")")){
                ++closeBrackets;
            }

            if (searching_for_close_bracket){
                if (openBrackets == closeBrackets){
                    searching_for_close_bracket = false;
                }
            } else {
                append_to = append_to + cats.substring(i, i+1);
            }
        }

        return append_to;
    }

    public static List<String> javaexplode(String delimiter, String content){
        int _lastpos = 0;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < content.length(); i++){
            if(content.substring(i, i + 1).equals(delimiter)){
                list.add(content.substring(_lastpos , i));
                _lastpos = i + 1;
            }
        }

        list.add(content.substring(_lastpos, content.length()));
        return list;
    }

    public static String javaimplode(String delimeter, List<String> content){
        String to_return = "";

        for (int i = 0; i < content.size(); i++){
            if (i == 0) to_return = content.get(i);
            else to_return = to_return + delimeter + content.get(i);
        }

        return to_return;
    }
}


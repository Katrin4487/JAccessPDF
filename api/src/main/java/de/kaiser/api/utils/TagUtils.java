package de.kaiser.api.utils;

import java.util.HashMap;
import java.util.Map;

public class TagUtils {

    public static void addTag(StringBuilder builder, String tag, String value) {
        builder.append("<").append(tag).append(">").append("\n");
        builder.append(value);
        builder.append("\n").append("</").append(tag).append(">");
        builder.append("\n");
    }

    public static String addSomethingTagWithParams(String praefix, String tagName, String content, Map<String, String> params) {

        StringBuilder builder = new StringBuilder();

        builder.append("<").append(praefix).append(":").append(tagName);
        if(params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
            }
        }
        if(content!=null){
            builder.append(">\n");
            builder.append(content);
            builder.append("\n");
            builder.append("</").append(praefix).append(":").append(tagName).append(">\n");
        }else{
            builder.append("/>\n");
        }

        return builder.toString();
    }

    public static String addSomethingTagWithParam(String praefix, String tagName, String content, String key, String value) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(key, value);
        return addSomethingTagWithParams(praefix, tagName, content, params);
    }

        public static String addFoTagWithParams(String tagName, String content, Map<String,String> params){
        return addSomethingTagWithParams("fo", tagName, content, params);
    }

    public static String addFoTagWithParams(String tagName, String content, String key, String value){
        Map<String,String> params = new HashMap<String,String>();
        params.put(key, value);
        return addSomethingTagWithParams("fo", tagName, content, params);
    }

    public static String addXlsTagWithParams(String tagName, String content, Map<String, String> params) {
        return addSomethingTagWithParams("xls", tagName, content, params);
    }

    public static String addXslTagWithParam(String tagName, String content, String key, String value) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(key, value);
        return addSomethingTagWithParams("xsl", tagName, content, params);
    }


}

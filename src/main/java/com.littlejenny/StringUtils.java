package com.littlejenny;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

public class StringUtils {
    /**
     delimiter = ? ,strings = {a,b,c} | -> a?b?c
     */
    public static String combineByDelimiter(String delimiter, List<String> strings){
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string+delimiter);
        }
        builder = builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
    /*
    com.littlejenny -> com/littlejenny
     */
    public static String dotToSlash(String name) {
        return name.replaceAll("\\.", "/");
    }
    public static String getJarRootPath(URL url) {
        // jar:file:/C: -> file:/C:
        String fileUrl = url.getFile();
        // file:/C:/**/demo-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/com/example/demo/paypal
        int pos = fileUrl.indexOf('!');
        if(pos == -1){
            throw new RuntimeException("URL : " + url.toString() +"不是一個JAR包路徑");
        }
        return fileUrl.substring(5, pos);
    }
    public static boolean isClassFile(String name) {
        return name.endsWith(".class");
    }
    public static String trimExtension(String name) {
        int pos = name.indexOf('.');
        if (-1 != pos) {
            return name.substring(0, pos);
        }
        return name;
    }
}

package com.bishe.hjh.ros.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by HJH on 2016/4/7.
 */
public class StreamUtils {
    public static String parserStream(InputStream inputStream) throws IOException {
        InputStreamReader reader=new InputStreamReader(inputStream);
        BufferedReader br=new BufferedReader(reader);
        StringWriter sw=new StringWriter();
        String s=null;
        if ((s=br.readLine())!=null){
            sw.write(s);
        }
        br.close();
        reader.close();
        return sw.toString();
    }
}

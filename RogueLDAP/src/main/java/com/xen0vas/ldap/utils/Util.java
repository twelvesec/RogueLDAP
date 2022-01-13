package com.xen0vas.ldap.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Random;

public class Util {
    public static String getRandomString() {
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    public static String getClassCode(@SuppressWarnings("rawtypes") Class classCode) throws Exception {
        byte[] bytes = getClassBytes(classCode);
        String result = Util.base64Encode(bytes);

        return result;
    }

    @SuppressWarnings("rawtypes")
	public static byte[] getClassBytes(Class clazz) throws Exception {
        String className = clazz.getName();
        String resoucePath = className.replaceAll("\\.", "/") + ".class";
        InputStream in = Util.class.getProtectionDomain().getClassLoader().getResourceAsStream(resoucePath);
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baous = new ByteArrayOutputStream();
        int len = 0;
        while((len = in.read(bytes)) != -1){
            baous.write(bytes, 0 , len);
        }

        in.close();
        baous.close();

        return baous.toByteArray();
    }

    @SuppressWarnings("rawtypes")
	public static String base64Encode(byte[] bytes) throws Exception{
        String result;

        try{
            Class clazz = Class.forName("java.util.Base64");
            @SuppressWarnings("unchecked")
			Method method = clazz.getDeclaredMethod("getEncoder");
            Object obj = method.invoke(null);
            method = obj.getClass().getDeclaredMethod("encodeToString", byte[].class);
            obj = method.invoke(obj, bytes);
            result = (String)obj;
        }catch(ClassNotFoundException e){
            Class clazz = Class.forName("sun.misc.BASE64Encoder");
            @SuppressWarnings("unchecked")
			Method method = clazz.getMethod("encodeBuffer", byte[].class);
            Object obj = method.invoke(clazz.newInstance(), bytes);
            result = (String)obj;
            result = result.replaceAll("\r|\n|\r\n", "");
        }

        return result;
    }

    @SuppressWarnings("unchecked")
	public static byte[] base64Decode(String str) throws Exception{
        byte[] bytes;

        try{
            @SuppressWarnings("rawtypes")
			Class clazz = Class.forName("java.util.Base64");
            Method method = clazz.getDeclaredMethod("getDecoder");
            Object obj = method.invoke(null);
            method = obj.getClass().getDeclaredMethod("decode", String.class);
            obj = method.invoke(obj, str);
            bytes = (byte[]) obj;
        }catch(ClassNotFoundException e){
            @SuppressWarnings("rawtypes")
			Class clazz = Class.forName("sun.misc.BASE64Decoder");
            Method method = clazz.getMethod("decodeBuffer", String.class);
            Object obj = method.invoke(clazz.newInstance(), str);
            bytes = (byte[]) obj;
        }

        return bytes;
    }

    public static byte[] serialize(Object ref) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(ref);
        return out.toByteArray();
    }

    public static String getCmdFromBase(String base) throws Exception {
        int firstIndex = base.lastIndexOf("/");
        String cmd = base.substring(firstIndex + 1);

        int secondIndex = base.lastIndexOf("/", firstIndex - 1);
        if(secondIndex < 0){
            secondIndex = 0;
        }
        
        byte[] bytes = Util.base64Decode(cmd);
        cmd = new String(bytes);
        return cmd;
    }

    public static String[] getIPAndPortFromBase(String base) throws NumberFormatException{
        int firstIndex = base.lastIndexOf("/");
        String port = base.substring(firstIndex + 1);

        int secondIndex = base.lastIndexOf("/", firstIndex - 1);
        if(secondIndex < 0){
            secondIndex = 0;
        }

        String ip = base.substring(secondIndex + 1, firstIndex);
        return new String[]{ip, Integer.parseInt(port) + ""};
    }
}
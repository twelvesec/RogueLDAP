package com.xen0vas.ldap;

import com.xen0vas.ldap.controllers.LdapController;
import com.xen0vas.ldap.controllers.LdapMapping;
import com.xen0vas.ldap.utils.Config;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import org.reflections.Reflections;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.util.Set;
import java.util.TreeMap;


public class LdapServer extends InMemoryOperationInterceptor {

    TreeMap<String, LdapController> routes = new TreeMap<String, LdapController>();

    public static void start() {
        try {
            InMemoryDirectoryServerConfig serverConfig = new InMemoryDirectoryServerConfig("dc=example,dc=com");
            serverConfig.setListenerConfigs(new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"),
                    Config.ldapPort,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));

            serverConfig.addInMemoryOperationInterceptor(new LdapServer());
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(serverConfig);
            ds.startListening();
            System.out.println("\n[*] LDAP Server Started on port " + Config.ldapPort + "...");
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public LdapServer() throws Exception {

        Set<Class<?>> controllers = new Reflections(this.getClass().getPackage().getName())
                .getTypesAnnotatedWith(LdapMapping.class);

        for(Class<?> controller : controllers) {
            Constructor<?> cons = controller.getConstructor();
            LdapController instance = (LdapController) cons.newInstance();
            String[] mappings = controller.getAnnotation(LdapMapping.class).uri();
            for(String mapping : mappings) {
                if(mapping.startsWith("/"))
                    mapping = mapping.substring(1);

                routes.put(mapping, instance);
            }
        }
    }

/*
    public void processSearchResult(InMemoryInterceptedSearchResult result) {
        String base = result.getRequest().getBaseDN();
        LdapController controller = null;
        for(String key: routes.keySet()) {
            if(base.toLowerCase().startsWith(key)) {
                controller = routes.get(key);
                break;
            }
        }

        if(controller == null){
            return;
        }

        try {
            controller.process(base);
            controller.sendResult(result, base);
        } catch (Exception e1) {
            System.out.println("[!] Exception: " + e1.getMessage());
        }
    }
    */
}
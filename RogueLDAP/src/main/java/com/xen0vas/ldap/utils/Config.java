package com.xen0vas.ldap.utils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.UnixStyleUsageFormatter;

public class Config {
    public static String codeBase;

    @Parameter(names = {"-i", "--ip"}, description = "Local ip address ", required = false, order = 1)
    public static String ip = "192.168.56.1";

    @Parameter(names = {"-l", "--ldapPort"}, description = "Ldap bind port", order = 2)
    public static int ldapPort = 6389;

    @Parameter(names = {"-p", "--httpPort"}, description = "Http bind port", order = 3)
    public static int httpPort = 1337; 

    @Parameter(names = {"-u", "--usage"}, description = "Show usage", order = 5)
    public static boolean showUsage;

    @Parameter(names = {"-h", "--help"}, help = true, description = "Show this help")
    private static boolean help = false;


    public static void applyCmdArgs(String[] args) {
        JCommander jc = JCommander.newBuilder()
                .addObject(new Config())
                .build();
        try{
            jc.parse(args);
        }catch(Exception e){
            if(!showUsage){
                System.out.println("Error: " + e.getMessage() + "\n");
                help = true;
            }
        }

        if(showUsage){
            System.out.println("Supported LADP Queries：");
            String prefix = "ldap://" + Config.ip + ":" + Config.ldapPort + "/";
            System.out.println("    " + prefix + "/[base64 commandd]");
           
            System.exit(0);
        }

        jc.setUsageFormatter(new UnixStyleUsageFormatter(jc));

        if(help) {
            jc.usage(); 
            System.exit(0);
        }

        Config.codeBase = "http://" + Config.ip + ":" + Config.httpPort + "/";
    }
}

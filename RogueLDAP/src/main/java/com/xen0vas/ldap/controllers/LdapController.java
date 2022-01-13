package com.xen0vas.ldap.controllers;


import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;

public interface LdapController {
    void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception;
    void process(String base) ;
}

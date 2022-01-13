package com.xen0vas.ldap.utils;

import java.util.concurrent.TimeUnit;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

public class Cache {
    private static ExpiringMap<String, byte[]> map = ExpiringMap.builder()
            .maxSize(1000)
            .expiration(30, TimeUnit.SECONDS)
            .variableExpiration()
            .expirationPolicy(ExpirationPolicy.CREATED)
            .build();

    public static byte[] get(String key){
        return map.get(key);
    }

    public static void set(String key, byte[] bytes){
        map.put(key, bytes);
    }

    public static boolean contains(String key){
        return map.containsKey(key);
    }

    public static void remove(String key){
        map.remove(key);
    }
}

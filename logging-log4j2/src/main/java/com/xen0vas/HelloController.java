package com.xen0vas;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static Logger log = LogManager.getLogger(HelloController.class.getName());
    
    @RequestMapping(value = "/lol", method = RequestMethod.POST)
    public String main(@RequestBody String vuln) {
    	 
    	//System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true"); 
    	
        log.error("Hello from Log4j2 - vuln : {} ", () -> vuln);
       
        return "Welcome";
    }
   /*
        @GetMapping("/")
        public String index(@RequestHeader("X-Api-Version") String apiVersion) {
            log.info("Received a request for API version " + apiVersion);
            return "Hello, world!";
        }
    */
}

package com.webel.jsf;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Uses JSF2.3 style CDI-compatible @Named @RequestScoped backing bean.
 * 
 * @author darrenkelly
 */
@Named
@RequestScoped
public class Jsf23RequestBean extends AbstractBackingBean {

    private static final Logger logger = Logger.getLogger(Jsf23RequestBean.class.getName());
    
    @Override
    protected Logger myLogger() {
        return logger;
    }
    
    /**
     * Creates a new instance.
     */
    public Jsf23RequestBean() {
    }
        
}

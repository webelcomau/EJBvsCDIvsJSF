package com.webel.jsf;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Application-wide options for the test.
 * 
 * @author darrenkelly
 */
@Named
@ApplicationScoped
public class AppOptions extends AbstractBackingBean {

    private static final Logger logger = Logger.getLogger(AppOptions.class.getName());
    
    @Override
    protected Logger myLogger() {
        return logger;
    }
    
    /**
     * Creates a new instance.
     */
    public AppOptions() {
    }
    
    private boolean doForceRemoveEjb = false;

    public boolean isDoForceRemoveEjb() {
        return doForceRemoveEjb;
    }

    public void setDoForceRemoveEjb(boolean doForceRemoveEjb) {
        this.doForceRemoveEjb = doForceRemoveEjb;
    }
    
        
}

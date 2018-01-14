package com.webel.jsf;

import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 * Uses JSF2.3 style CDI compatible @Named backing bean.
 * 
 * @author darrenkelly
 */
@Named
@ViewScoped
public class Jsf23ViewBean extends AbstractBackingBean {

    private static final Logger logger = Logger.getLogger(Jsf23ViewBean.class.getName());
    
    @Override
    protected Logger myLogger() {
        return logger;
    }
    
    /**
     * Creates a new instance.
     */
    public Jsf23ViewBean() {
    }
        
}

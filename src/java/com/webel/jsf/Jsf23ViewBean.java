package com.webel.jsf;

import com.webel.ejb.StatefulCommon;
import com.webel.ejb.StatelessCommon;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped; // CDI-compatible JSF2.2 version

/**
 * Uses JSF2.3 style CDI compatible @Named backing bean.
 * 
 * @author darrenkelly
 */
@Named
@ViewScoped
public class Jsf23ViewBean extends AbstractViewBean {

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

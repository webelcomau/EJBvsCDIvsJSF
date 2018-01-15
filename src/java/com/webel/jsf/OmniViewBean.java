package com.webel.jsf;

import java.util.logging.Logger;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped; // 3rd party CDI-compatible

/**
 * Visit <a href="http://showcase.omnifaces.org/cdi/ViewScoped">OmniFaces @ViewScoped</a>.
 * 
 * @author darrenkelly
 */
@Named
@ViewScoped
public class OmniViewBean extends AbstractBackingBean {

    private static final Logger logger = Logger.getLogger(OmniViewBean.class.getName());
    
    @Override
    protected Logger myLogger() {
        return logger;
    }
    
    /**
     * Creates a new instance.
     */
    public OmniViewBean() {
        super(ViewScoped.class);
    }
        
    
}

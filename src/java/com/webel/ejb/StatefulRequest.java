package com.webel.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;

/**
 * This is a stateful EJB with explicit request scope; Inject this into a backing bean using @Inject.
 * 
 * @author darrenkelly
 */
@Stateful
@RequestScoped
public class StatefulRequest extends StatefulCommon {

    private static final Logger logger = Logger.getLogger(StatefulRequest.class.getName());
    
    @Override
    protected Logger myLogger() {return logger;}
    
    /**
     * Creates a new instance.
     */
    public StatefulRequest() {
    }        

}

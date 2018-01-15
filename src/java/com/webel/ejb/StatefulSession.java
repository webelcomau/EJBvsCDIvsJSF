package com.webel.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

/**
 * A stateful EJB with explicit session scope; Inject this into a backing bean using @Inject.
 * 
 * @author darrenkelly
 */
@Stateful
@SessionScoped
public class StatefulSession extends StatefulCommon {

    private static final Logger logger = Logger.getLogger(StatefulSession.class.getName());
    
    @Override
    protected Logger myLogger() {return logger;}
    
    /**
     * Creates a new instance.
     */
    public StatefulSession() {
    }        

}

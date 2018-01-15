package com.webel.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateful;

/**
 * A stateful EJB without explicit scope; Inject this into a backing bean using @EJB.
 * 
 * @author darrenkelly
 */
@Stateful
public class StatefulEjb extends StatefulCommon {

    private static final Logger logger = Logger.getLogger(StatefulEjb.class.getName());
    
    @Override
    protected Logger myLogger() {return logger;}
    
    /**
     * Creates a new instance.
     */
    public StatefulEjb() {
    }        

}

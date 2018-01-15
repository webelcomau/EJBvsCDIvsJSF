package com.webel.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 * This is a stateless EJB (without explicit scope); Inject this into a backing bean using @EJB.
 * 
 * @author darrenkelly
 */
@Stateless
public class StatelessEjb extends StatelessCommon {

    private static final Logger logger = Logger.getLogger(StatelessEjb.class.getName());
    
    @Override
    protected Logger myLogger() {return logger;}
    
    /**
     * Creates a new instance.
     */
    public StatelessEjb() {
    }    
    
}

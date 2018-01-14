package com.webel.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateful;

/**
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

package com.webel.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.enterprise.context.Dependent;

/**
 * A stateful EJB with explicit dependent pseudo-scope; Inject this into a backing bean using @Inject.
 * 
 * @author darrenkelly
 */
@Stateful
@Dependent
public class StatefulDepend extends StatefulCommon {

    private static final Logger logger = Logger.getLogger(StatefulDepend.class.getName());
    
    @Override
    protected Logger myLogger() {return logger;}
    
    /**
     * Creates a new instance.
     */
    public StatefulDepend() {
    }        

}

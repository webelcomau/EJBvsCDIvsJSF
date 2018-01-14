package com.webel.ejb;

import com.webel.all.All;
import java.util.logging.Logger;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;

/**
 * 
 * @author darrenkelly
 */
@Stateful
public class StatefulCommon extends All {

    private static final Logger logger = Logger.getLogger(StatefulCommon.class.getName());
    
    @Override
    protected Logger myLogger() {return logger;}
    
    /**
     * Creates a new instance.
     */
    protected StatefulCommon() {
    }    
    

    @PostActivate
    public void postActivate() {
        echo("postActivate");
    }
    
    @PrePassivate
    public void prePassivate() {
        echo("prePassivate");
    }    
    
    @Remove
    public void remove() {
        echo("remove");
    }
}

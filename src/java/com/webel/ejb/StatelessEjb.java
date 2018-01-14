package com.webel.ejb;

import com.webel.all.All;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
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

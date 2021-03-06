package com.webel.ejb;

import com.webel.all.All;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 * 
 * @author darrenkelly
 */
@Stateless
public class StatelessCommon extends All {

    private static final Logger logger = Logger.getLogger(StatelessCommon.class.getName());
       
    @Override
    protected Logger myLogger() {return logger;}
    
    /**
     * Creates a new instance.
     */
    public StatelessCommon() {
    }    
    
    private int pseudoState = 0;
    
    protected void incPseudoState() {
        pseudoState++;
    }
    
    @Override
    public void exec() {
        super.exec();
        incPseudoState();
        echo(String.format("pseudoState=%d",pseudoState));
    }
    
}

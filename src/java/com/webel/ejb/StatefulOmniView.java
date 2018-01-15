package com.webel.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateful;
import org.omnifaces.cdi.ViewScoped;

/**
 * A stateful EJB with explicit OmniFaces view scope; Inject this into a backing bean using @Inject.
 * 
 * This is for testing purposes only, it is not suggested here that using view scope
 * with a stateful session bean is recommended (even though it still appears to work).
 * 
 * @author darrenkelly
 */
@Stateful
@ViewScoped
public class StatefulOmniView extends StatefulCommon {

    private static final Logger logger = Logger.getLogger(StatefulOmniView.class.getName());
    
    @Override
    protected Logger myLogger() {return logger;}
    
    /**
     * Creates a new instance.
     */
    public StatefulOmniView() {
    }        

}

package com.webel.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.faces.view.ViewScoped;

/**
 * A stateful EJB with explicit CDI-compatible view scope; Inject this into a backing bean using @Inject.
 * 
 * This is for testing purposes only, it is not suggested here that using view scope
 * with a stateful session bean is recommended (even though it still appears to work).
 * 
 * @author darrenkelly
 */
@Stateful
@ViewScoped
public class StatefulViewView extends StatefulCommon {

    private static final Logger logger = Logger.getLogger(StatefulViewView.class.getName());
    
    @Override
    protected Logger myLogger() {return logger;}
    
    /**
     * Creates a new instance.
     */
    public StatefulViewView() {
    }        

}

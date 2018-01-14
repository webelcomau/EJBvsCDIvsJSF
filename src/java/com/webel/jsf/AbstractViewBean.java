package com.webel.jsf;

import com.webel.ejb.StatefulEjb;
import com.webel.ejb.StatefulInject;
import com.webel.ejb.StatelessEjb;
import com.webel.ejb.StatelessInject;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 * Base for all view bean examples.
 * 
 * @author darrenkelly
 */
abstract public class AbstractViewBean implements Serializable {

    private static final Logger logger = Logger.getLogger(AbstractViewBean.class.getName());
    
    abstract protected Logger myLogger();
    
    protected void echo(String s) {
        myLogger().log(Level.INFO, "{0}: {1}", new Object[]{this.getClass().getSimpleName(), s});
    }
    
    /**
     * Creates a new instance.
     */
    public AbstractViewBean() {
    }    
    
    public void reset() {
        //entities = null;
        echo("reset()");
    }
        
    @EJB private StatefulEjb  statefulEjb;

    @Inject private StatefulInject  statefulInject;
    
    @EJB private StatelessEjb  statelessEjb;
    
    @Inject private StatelessInject statelessInject;
    
    
    @PostConstruct
    public void postConstruct() {
        echo("postConstruct");
        myPostConstruct();        
    }
    
    //abstract protected void myPostConstruct();
    //@Override
    
    protected void myPostConstruct() {
        statefulEjb.exec();
        statefulInject.exec();
        statelessEjb.exec();    
        statelessInject.exec();
    }
    
    
    @PreDestroy
    public void preDestroy() {
        echo("preDestroy");
        myPreDestroy();
    }    
    
    //@Override
    protected void myPreDestroy() {
        
        // These MUST be invoked for @PreDestroy to be forced on @Stateful,
        // otherwise @PreDestroy only invoked by container on timeout.
        statefulEjb.remove();
        statefulInject.remove();//!Also needed even though "contextual"
        
        statefulEjb = null;
        statefulInject = null;
        
        statelessEjb = null;
        statelessInject = null;
    }
    
    public String actionDone() {
        echo("actionDone");
        return "done?faces-redirect=true";
    }
    
    public String actionNull() {
        echo("actionNull");
        return null; // ! Stay in view scope.
    }

    private String newName = "[NAME]";

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    
}

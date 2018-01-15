package com.webel.jsf;

import com.webel.all.All;
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
abstract public class AbstractBackingBean extends All {

    private static final Logger logger = Logger.getLogger(AbstractBackingBean.class.getName());

    /**
     * Creates a new instance.
     */
    public AbstractBackingBean() {
    }

    
//    private String newName = "[NAME]";
//
//    public String getNewName() {
//        return newName;
//    }
//
//    public void setNewName(String newName) {
//        this.newName = newName;
//    }
    
    @EJB
    private StatefulEjb statefulEjb;

    @Inject
    private StatefulInject statefulInject;

    @EJB
    private StatelessEjb statelessEjb;

    @Inject
    private StatelessInject statelessInject;

    @PostConstruct
    @Override
    public void postConstruct() {
        super.postConstruct();
        execEJBs();
    }

    //abstract protected void myPostConstruct();
    //@Override
    protected void execEJBs() {
        statefulEjb.exec();
        statefulInject.exec();
        statelessEjb.exec();
        statelessInject.exec();
    }

    @PreDestroy
    @Override
    public void preDestroy() {
        super.preDestroy();
        myPreDestroy();
    }

    static final private boolean DO_NULL_EJB_RESOURCES = false;

    @Inject private AppOptions appOptions;
    
    //@Override
    protected void myPreDestroy() {

        // The @Remove remove() MUST be invoked for @PreDestroy to be forced on @Stateful when
        // when @EJB used for injection, otherwise @PreDestroy only invoked by container on timeout.
        
        if (appOptions.isDoForceRemoveEjb()) {
            echo("DO force remove() on @EJB statefulEjb !");
            statefulEjb.remove(); //! Needed because not "contextual"
            
            //statefulInject.remove();// Not usually needed because "contextual"
        }
        else {
            echo("DO NOT force remove() on @EJB statefulEjb !");        
        }

        if (DO_NULL_EJB_RESOURCES) {            
            echo("force null all EJBs");
            statefulEjb = null;
            statefulInject = null;

            statelessEjb = null;
            statelessInject = null;
        }
    }

    public String actionDone() {
        echo("actionDone");
        return "done?faces-redirect=true";
    }

    public String actionNull() {
        echo("actionNull");
        return null; // ! Stay in view scope.
    }

}

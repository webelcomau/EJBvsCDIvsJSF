package com.webel.jsf;

import com.webel.all.All;
import com.webel.ejb.StatefulDepend;
import com.webel.ejb.StatefulEjb;
import com.webel.ejb.StatefulInject;
import com.webel.ejb.StatefulOmniView;
import com.webel.ejb.StatefulRequest;
import com.webel.ejb.StatefulSession;
import com.webel.ejb.StatefulViewView;
import com.webel.ejb.StatelessEjb;
import com.webel.ejb.StatelessInject;
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

    private Class scopeClass;

    /**
     * For easy access for diagnostics.
     * 
     * @return 
     */
    public Class getScopeClass() {return scopeClass;}
    
    /**
     * Creates a new instance with the given scope class.
     */
    protected AbstractBackingBean(Class scopeClass) {
        this.scopeClass = scopeClass;
    }
    
    
    @EJB
    private StatefulEjb statefulEjb;
    
    
    @Inject
    private StatefulInject statefulInject;

    @Inject
    private StatefulDepend statefulDepend;

    @Inject
    private StatefulRequest statefulRequest;
    
    @Inject
    private StatefulViewView statefulViewView;
    
    @Inject
    private StatefulOmniView statefulOmniView;

    @Inject
    private StatefulSession statefulSession;
    
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
        statefulDepend.exec();
        statefulRequest.exec();
        statefulViewView.exec();
        statefulOmniView.exec();
        statefulSession.exec();
        
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
        // @EJB used for injection, otherwise @PreDestroy only invoked by container on timeout.
        
        if (appOptions.isDoForceRemoveEjb()) {
            echo("DO force remove() on @EJB statefulEjb !");
            statefulEjb.remove(); //! Usually needed because not "contextual"
        }
        else {
            echo("SKIP: DO NOT force remove() on @EJB statefulEjb !");        
        }

        // For @RequestScoped, a @Stateful EJB injected with @Inject will have it's @PreDestroy
        // called by the container.
        //
        // But for @ViewScoped (Mojarra or OmniFaces version) @Remove remove() MUST be invoked 
        // for @PreDestroy to be forced on @Stateful when @Inject used for injection on the EJB,
        // otherwise @PreDestroy only invoked by container on timeout.
        
        if (appOptions.isDoForceRemoveInject()) {
            String $f = "DO force remove() on @Inject %s !";
            
            echo(String.format($f,"statefulInject"));            
            statefulInject.remove();// Not always needed because "contextual"
            
            echo(String.format($f,"statefulDepend"));            
            statefulDepend.remove();// Not always needed because "contextual"
            
            echo(String.format($f,"statefulRequest"));            
            statefulRequest.remove();// Not always needed because "contextual"
            
            echo(String.format($f,"statefulViewView"));            
            statefulViewView.remove();// Not always needed because "contextual"
            
            echo(String.format($f,"statefulOmniView"));            
            statefulOmniView.remove();// Not always needed because "contextual"            
            
            echo(String.format($f,"statefulSession"));            
            statefulSession.remove();// Not always needed because "contextual"            
            
        }
        else {
            String $f = "SKIP: DO NOT force remove() on @Inject %s !";            
            echo(String.format($f,"statefulInject"));                        
            echo(String.format($f,"statefulDepend"));                        
            echo(String.format($f,"statefulRequest"));                       
            echo(String.format($f,"statefulViewView"));
            echo(String.format($f,"statefulOmniView"));
            echo(String.format($f,"statefulSession"));
        }
        
        if (DO_NULL_EJB_RESOURCES) {            
            
            echo("force null all EJBs");
            
            statefulEjb = null;
            
            statefulInject = null;
            statefulDepend = null;
            statefulRequest = null;
            statefulViewView = null;
            statefulOmniView = null;
            statefulSession = null;

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

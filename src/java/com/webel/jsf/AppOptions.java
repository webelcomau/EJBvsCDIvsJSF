package com.webel.jsf;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Application-wide options for the test.
 *
 * @author darrenkelly
 */
@Named
@ApplicationScoped
public class AppOptions extends AbstractBackingBean {

    private static final Logger logger = Logger.getLogger(AppOptions.class.getName());

    @Override
    protected Logger myLogger() {
        return logger;
    }

    /**
     * Creates a new instance.
     */
    public AppOptions() {
    }

    private boolean doForceRemoveEjb = false;

    /**
     * If true, on @PreDestroy of backing beans, a force remove() will be performed on @Stateful beans that were injected with @EJB.
     * 
     * @return 
     */
    public boolean isDoForceRemoveEjb() {
        return doForceRemoveEjb;
    }

    public void setDoForceRemoveEjb(boolean doForceRemoveEjb) {
        this.doForceRemoveEjb = doForceRemoveEjb;
    }

    private boolean doForceRemoveInject = false;

    /**
     * If true, on @PreDestroy of backing beans, a force remove() will be performed on @Stateful beans that were injected with @Inject.
     * 
     * @return 
     */
    public boolean isDoForceRemoveInject() {
        return doForceRemoveInject;
    }

    public void setDoForceRemoveInject(boolean doForceRemoveInject) {
        this.doForceRemoveInject = doForceRemoveInject;
    }
    
    
    /**
     * Suggest (only) that System should Garbage Collect.
     */
    public void doGC() {
        System.gc();
    }

}

package com.webel.jsf;

import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Uses JSF2.3 style CDI-compatible @Named @SessionScoped backing bean.
 * 
 * @author darrenkelly
 */
@Named
@SessionScoped
public class Jsf23SessionBean extends AbstractBackingBean {

    private static final Logger logger = Logger.getLogger(Jsf23SessionBean.class.getName());
    
    @Override
    protected Logger myLogger() {
        return logger;
    }
    
    /**
     * Creates a new instance.
     */
    public Jsf23SessionBean() {
    }
 
    public String endSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }    
}

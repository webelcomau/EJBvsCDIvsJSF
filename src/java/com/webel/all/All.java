package com.webel.all;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Base with simple logging.
 * 
 * @author darrenkelly
 */
abstract public class All implements Serializable {
    
    abstract protected Logger myLogger();
    
    protected void echo(String s) {
        myLogger().log(Level.INFO, "{0}: {1}", new Object[]{this.getClass().getSimpleName(), s});
    }
    
    /**
     * Creates a new instance.
     */
    protected All() {
    }    
    
    public void reset() {
        echo("reset()");
    }

    public void exec() {
        echo("exec");
    }
    
    @PostConstruct
    public void postConstruct() {
        echo("postConstruct");
    }
    
    @PreDestroy
    public void preDestroy() {
        echo("preDestroy");
    }        
}

package com.webel.all;

import java.io.Serializable;
import java.util.Date;
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
        myLogger().log(
                Level.INFO,
                "{0} [{1}]: {2}",
                new Object[]{
                    String.format("%-16s", this.getClass().getSimpleName()),
                    this.myId(), s}
        );
    }

    private String $id;

    /**
     * Creates a new instance.
     */
    protected All() {
        $id = "" + new Date().getTime();
    }

    //abstract protected String myId();
    //@Override
    protected String myId() {
        return $id;
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

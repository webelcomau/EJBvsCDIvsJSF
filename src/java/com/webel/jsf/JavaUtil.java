package com.webel.jsf;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author darrenkelly
 */
@Named(value = "javaUtil")
@ApplicationScoped
public class JavaUtil implements Serializable {

    /**
     * Creates a new instance.
     */
    public JavaUtil() {
    }

    public String getJavaVersion() {
     return System.getProperty("java.version");
    }
}

## Investigation of lifecycle of EJBs under various forms of injection into JSF backing beans of various CDI scopes, with a focus on @ViewScoped beans by navigation type

*Author: Darren Kelly (Webel IT Australia, https://www.webel.com.au)*

This mini test **NetBeans IDE** web app is for demonstration and investigation of the lifecycle of @Stateless and @Stateful EJBs under @EJB and @Inject into @Named JSF "backing beans" of different CDI scopes.

The EJB session bean lifecycle callbacks @PostConstruct and @PreDestroy -  and additionally also  @PostActivate and @PrePassivate for @Stateful session beans - are logged for analysis.

This mini test web app also investigates when or whether @PreDestroy methods are called and whether garbage collection succeeds for various forms of JavaServer Faces (JSF) "backing beans" (@RequestScoped,  @ViewScoped, and @SessionScoped), which concern is inextricably linked with the lifecycle of any EJBs used by the backing beans, and how they are injected.

(See also the separate JSF-only test project https://github.com/webelcomau/JSFviewScopedNav.)

This **NetBeans IDE 8.2** project may be deployed to the bundled Glassfish 4.1.1 web app server
(or to an additionally installed Payara41 web app server). Download from:

- http://www.netbeans.org (required)

- http://www.payara.fish (optional)

You may have to set your own web application server under `Properties > Run`.



#### MOJARRA JSF IMPLEMENTATION VERSION

Currently the web app is for demonstration against the Mojarra 2.x JSF implementation series.
Use the JSF Mojarra version bundled with NetBeans8.2/Glassfish4.1.1 or install a recent version:

- Use Maven or download javax.faces.jar directly from:

  Releases: https://maven.java.net/content/repositories/releases/org/glassfish/javax.faces/

  Snapshots: https://maven.java.net/content/repositories/snapshots/org/glassfish/javax.faces/

To install by hand, stop your web app server and copy the Mojarra JAR to:

`.../NetBeans-8.2/glassfish-4.1.1/glassfish/modules/javax.faces.jar`

`.../NetBeans-8.2/payara41/glassfish/modules/javax.faces.jar`

Then restart your server:

- The test web app interrogates the Mojarra version live and displays it in the header of each web page.




#### JAVA VERSION

For use with Java7 or Java8:

- The test web app interrogates the Java version and displays it in the header of each web page.




#### DIAGNOSTIC TOOLS

You will need a tool for diagnosing memory use and references to instances of JSF beans:

- You can use the Profiler within NetBeans IDE.

- **2017-12-05 DO NOT use JVisualVM !** It gives incorrect results. When attached to GlassFish/Payara
  it gives references still held (even after @PreDestroy called) by a field sessionListeners of type 
  `com.sun.web.server.WebContainerListener` within `ContainerBase$ContainerBackgroundProcessor`, and they won't GC !  See [this forum posting](https://stackoverflow.com/questions/40569971/jsf-mojarra-vs-omnifaces-viewscoped-predestroy-called-but-bean-cant-be-gar).




#### ON SESSION TIMEOUT

Some callbacks are only invoked when the session expires, which may sometimes occur due session time-out (see `WEB/web.xml` to set the timeout value in minutes):

    <session-config>
      <session-timeout>
            30
      </session-timeout>
    </session-config>
There is currently no explicit login/logout in this test web app, but you can set this to a very small number like `1` (means 1 minute) to see what happens when a session ends.



#### ON USE OF OMNIFACES

This test web app includes also a comparison of CDI-compatible JSF bean view scope with the 3rd-party OmniFaces JSF toolkit view scope:

- A current/recent OmniFaces library is included with the project under `./lib`

- Please visit and read also: http://showcase.omnifaces.org/cdi/ViewScoped

  ​


#### ON USE OF JSF VIEW SCOPE CONTEXT PARAMETERS

In web.xml the application is set to use:

    com.sun.faces.numberOfViewsInSession 4

    com.sun.faces.numberOfLogicalViews 4
By default this OmniFaces-specific parameter is commented out:

    org.omnifaces.VIEW_SCOPE_MANAGER_MAX_ACTIVE_VIEW_SCOPES
The `javax.faces.STATE_SAVING_METHOD` defaults to 'server'.



#### **HOWTO RUN AND USE THE TEST WEB APP**


**STEP: Open the project in NetBeans IDE** and check the Project Properties:

- Under Libraries set the Java Platform to JDK1.8 (if available), otherwise JDK1.7.

- Under Run set the server to Glassfish4.1.1 or Payara41 (if installed).

Be sure to check that you choose a server with the desired Mojarra version installed (see above).

The folder `./nbproject/private` is NOT distributed with the test web app, so these settings are local.



**STEP: clean and build.**



**STEP: run in profiling mode:**

Use the NetBeans built-in Profiler, **DO NOT use JVisualVM** !

- DO NOT use the usual Run button, use instead Profile (right click context menu on Project node).

This will run the project in profiling mode (and usually restarts the web app server too).

- Under the Profile toolbar button use the pulldown (small down array right of Profile button)
  to choose Enable Multiple Modes then Telemetry (gives an overview) and Objects.
- Click the settings gear wheel icon (top right).
  - Choose to Profile Selected classes, then select these classes:

    `com.webel.jsf.Jsf23RequestBean`, `com.webel.jsf.Jsf23ViewBean`, `com.webel.jsf.OmniViewBean`

    `com.webel.ejb.StatefulEjb` `com.webel.ejb.StatefulInject` `com.webel.ejb.StatefulDep` `com.webel.ejb.StatefulRequest` `com.webel.ejb.StatelessEjb` `com.webel.ejb.StatelessInject`

    Instances for them won't be shown in the Profiler until you choose and load a matching test page.


- Note how there is also a rubbish bin icon/button for invoking Garbage Collection.




#### QUICKSTART: Understanding the different EJB session beans

There are 6 different types of EJBs investigated:

----

    // A stateful EJB without explicit scope; Inject this into a backing bean using @EJB.`
    @Stateful
    public class StatefulEjb extends StatefulCommon {

---

    // A stateful EJB without explicit scope; Inject this into a backing bean using @Inject.
    @Stateful
    public class StatefulInject extends StatefulCommon {

----

    // A stateful EJB with dependent pseudo-scope; Inject this into a backing bean using @Inject.
    @Stateful
    @Dependent
    public class StatefulDepend extends StatefulCommon {

----

    // A stateful EJB with explicit request scope; Inject this into a backing bean using @Inject.
    @Stateful
    @RequestScoped
    public class StatefulRequest extends StatefulCommon {

----

    // A stateless EJB (without explicit scope); Inject this into a backing bean using @EJB.
    @Stateless
    public class StatelessEjb extends StatelessCommon {

----

    // A stateless EJB (without explicit scope); Inject this into a backing bean using @Inject.
    @Stateless
    public class StatelessInject extends StatelessCommon {

----

These are injected into the JSF backing beans thus:

```
@EJB
private StatefulEjb statefulEjb;

@Inject
private StatefulInject statefulInject;

@Inject
private StatefulDepend statefulDepend;

@Inject
private StatefulRequest statefulRequest;

@EJB
private StatelessEjb statelessEjb;

@Inject
private StatelessInject statelessInject;
```

The exec() method of each EJB is also invoked on @PostConstruct of each backing bean.



#### QUICKSTART: Understanding typical output: CASE: @RequestScoped

The following shows typical server log output for a request scoped backing bean. (In this case, unlike with view-scoped, there is no need to navigate away from the page to see the backing bean destruction.)

http://localhost:8080/EJBvsCDIvsJSF/faces/test_request.xhtml

`Info:   StatefulEjb      [1515985472188]: postConstruct`
`Info:   StatefulInject   [1515985472190]: postConstruct`
`Info:   StatefulDepend   [1515985472190]: postConstruct`
`Info:   Jsf23RequestBean [1515985472188]: postConstruct`
`Info:   StatefulEjb      [1515985472188]: exec`
`Info:   StatefulInject   [1515985472190]: exec`
`Info:   StatefulDepend   [1515985472190]: exec`
`Info:   StatefulRequest  [1515985472193]: postConstruct`
`Info:   StatefulRequest  [1515985472193]: exec`
`Info:   StatelessEjb     [1515985389205]: exec`
`Info:   StatelessEjb     [1515985389205]: pseudoState=3`
`Info:   StatelessInject  [1515985389207]: exec`
`Info:   StatelessInject  [1515985389207]: pseudoState=3`
`Info:   Jsf23RequestBean [1515985472188]: preDestroy`
`Info:   Jsf23RequestBean [1515985472188]: SKIP: DO NOT force remove() on @EJB statefulEjb !`
`Info:   Jsf23RequestBean [1515985472188]: SKIP: DO NOT force remove() on @Inject statefulInject !`
`Info:   Jsf23RequestBean [1515985472188]: SKIP: DO NOT force remove() on @Inject statefulDepend !`
`Info:   Jsf23RequestBean [1515985472188]: SKIP: DO NOT force remove() on @Inject statefulRequest !`
`Info:   StatefulInject   [1515985472190]: preDestroy`
`Info:   StatefulDepend   [1515985472190]: preDestroy`
`Info:   StatefulRequest  [1515985472193]: preDestroy`

Note that:

- Each bean has an identifier, which is the `Date.time()` milliseconds at the moment of construction; this approach makes it very easy to track each bean and see whether and when it is destroyed (and is independent of proxies).
- On construction of a backing bean, exec() is invoked on every injected EJB.
- Stateless EJBs have a `pseudoState`, the number of times `exec()` was invoked, which enables one to see whether the same stateless bean is being offered back via the pool on separate invocations.
- You can choose whether to explicitly invoke `remove()` on stateful session beans injected via @EJB and/or on stateful session beans injected via @Inject. (See section below about option switches.)

In the case above:

- The @PostConstruct was not invoked on the @Stateless EJBs, presumably because they were already created earlier (during a separate page load) and are merely being served back from the pool.
- The @PreDestroy was automatically invoked on those @Stateful session beans <u>that were injected using @Inject</u>, **but was NOT invoked (yet) for the @Stateful EJB <u>that was injected using @EJB</u>**.

 

#### DIAGNOSING EJB RELEASE BY JSF BEAN SCOPE AND/OR IMPLEMENTATION

The initial web page is an index to 4 test cases, one for each of the bean forms below:

----

    import javax.inject.Named;
    import javax.enterprise.context.RequestScoped; // CDI-compatible version
    @Named
    @RequestScoped
    public class Jsf23RequestBean extends AbstractViewBean {

----

    import javax.inject.Named;
    import javax.faces.view.ViewScoped; // CDI-compatible JSF2.3 version
    @Named
    @ViewScoped
    public class Jsf23ViewBean extends AbstractViewBean {

----


    import javax.inject.Named;
    import org.omnifaces.cdi.ViewScoped; // 3rd party CDI-compatible
    @Named
    @ViewScoped
    public class OmniViewBean extends AbstractViewBean {

----

    import javax.inject.Named;
    import javax.enterprise.context.SessionScoped; // CDI-compatible version
    @Named
    @SessionScoped
    public class Jsf23SessionBean extends AbstractViewBean {

----

Each test page creates (usually) a new bean when first loaded. Of interest is what happens to referenced beans **and their EJBs** once the page is left (whereby the result depends on the backing bean type and navigation method used).

- Concern1: Is @PreDestroy invoked on the backing bean so there is an opportunity to clean up resources ?
- Concern2: Can the backing bean itself be garbage collected ?
  - Concern2a: Can the backing bean be immediately garbage collected ?
  - Concern2b: Can the backing  bean be garbage collected later 
    (such as when the logical number of view is hit, or the session ends) ?
- **Concern3: Is @PreDestroy invoked on the injected EJBs (session beans) ?**
  - Concern3a: Can  the session beans be immediately garbage collected ?
  - Concern3b: Can the session beans be garbage collected later ?
  - <u>Concern3c: Does remove() have to be explicitly invoked on any of the injected EJBs ?</u>
- <u>Concern4: Does it make a difference whether @Inject or @EJB is used ?</u>
- <u>Concern5: Does it make a difference what explicit scope (if any) a @Stateful EJB has ?</u>


**IMPORTANT:** The test web app here is primarily for use with immediate garbage collection forced via the Profiler, and while it can be used to investigate later garbage collection (such as after session time-out) the published test results don't address that aspect.

**IMPORTANT:** in order to observe when and/or whether `@PreDestroy` methods are invoked and whether a backing bean and any injected EJBs are garbage collected you will need a systematic approach:

1. Use the Perform GC function in your profiler BEFORE loading each test page.

2. Invocations of `@PostConstruct` and `@PreDestroy` methods are logged to the server console.
   Use the NetBeans server log console window, and clear it BEFORE loading each test page
   and AFTER navigating away from each test page (but not before first noting the output).

3. Use the Perform GC function in your profile AFTER navigating away from each test page.

You must be vigilant and watch carefully the number of instances of each bean type
in your profiler at each stage.



#### ABOUT THE TEST PAGES AND NAVIGATION CASES IN DETAIL

Each test web page has hopefully clear instructions.

Each test page (for each different backing bean type) offers the same
selection of navigation cases for leaving the scoped page and landing at
a target page `done.xhtml` (which page does not use any scoped backing beans).

Access a desired test page initially from the top-level index page (Home),
which page likewise does not use or create any scoped backing beans.

There are 3 basic subsets of navigation cases:

1. GET related navigation cases (no form): `h:link`, `h:outputLink`, and HTML <a>

   (Please ignore the distinction between links with and without redirects,
    that is for investigating something unrelated to the main concerns.)

2. Form based navigation cases: 

   - `h:commandButton` with `action` String and `h:commandButton` with action method return String.

   - A special case is remaining on the same view scoped page using 'null' navigation.

3. Web browser's page reload action.

(An additional special case, not included in the results table, is a GET on the web browser URL,
which case might be used during facelets development, but is not usually used by novice users.)


After navigating away from a test page to the target page you may:

- Navigate back to the previous scoped backing bean test page (or another).
- Navigate back to the top-level home page (index.html).
- Perform a Garbage Collection prompt action (not guaranteed to actually force GC)
- Set an application-wide option

Again: be aware of diagnostics in both your profiler and server log window at all times.

#### Special application-wide options

You can choose how the backing beans behave w.r.t. their injected EJBs when @PreDestroy is called on a backing bean:

- On @PreDestroy of backing beans, force remove() @Stateful beans injected with @EJB ? 
- On @PreDestroy of backing beans, force remove() @Stateful beans injected with @Inject ? 



#### FURTHER READING

- https://stackoverflow.com/questions/8887140/jsf-request-scoped-bean-keeps-recreating-new-stateful-session-beans-on-every-req

- https://docs.jboss.org/cdi/learn/userguide/CDI-user-guide.html#_session_beans

- Contexts and DependencyInjection for the Java EE platform (CDI 1.2 JSR 346 Maintenance)

  ​

Hoping this helps other Enterprise Java enthusiasts with this subtle and important manner,

Darren Kelly (Webel IT Australia)
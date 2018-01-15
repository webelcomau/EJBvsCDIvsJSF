## Investigation of lifecycle of EJBs under various forms of injection into JSF backing beans of various CDI scopes, with a focus on @ViewScoped beans by navigation type

*Author: Darren Kelly (Webel IT Australia, https://www.webel.com.au)*

*Thanks also to [GreenSoft Pty Ltd](www.greensoftaustralia.com) for partially sponsoring development of this test web app and investigation.*

This mini test **NetBeans IDE** web app is for investigation of the lifecycle of @Stateless and @Stateful EJBs under @EJB and @Inject into @Named JSF "backing beans" of different CDI-compatible scopes.

The EJB session bean lifecycle callbacks @PostConstruct and @PreDestroy -  and additionally also  @PostActivate and @PrePassivate for @Stateful session beans - are logged for analysis.

This mini test web app also investigates when or whether @PreDestroy methods are called and whether garbage collection succeeds for various forms of JavaServer Faces (JSF) "backing beans" (@RequestScoped,  @ViewScoped, and @SessionScoped), which concern is inextricably linked with the lifecycle of any session beans used by the backing beans, and how they are injected (via @EJB or via CDI @Inject).

(See also this separate JSF-only test project https://github.com/webelcomau/JSFviewScopedNav, which was spawned and extended to include examination of the lifecycle of EJB session beans under @Inject vs @EJB.)

This **NetBeans IDE 8.2** project may be deployed to the bundled Glassfish 4.1.1 web app server
(or to an additionally installed Payara41 web app server). Download from:

- http://www.netbeans.org (required)

- http://www.payara.fish (optional)

You may have to set your own web application server under `Properties > Run`.

### Background to @CDI vs @Inject for session beans

Keep the following from CDI-1.2 ([JSR-346](https://jcp.org/en/jsr/detail?id=346)) in mind at all times (my <u>underlining</u>):

> 'EJB components may be stateful, but are not by nature contextual. References to stateful component instances must be explicitly passed between clients and stateful instances <u>must be explicitly destroyed by the application</u>.
>
> This specification enhances the EJB component model with contextual lifecycle management.
>
> Any session bean instance obtained via the dependency injection service is a contextual instance. It is bound to a lifecycle context and is available to other objects that execute in that context. Thecontainer automatically creates the instance when it is needed by a client. <u>When the context ends, the container automatically destroys the instance</u>.'

Amongst other things, this test suite is designed to illustrate exactly what this means when working with @Stateful session beans, and what the implications of declaring explicit scope for session beans is, and how they behave differently under injection into a backing bean using @EJB vs @Inject.

Also, from the [JBoss CDI User Guide](https://docs.jboss.org/cdi/learn/userguide/CDI-user-guide.html#_session_beans):

> 'There’s no reason to explicitly declare the scope of a stateless session bean or singleton session bean. […] On the other hand, a stateful session bean may have any scope.
>
> Stateful session beans may define a remove method, annotated @Remove, that is used by the application to indicate that an instance should be destroyed. However, for a contextual instance of the bean — an instance under the control of CDI — this method may only be called by the application if the bean has scope @Dependent. For beans with other scopes, the application must let the container destroy the bean.'

The test web app includes some switches to force invocation of `remove()` on stateful session beans whenever the backing bean into which they were injected has its `@PreDestroy` method invoked (see section below on options). By default these should be OFF so you can see the intended behaviour of the container.



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

Some callbacks are only invoked automatically when the session expires, which may sometimes occur due session time-out (see `WEB/web.xml` to set the timeout value in minutes):

    <session-config>
      <session-timeout>
            30
      </session-timeout>
    </session-config>
You can set this to a very small number like `1` (means 1 minute) to see what happens when a session ends by expiration. On the test page for @SessionScoped there is also a special button for forcing a JSF session to end.



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
  - Choose to **Profile Selected Classes**, then select these classes:
    - `com.webel.jsf.Jsf23RequestBean`, `com.webel.jsf.Jsf23ViewBean`, `com.webel.jsf.OmniViewBean`
    - `com.webel.ejb.StatefulEjb` `com.webel.ejb.StatefulInject` `com.webel.ejb.StatefulDepend` `com.webel.ejb.StatefulRequest` `com.webel.ejb.StatefulViewView` `com.webel.ejb.StatefulOmniView` `com.webel.ejb.StatefulSession`
    -  `com.webel.ejb.StatelessEjb` `com.webel.ejb.StatelessInject`
  - Instances for them won't be shown in the Profiler until you choose and load a matching test page.


- Note how there is also a rubbish bin icon/button for invoking Garbage Collection.




#### QUICKSTART: Understanding the different EJB session beans

The following different types of EJB session beans are investigated, named to indicate the scope and how they should be injected (for the purposes of this comparative test suite):

----

    // A stateful EJB without explicit scope; 
    // Inject this into a backing bean using @EJB.`
    @Stateful
    public class StatefulEjb extends StatefulCommon {

---

    // A stateful EJB without explicit scope; 
    // Inject this into a backing bean using @Inject.
    @Stateful
    public class StatefulInject extends StatefulCommon {

----
    import javax.enterprise.context.Dependent;
    // A stateful EJB with explicit dependent pseudo-scope; 
    // Inject this into a backing bean using @Inject.
    @Stateful
    @Dependent
    public class StatefulDepend extends StatefulCommon {

----
    import javax.enterprise.context.RequestScoped;
    // A stateful EJB with explicit request scope; 
    // Inject this into a backing bean using @Inject.
    @Stateful
    @RequestScoped
    public class StatefulRequest extends StatefulCommon {

----
    import javax.faces.view.ViewScoped;
    // A stateful EJB with explicit CDI-compatible view scope; 
    // Inject this into a backing bean using @Inject.
    //
    // This is for testing purposes only; it is not suggested here that using view scope
    // with a stateful session bean is recommended (even though it still appears to work).
    @Stateful
    @ViewScoped
    public class StatefulViewView extends StatefulCommon {

----
    import org.omnifaces.cdi.ViewScoped;
    // A stateful EJB with explicit OmniFaces view scope; 
    // Inject this into a backing bean using @Inject.
    //
    // This is for testing purposes only; it is not suggested here that using view scope
    // with a stateful session bean is recommended (even though it still appears to work). 
    @Stateful
    @ViewScoped
    public class StatefulOmniView extends StatefulCommon {

----
    import javax.enterprise.context.SessionScoped;
    // A stateful EJB with explicit session scope; 
    // Inject this into a backing bean using @Inject.
    @Stateful
    @SessionScoped
    public class StatefulSession extends StatefulCommon {

----

    // A stateless EJB (without explicit scope); 
    // Inject this into a backing bean using @EJB.
    @Stateless
    public class StatelessEjb extends StatelessCommon {

----

    // A stateless EJB (without explicit scope); 
    // Inject this into a backing bean using @Inject.
    @Stateless
    public class StatelessInject extends StatelessCommon {

----

These are injected into the JSF backing beans thus:

```
@EJB //! For this test
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


@EJB //! For this test
private StatelessEjb statelessEjb;

@Inject
private StatelessInject statelessInject;
```

The `exec()` method of each session bean is also invoked on @PostConstruct of each backing bean.



#### QUICKSTART: Understanding typical output: CASE: @RequestScoped

The following shows typical server log output for a request-scoped backing bean. (In this case, unlike with view-scoped, there is no need to navigate away from the page to see the backing bean destruction.)

http://localhost:8080/EJBvsCDIvsJSF/faces/test_request.xhtml

`Info:   StatefulEjb      [1516008751003]: postConstruct`
`Info:   StatefulInject   [1516008751005]: postConstruct`
`Info:   StatefulDepend   [1516008751006]: postConstruct`
`Info:   Jsf23RequestBean [1516008751003]: postConstruct`
`Info:   StatefulEjb      [1516008751003]: exec`
`Info:   StatefulInject   [1516008751005]: exec`
`Info:   StatefulDepend   [1516008751006]: exec`
`Info:   StatefulRequest  [1516008751009]: postConstruct`
`Info:   StatefulRequest  [1516008751009]: exec`
`Info:   StatefulViewView [1516008751011]: postConstruct`
`Info:   StatefulViewView [1516008751011]: exec`
`Info:   StatefulOmniView [1516008751014]: postConstruct`
`Info:   StatefulOmniView [1516008751014]: exec`
`Info:   StatefulSession  [1516008112530]: exec`
`Info:   StatelessEjb     [1516008112536]: exec`
`Info:   StatelessEjb     [1516008112536]: pseudoState=11 [Shows has been invoked many times via pool]`
`Info:   StatelessInject  [1516008112539]: exec`
`Info:   StatelessInject  [1516008112539]: pseudoState=11 [Shows has been invoked many times via pool]`
`Info:   Jsf23RequestBean [1516008751003]: preDestroy`
`Info:   Jsf23RequestBean [1516008751003]: SKIP: DO NOT force remove() on @EJB statefulEjb !`
`Info:   Jsf23RequestBean [1516008751003]: SKIP: DO NOT force remove() on @Inject statefulInject !`
`Info:   Jsf23RequestBean [1516008751003]: SKIP: DO NOT force remove() on @Inject statefulDepend !`
`Info:   Jsf23RequestBean [1516008751003]: SKIP: DO NOT force remove() on @Inject statefulRequest !`
`Info:   Jsf23RequestBean [1516008751003]: SKIP: DO NOT force remove() on @Inject statefulViewView !`
`Info:   Jsf23RequestBean [1516008751003]: SKIP: DO NOT force remove() on @Inject statefulOmniView !`
`Info:   Jsf23RequestBean [1516008751003]: SKIP: DO NOT force remove() on @Inject statefulSession !`
`Info:   StatefulInject   [1516008751005]: preDestroy`
`Info:   StatefulDepend   [1516008751006]: preDestroy`
`Info:   StatefulRequest  [1516008751009]: preDestroy`

Note that:

- Each bean has an identifier, which is the `Date.time()` milliseconds at the moment of construction; this approach makes it very easy to track each bean and see whether and when it is destroyed (and is independent of proxies).
- On construction of a backing bean, exec() is invoked on every injected EJB.
- Stateless EJBs have a `pseudoState`, the number of times `exec()` was invoked, which enables one to see whether the same stateless bean is being offered back via the pool on separate invocations.
- You can choose whether to explicitly invoke `remove()` on stateful session beans injected via `@EJB` and/or on stateful session beans injected via `@Inject`. The logged info makes it clear that this has been SKIPPED and NOT done here. (See also section below about option switches.)

In the particular run above:

- The @PostConstruct was not invoked on the `@Stateless` session beans, presumably because they were already created earlier (during a separate page load) and are merely being served back from the pool.
- Likewise, @PostConstruct was not invoked on the `@SessionScoped ` `@Stateless` session bean, which it seems was already created earlier during the test run session.
- The @PreDestroy was automatically invoked on those `@Stateful` session beans <u>that were injected using `@Inject`</u> AND have lower or matching scope w.r.t. `@RequestScoped`, **but was NOT invoked (yet) for the @Stateful EJB <u>that was injected using @EJB</u>**.
- The @PreDestroy was also not invoked (yet) for those `@Stateful` session beans that were injected using `@Inject` but had the higher scopes `@ViewScoped`, `@SessionScoped`.




#### DIAGNOSING EJB RELEASE BY JSF BEAN SCOPE AND/OR IMPLEMENTATION

The initial web page is an index to 4 test cases, one for each of the JSF backing bean forms below:

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



#### SUMMARY OF TYPICAL RESULTS

See the `/results` folder for some simple annotated text files that show some results for typical backing bean cases, and for each of the session bean types, under @Inject and @EJB.



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

You can choose how the backing beans handle their injected session beans when `@PreDestroy` is called on a backing bean. These options are accessible from the home `index.xhtml` page and the target `done.xhtml` page:

- On `@PreDestroy` of backing beans, force `remove()` `@Stateful` beans injected with `@EJB` ? 
- On `@PreDestroy` of backing beans, force `remove()` `@Stateful` beans injected with `@Inject` ? 


By default these are both initially OFF so you can see the intended behaviour of the container.

Note also, that under CDI 1.2, you are not supposed to explicitly invoke remove():

> ***3.2.1. EJB remove methods of session beans***
> 
> 'If a session bean is a stateful session bean:
>
> - If the scope is @Dependent, the application may call any EJB remove method of a contextual instance of the session bean.
> - Otherwise, the application may not directly call any EJB remove method of any contextual instance of the session bean.
>
> The session bean is not required to have an EJB remove method in order for the container to destroy it.
>
> If the application directly calls an EJB remove method of a contextual instance of a session bean that is a stateful session bean and declares any scope other than `@Dependent`, an `UnsupportedOperationException` is thrown.'

Therefore the 2nd option, for use with @Inject, should not usually  be turned ON anyway.

#### SOME CONCLUSIONS

For `@Stateless` session beans, there is no observable difference (at least for the cases covered by this test web app) whether one injects with `@Inject` or `@EJB` (which is consistent with the idea that `@Stateless` session beans are not contextual).

For `@Stateful` session beans, there is a big difference whether one injects with `@Inject` or `@EJB`, and for CDI `@Inject` injection, it matters crucially whether one declares an explicit scope or not:

- If you inject a `@Stateful` session bean with `@EJB` <u>you MUST invoke `remove()` on it at some stage yourself in the application</u> in order for its `@PreDestroy` (if any) to be invoked.
- If you inject a `@Stateful` session bean with CDI `@Inject` you "should" either declare an explicit scope and understand how that impacts on the lifecycle of the stateful bean, or understand the implications of the default `@Dependent` pseudo-scope.

Finally, the test case includes and compares both Mojarra `@ViewScoped` and OmniFaces `@ViewScoped` in session beans; although these are not "official" scopes mentioned in the CDI 1.2 spec, they seem to work well in combination with matching `@ViewScoped` JSF backing beans.


#### FURTHER READING

- https://stackoverflow.com/questions/8887140/jsf-request-scoped-bean-keeps-recreating-new-stateful-session-beans-on-every-req

- https://docs.jboss.org/cdi/learn/userguide/CDI-user-guide.html#_session_beans

- Contexts and Dependency Injection for the Java EE platform (CDI 1.2 [JSR 346 Maintenance](https://jcp.org/en/jsr/detail?id=346))

- https://docs.jboss.org/cdi/spec/1.2/cdi-spec-1.2.pdf

  ​

Hoping this helps other Enterprise Java enthusiasts with this subtle and important manner,

Darren Kelly (Webel IT Australia)
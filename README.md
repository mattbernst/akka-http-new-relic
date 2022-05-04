# AkkaHttp IllegalAccessError with New Relic agent

## Context

This is a small project to reproduce problems like those reported on New Relic's Java agent issue 620, [Exception in thread "main" java.lang.IllegalAccessError: Update to non-static final field](https://github.com/newrelic/newrelic-java-agent/issues/620).

## How to reproduce the error

- Install [sbt](https://www.scala-sbt.org/1.x/docs/Setup.html), the Scala Build Tool, if you do not already have it installed. 
- Download [newrelic-agent-7.6.0.jar](https://repo1.maven.org/maven2/com/newrelic/agent/java/newrelic-agent/7.6.0/newrelic-agent-7.6.0.jar) into the top level of this project directory.
- Export environment variables:

```
export JAVA_OPTS=-javaagent:./newrelic-agent-7.6.0.jar
export NEW_RELIC_APP_NAME=akkatest
export NEW_RELIC_LICENSE_KEY=<a valid license key>
```

- Confirm that an appropriate version of Java is running. This problem occurs with Java 11 and 17. (I have only tested LTS versions.) I use [SDKMAN!](https://sdkman.io/) to easily switch between different Java versions.

```
~/akka-http-new-relic % java -version
openjdk version "11.0.11" 2021-04-20
OpenJDK Runtime Environment AdoptOpenJDK-11.0.11+9 (build 11.0.11+9)
OpenJDK 64-Bit Server VM AdoptOpenJDK-11.0.11+9 (build 11.0.11+9, mixed mode)
```

Run `sbt test` and observe the `IllegalAccessError`:

```
~/akka-http-new-relic % sbt test
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.newrelic.weave.weavepackage.NewClassAppender (file:/Users/mernst/akka-http-new-relic/newrelic-agent-7.6.0.jar) to method java.net.URLClassLoader.addURL(java.net.URL)
WARNING: Please consider reporting this to the maintainers of com.newrelic.weave.weavepackage.NewClassAppender
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
2022-05-03T16:56:12,960-0700 [50511 1] com.newrelic INFO: Configuration file not found. The agent will attempt to read required values from environment variables.
2022-05-03T16:56:12,975-0700 [50511 1] com.newrelic INFO: Using default collector host: collector.newrelic.com
2022-05-03T16:56:12,976-0700 [50511 1] com.newrelic INFO: Using default metric ingest URI: https://metric-api.newrelic.com/metric/v1
2022-05-03T16:56:12,976-0700 [50511 1] com.newrelic INFO: Using default event ingest URI: https://insights-collector.newrelic.com/v1/accounts/events
2022-05-03T16:56:13,019-0700 [50511 1] com.newrelic INFO: New Relic Agent: Writing to log file: /Users/mernst/akka-http-new-relic/logs/newrelic_agent.log
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.newrelic.weave.weavepackage.NewClassAppender (file:/Users/mernst/akka-http-new-relic/newrelic-agent-7.6.0.jar) to method java.net.URLClassLoader.addURL(java.net.URL)
WARNING: Please consider reporting this to the maintainers of com.newrelic.weave.weavepackage.NewClassAppender
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
[info] welcome to sbt 1.6.2 (AdoptOpenJDK Java 11.0.11)
[info] loading settings for project global-plugins from credentials.sbt,plugins.sbt ...
[info] loading global plugins from /Users/mernst/.sbt/1.0/plugins
[info] loading project definition from /Users/mernst/akka-http-new-relic/project
[info] loading settings for project root from build.sbt ...
[info] set current project to akka-http-new-relic (in build file:/Users/mernst/akka-http-new-relic/)
[info] VersionResourceSpec:
[info] versionRoute
[info] web.VersionResourceSpec *** ABORTED ***
[info]   java.lang.IllegalAccessError: Update to non-static final field akka.http.scaladsl.server.Directives$.DoubleNumber attempted from a different method (akka$http$scaladsl$server$PathMatchers$_setter_$DoubleNumber_$eq) than the initializer method <init>
[info]   at akka.http.scaladsl.server.Directives$.akka$http$scaladsl$server$PathMatchers$_setter_$DoubleNumber_$eq(Directives.scala:46)
[info]   at akka.http.scaladsl.server.PathMatchers.$init$(PathMatcher.scala:478)
[info]   at akka.http.scaladsl.server.Directives$.<init>(Directives.scala:46)
[info]   at akka.http.scaladsl.server.Directives$.<clinit>(Directives.scala)
[info]   at web.VersionResource.versionRoute(VersionResource.scala:10)
[info]   at web.VersionResource.versionRoute$(VersionResource.scala:10)
[info]   at web.VersionResourceSpec.versionRoute$lzycompute(VersionResourceSpec.scala:7)
[info]   at web.VersionResourceSpec.versionRoute(VersionResourceSpec.scala:7)
[info]   at web.VersionResourceSpec.$anonfun$new$1(VersionResourceSpec.scala:14)
[info]   at org.scalatest.OutcomeOf.outcomeOf(OutcomeOf.scala:85)
[info]   ...
[error] Test suite web.VersionResourceSpec failed with java.lang.IllegalAccessError: Update to non-static final field akka.http.scaladsl.server.Directives$.DoubleNumber attempted from a different method (akka$http$scaladsl$server$PathMatchers$_setter_$DoubleNumber_$eq) than the initializer method <init> .
[error] This may be due to the ClassLoaderLayeringStrategy (ScalaLibrary) used by your task.
[error] To improve performance and reduce memory, sbt attempts to cache the class loaders used to load the project dependencies.
[error] The project class files are loaded in a separate class loader that is created for each test run.
[error] The test class loader accesses the project dependency classes using the cached project dependency classloader.
[error] With this approach, class loading may fail under the following conditions:
[error] 
[error]  * Dependencies use reflection to access classes in your project's classpath.
[error]    Java serialization/deserialization may cause this.
[error]  * An open package is accessed across layers. If the project's classes access or extend
[error]    jvm package private classes defined in a project dependency, it may cause an IllegalAccessError
[error]    because the jvm enforces package private at the classloader level.
[error] 
[error] These issues, along with others that were not enumerated above, may be resolved by changing the class loader layering strategy.
[error] The Flat and ScalaLibrary strategies bundle the full project classpath in the same class loader.
[error] To use one of these strategies, set the  ClassLoaderLayeringStrategy key
[error] in your configuration, for example:
[error] 
[error] set root / Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.ScalaLibrary
[error] set root / Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat
[error] 
[error] See ClassLoaderLayeringStrategy.scala for the full list of options.
[info] Run completed in 1 second, 657 milliseconds.
[info] Total number of tests run: 0
[info] Suites: completed 0, aborted 1
[info] Tests: succeeded 0, failed 0, canceled 0, ignored 0, pending 0
[info] *** 1 SUITE ABORTED ***
[error] Error during tests:
[error] 	web.VersionResourceSpec
[error] (Test / test) sbt.TestsFailedException: Tests unsuccessful
[error] Total time: 4 s, completed May 3, 2022, 4:56:23 PM
```

## Additional notes

- This failure *does not* happen if the agent is inactive (e.g. no license key or app name set up, so agent does not start.)
- This failure *does not* happen with Java 8.
- This failure *does not* happen with agent version 7.1.1, even on Java 11+. Versions 7.2.0 and later *do* fail.
- This failure *does not* happen with Scala 2.13. It *does* happen with Scala 2.12 and 2.11 (not that I expect New Relic to support the long-unmaintained 2.11).
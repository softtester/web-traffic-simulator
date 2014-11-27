[![Jenkins](https://travis-ci.org/web-traffic-simulator/web-traffic-simulator.svg)](https://travis-ci.org/web-traffic-simulator/web-traffic-simulator)

web-traffic-simulator
=====================
Test framework to simulate traffic on a homepage and collect log data.

#Install
```

We use vagrant to setup our development environment.
More info about this can be found in /vagrant in this repository.

When you have setup the development environment and cloned this repository you build the project by:

gradle wrapper
gradlew build
```
To generate Eclipse projects, do:
```
gradlew eclipse
```

From now on, only use gradlew.

Import eclipse_formatter.xml and eclipse_codestyle.xml. Use save actions in Eclipse!

#Problems and solutions
Some problems and solutions.

## JAVA_HOME not found on Ubuntu
When you run
```
gradle wrapper
```
You may get
```
ERROR: JAVA_HOME is set to an invalid directory: /usr/lib/jvm/default-java
```
Solution is found here: http://stackoverflow.com/questions/22307516/gradle-finds-wrong-java-home-even-though-its-correctly-set

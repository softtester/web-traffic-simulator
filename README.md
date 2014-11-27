web-traffic-simulator
=====================
Test framework to simulate traffic on a homepage and collect log data.

#Install
sudo apt-get install gradle
gradle wrapper
gradlew build

To generate Eclipse projects, do: gradlew eclipse

From now on, only use gradlew.

Import eclipse_formatter.xml and eclipse_codestyle.xml. Use save actions in Eclipse!

#Problems and solutions
When you run
gradle wrapper
You may get
ERROR: JAVA_HOME is set to an invalid directory: /usr/lib/jvm/default-java
Solution is found here
http://stackoverflow.com/questions/22307516/gradle-finds-wrong-java-home-even-though-its-correctly-set

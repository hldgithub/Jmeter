DESCRIPTION
===========
This project analyzes metrics produced by apache Jmeter and creates a 
management summary report.

FILES
=====
src/
	java applications which generate statistic from jmeter result
JmeterReport.jar
	the jar created by JmeterReport directory
build.xml
	ant build file controlling the process, used in windows environment
build.xml.unix
	ant build file controlling the process, used in unix environment	
datasets/
	input data for jmeter
jmeter.jmx
	jmeter general configurations
properties/
	configuration files for different environment
result/
	output directory
jmeter-cronscript.sh/
	run the application and make the result available

HOW TO RUN
==========
First you must build the JmeterReport jar by executing the following commands:
  
  >cd Jmeter
  >ant

After it builds...
  
  >cd ..

The Jmeter performance test can be executed by using the following command:

  ant -propertyfile properties/test.properties -f build.xml.unix -Djmeter.props=properties/jmeter-test.properties -DrunName="Performance Test"

Explanation of parameters:
    (-propertyfile properties/test.properties)         - Specifies the property file that contains the environment specific settings
                                                            as well as Jmeter properties like (number of users, ramp up time, etc).
    (-f build.xml.unix) -                              - Explicitly execute the build.xml.unix ant build file
    (-Djmeter.props=properties/jmeter-test.properties) - specifies the user-specific properties such as file paths
    (-DrunName="Performance Test")                     - Names the Test, any spaces in the text should be enclosed in parenthesis                                      

In unix, a shell script has been set up that handles the ant execution as well as 
the direction of the output.
  
  ./jmeter-cronscript.sh <user property name> <environment>
  
  example...
  
  ./jmeter-cronscript.sh test test

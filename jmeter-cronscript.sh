#!/bin/sh

date

START_DIR=/bin/jakarta-jmeter-2.4/bin

export HOME=/
export JAVA_HOME=/usr/java/jdk1.6.0_06
export ANT_HOME=/usr/apache/apache-ant-1.7.0
export MAVEN_HOME=/sq/maven/apache-maven-2.0.9
export MAVEN_OPTS="-Xmx1024m -Xms128m -XX:MaxPermSize=256m"
export M2_HOME=/sq/maven/apache-maven-2.0.9
export M2=${M2_HOME}/bin
export TOMCAT_HOME=~/apache-tomcat-6.0.26

export PATH=${PATH}:/sq/svn_1.6.1/x86_64/bin:${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${ANT_HOME}/bin:${TOMCAT_HOME}/bin:${HOME}/bin/jakarta-jmeter-2.4/bin

export CATALINA_OPTS="-javaagent:${TOMCAT_HOME}/lib/spring-agent-2.5.6.jar -Xms1024m -Xmx2048m -XX:PermSize=64m -XX:MaxPermSize=256m"

umask 000

DATE=`date +%Y%m%d`
TIME=`date +%H%M`
EMAIL_DATE=`date +%m-%d-%Y`
DIRECTORY_DATE=`date +%Y-%m-%d`
EMAIL_LIST="test@test.com"

##Update code from svn
cd ${HOME}/code/Jmeter
/sq/svn_1.6.1/x86_64/bin/svn update

##Build the JmeterReport jar
cd ${HOME}/code/Jmeter/src/JmeterReport
/usr/apache/apache-ant-1.7.0/bin/ant

##Execute the jmeter performance test
cd ${HOME}/code/Jmeter
/usr/apache/apache-ant-1.7.0/bin/ant -propertyfile properties/jmeter.properties -f build.xml.unix -Djmeter.props=properties/jmeter.properties -DrunName="Performance Test"

##Email the link to the results
resultDir=`ls -dt /home/public_html/${DIRECTORY_DATE}* | head -1`
if [[ ${resultDir} = '' ]]; then
  echo "Performance Test FAILED" | mailx -s "$EMAIL_DATE Performance Test FAILED" $EMAIL_LIST 
else
  resultFile=`ls ${resultDir}/results.html`

  if [[ ${resultFile} = '' ]]; then

    echo "Performance Test FAILED" | mailx -s "${EMAIL_DATE} Performance Test FAILED" ${EMAIL_LIST}

  else

    resultDirName=`echo ${resultDir} | cut -c24-`

    finalFile=/home/public_html/perf_test_results/${resultDirName}_results.html

    cp ${resultFile} ${finalFile}

    echo "http://perf_test_results/${resultDirName}_results.html" | mailx -s "${EMAIL_DATE} Performance Test Results" ${EMAIL_LIST}

  fi

fi


#remove directories older than 60 days

find /home/public_html -maxdepth 1 -mindepth 1 -type d -mtime +60 \( ! -iname "perf_test_results" \) -exec rm -rf '{}' \;


#remove reports older than 6 months

find /home/public_html/perf_test_results -type f -mtime +180 -exec rm -rf '{}' \;

cd ${START_DIR}

date


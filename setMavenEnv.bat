@echo off

set MAVEN_HOME=%CD%\build-tools\apache-maven-3.3.9
set PATH=%MAVEN_HOME%\bin;%PATH%

echo Setting Maven home to: %MAVEN_HOME%

mvn -version


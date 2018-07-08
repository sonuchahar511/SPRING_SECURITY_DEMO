@echo off

set ANT_OPTS=-Xmx200m -XX:MaxPermSize=128M
set ANT_HOME=build-tools\apache-ant-1.9.1
set PATH=%ANT_HOME%\bin;%PATH%

echo Setting ant home to: %ANT_HOME%

ant -version


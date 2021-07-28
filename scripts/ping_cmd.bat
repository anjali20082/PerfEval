@ECHO OFF
set host=%1
set PINGINTERVAL=5
echo Pinging %host% > %host%.log
:PINGINTERVAL
echo Date:%date% %time% >> %host%.log
adb shell ping -c 1 %host% >> %host%.log
timeout %PINGINTERVAL%
GOTO PINGINTERVAL
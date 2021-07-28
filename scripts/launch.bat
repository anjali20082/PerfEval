@echo off

rem FOR /F "tokens=5 delims= " %%P IN ('netstat -a -n -o ^| findstr :5037.*LISTENING') DO TaskKill.exe /F /PID %%P
rem FOR /F "tokens=5 delims= " %%P IN ('netstat -a -n -o ^| findstr :4723.*LISTENING') DO TaskKill.exe /F /PID %%P

adb devices
start /min appium
start /min ping_cmd.bat www.google.com
start /min ping_cmd.bat www.amazon.com
start /min ping_cmd.bat www.mobikwik.com

set app=gm

FOR /l %%I IN (0, 1, 20) DO (
  echo Iteration:%%I
  echo %app% %version%

  del /f www.google.com.log,www.amazon.com.log,www.mobikwik.com.log

  set checkfb=fb
  set Replaced=%%app:%checkfb%=%%

  If NOT "%app%"=="%Replaced%" (
    echo Clearing Fb
    adb shell pm clear com.facebook.katana
  ) else (
    echo Not clearing fb
  )

  @REM set check=yt
  @REM set Replaced=%%app:%check%=%%

  @REM If NOT "%app%"=="%Replaced%" (
  @REM   echo Clearing Youtube
  @REM   adb shell pm clear com.google.android.youtube
  @REM ) else (
  @REM   echo Not clearing youtube
  @REM )

  set check=gm
  set Replaced=%%app:%check%=%%

  If NOT "%app%"=="%Replaced%" (
    echo Clearing Gmaps
    adb shell pm clear com.google.android.apps.maps
  ) else (
    echo Not clearing  gm
  )

  @REM set check=gn
  @REM set Replaced=%%app:%check%=%%

  @REM If NOT "%app%"=="%Replaced%" (
  @REM   echo Clearing GNews
  @REM   adb shell pm clear com.google.android.apps.magazines
  @REM ) else (
  @REM   echo Not clearing gn
  @REM )

  @REM set check=tg
  @REM set Replaced=%%app:%check%=%%

  @REM If NOT "%app%"=="%Replaced%" (
  @REM   echo Clearing Telegram
  @REM   adb shell pm clear org.telegram.messenger
  @REM ) else (
  @REM   echo Not clearing tg
  @REM )

  @REM set check=fl
  @REM set Replaced=%%app:%check%=%%

  @REM If NOT "%app%"=="%Replaced%" (
  @REM   echo Clearing Flipkart
  @REM   adb shell pm clear com.flipkart.android
  @REM ) else (
  @REM   echo Not clearing fl
  @REM )

  java -jar EvalApp.jar 8 3 "%app%"
  timeout 60
)


@REM {"_id":{"$oid":"605b48d2a26a313d45bd105b"},"uploadedAt":"2021/03/24 19:42:34",
@REM "data_before":"TELEGRAM:252507294:12162725:0:0
@REM WHATSAPP:3029477037:10503154593:2:0
@REM PAYTM:21673215:3974668:0:0
@REM GOOGLE NEWS:24464053:2522982:0:0
@REM YOUTUBE:52655556009:1932756545:1:0
@REM MOBIKWIK:5778288:7332204:0:0
@REM AMAZON:150294761:51839628:0:0
@REM HOTSTAR:29266754:2455433:3:0
@REM MAPS:369715580:17109206:0:0
@REM FLIPKART:37916185:3546149:0:0
@REM LINKEDIN:50669030:10720088:0:0
@REM FACEBOOK:197388551:22391493:0:0
@REM DAILYHUNT:adb:4387929:0:0
@REM ","data_after":"TELEGRAM:252507294:12162725:0:0
@REM WHATSAPP:3029477037:10503154593:2:0
@REM PAYTM:21673215:3974668:0:0
@REM GOOGLE NEWS:24464053:2522982:0:0
@REM YOUTUBE:52655556009:1932756545:2:0
@REM MOBIKWIK:5778288:7332204:0:0
@REM AMAZON:150294761:51839628:0:0
@REM HOTSTAR:29266754:2455433:4:0
@REM MAPS:369715580:17109206:0:0
@REM FLIPKART:37916185:3546149:0:0
@REM LINKEDIN:50669030:10720088:0:0
@REM FACEBOOK:197388551:22391493:0:0
@REM DAILYHUNT:18341580:4387929:0:0
@REM "}
@echo off

set version=%1

adb shell settings put global verifier_verify_adb_installs 0

@REM echo "GNews:%version%"
@REM adb uninstall com.google.android.apps.magazines
@REM adb install -r GNews/%version%.apk

@REM echo "GMaps:%version%"
@REM adb uninstall com.google.android.apps.maps
@REM adb install -r Gmaps/%version%.apk

echo "Youtube:%version%"
adb uninstall com.google.android.youtube
adb install -r Youtube/%version%.apk

@REM echo "Flipkart:%version%"
@REM adb uninstall com.flipkart.android
@REM adb install Flipkart/%version%.apk

echo "Telegram:%version%"
adb uninstall org.telegram.messenger
adb install Telegram/%version%.apk

@REM echo "Facebook:%version%"
@REM adb uninstall com.facebook.katana
@REM adb install Facebook/%version%.apk
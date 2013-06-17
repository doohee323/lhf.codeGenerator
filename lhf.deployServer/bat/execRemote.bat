rem @echo off

rem ###########################################################
rem # Batch Script
rem # usage : execRemote.bat execRemote all
rem ###########################################################

set arg1=%1
set arg2=%2

if "%arg1%" equ "" (set arg1="execRemote")
if "%arg2%" equ "" (set arg2="all")
echo %arg1% %arg2%

set BASE_DIR=%cd%
set HOME_DIR=C:\LHF_IDE\workspace\lhf.deployServer

set NODE_HOME="C:\Program Files\nodejs"
set NODE_SHELL=node
set MAIN_CLASS=LaunchCmd.js
set WORK_DIR=%HOME_DIR%\src\lhf.launcher\cmd

set PATH=%PATH%;%NODE_HOME%\bin

rem ======= working dir =================
cd %WORK_DIR%

rem ======= =================
rem echo %NODE_SHELL% %MAIN_CLASS% %arg1% %arg2% %3 %4 %5
%NODE_SHELL% %MAIN_CLASS% %arg1% %arg2% %3 %4 %5

cd "%BASE_DIR%"

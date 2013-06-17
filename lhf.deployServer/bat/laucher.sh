#!/bin/sh

############################################################
# Batch Script
# usage : laucher.sh linux deploy / startup / shutdown
############################################################

export arg1=$1
export arg2=$2

if [ "$arg1" = "" ]; then
	arg1='linux' 
fi
if [ "$arg2" = "" ]; then
	arg2='deploy' 
fi
echo $arg1 $arg2

export LANG=ko_KR.UTF-8
export BASE_DIR=`pwd`
export HOME_DIR=/DATA2/lhf.deployServer

export NODE_HOME=/DATA1/groovy-2.1.3
export NODE_SHELL=node
export MAIN_CLASS=LaunchCmd.js
export WORK_DIR=$HOME_DIR/src/lhf.launcher/cmd

export PATH=$PATH:$NODE_HOME/bin

#======= working dir =================
cd $WORK_DIR

#======= =================
echo $NODE_SHELL $MAIN_CLASS $arg1 $arg2 $3 $4 $5
$NODE_SHELL $MAIN_CLASS $arg1 $arg2 $3 $4 $5

cd $BASE_DIR

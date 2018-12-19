#!/bin/bash

SRC=sources.txt
CP=cp.txt

function Clear() {
	if [ -f $SRC ] ; then
		rm $SRC
	fi

	if [ -f $CP ] ; then
		rm $CP
	fi
}

cp ./UFileSDK/libs/* ./UFileSDKTest/libs/

cd ./UFileSDKTest/src
#find all source files
find . -name '*.java' > $SRC

#find external jars
LIBS=../../UFileSDKTest/libs
find $LIBS -name '*.jar' | tr '\n' ':' > $CP

BIN=../../UFileSDKTest/bin/
mkdir -p $BIN
javac -encoding UTF8 -d $BIN -classpath @$CP @$SRC

Clear

#cp config.properties $BIN

#cd $BIN

#jar -cvf ../../ufile_sdk_test.jar ./*

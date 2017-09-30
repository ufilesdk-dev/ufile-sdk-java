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

cd ./UFileSDK/src
#find all source files
find . -name '*.java' > $SRC

#find external jars
LIBS=../../UFileSDK/libs/
find $LIBS -name '*.jar' | tr '\n' ':' > $CP

BIN=../../UFileSDK/bin/
mkdir -p $BIN
javac -encoding UTF-8 -d $BIN -classpath @$CP @$SRC

cp mime_type.list $BIN

Clear

cd $BIN
jar -cvf ../../UFileSDKTest/libs/ufile_sdk.jar ./*

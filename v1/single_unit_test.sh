
#打包成jar 指定jar中的Main-Class 以及依赖的第三方jar包
#注意相对路径
#cd ./UFileSDKTest/bin/  &&  java -cp ../../ufile_sdk_test.jar  -Djava.ext.dirs=../libs cn/ucloud/ufile/sdk/test/UFilePutTest && cd -


cd ./UFileSDKTest/bin

CLASSPATH=../libs/httpcore-4.1.4.jar:../libs/httpmime-4.3.6.jar:../libs/gson-2.3.1.jar:../libs/commons-logging-1.1.1.jar:../libs/junit-4.0.jar:../libs/commons-codec-1.4.jar:../libs/httpclient-4.1.3.jar:../libs/ufile_sdk.jar:.


#PutFile 
#java -cp $CLASSPATH cn/ucloud/ufile/sdk/test/UFilePutTest

#java -cp $CLASSPATH cn/ucloud/ufile/sdk/test/UFilePostTest

#java -cp $CLASSPATH cn/ucloud/ufile/sdk/test/UFileMultiUploadTest

#UploadHit
#java -cp $CLASSPATH cn/ucloud/ufile/sdk/test/UFileUploadHitTest

# DownloadUrl
java -cp $CLASSPATH cn/ucloud/ufile/sdk/test/UFileDownloadUrlTest


# PutFileSteam 
#java -cp $CLASSPATH cn/ucloud/ufile/sdk/stream/test/UFilePutFileStreamTest


# PutStringSteam 
#java -cp $CLASSPATH cn/ucloud/ufile/sdk/stream/test/UFilePutStringStreamTest

#java -cp $CLASSPATH cn/ucloud/ufile/sdk/test/UFileMultiThreadTest

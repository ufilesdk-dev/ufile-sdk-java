# UFile Java SDK Ver-1.0.0

## 当前1.0版本已不再维护，如需使用Ufile SDK for Java，建议您移步master分支的版本
**[Latest Version](https://github.com/ufilesdk-dev/ufile-javasdk)**

> 特别说明： UFile对短连接的支持较好，完成一个请求，务必关闭连接后，再进行下一个请求。

## 1. 目录说明

- 文件夹v1下面有两个基于Eclipse开发的项目目录
    > ./v1/UFileSDK
    > ./v1/UFileSDKTest

- ./v1/UFileSDK 
    SDK的源码目录，可将此目录打包成jar用于其他项目。
UFileSDK依赖的jar都在./v1/UFileSDK/libs目录下：
    > ./v1/UFileSDK/libs/commons-codec-1.4.jar 
    > ./v1/UFileSDK/libs/commons-logging-1.1.1.jar 
    > ./v1/UFileSDK/libs/httpclient-4.1.3.jar
    > ./v1/UFileSDK/libs/httpcore-4.1.4.jar
    > ./v1/UFileSDK/libs/httpmime-4.3.6.jar
    > ./v1/UFileSDK/libs/junit-4.0.jar
    > ./v1/UFileSDK/libs/gson-2.3.1.jar

- ./v1/UFileSDKTest 
    > ./v1/UFileSDKTest/cn/ucloud/ufile/sdk/test 
        是使用UFileSDK的demo
    > ./v1/UFileSDKTest/cn/ucloud/ufile/sdk/stream/test
        是使用UFileSDK并通过PutFile接口以流的形式（文件流、字节流）上传文件的demo
   

## 2. 配置文件
- 配置文件示例
    > ./v1/UFileSDK/src/config.properties

- 配置文件中有四个参数
    > UCloudPublicKey=your_ucloud_public_key
    > UCloudPrivateKey=your_ucloud_private_key
    > ProxySuffix=.ufile.ucloud.cn
    > DownloadProxySuffix=.ufile.ucloud.cn

- 说明：
    - UCloudPublicKey 请改成用户的公钥
    - UCloudPrivateKey 请改成用户的私钥
    - ProxySuffix 指定上传域名的后缀，可以填写源站的后缀(例如北京地域 .cn-bj.ufileos.com)或内网域名的后缀
    - DownloadProxySuffix 指定下载域名的后缀，可以填写源站(例如北京地域 .cn-bj.ufileos.com)或加速域名的后缀 (.ufile.ucloud.com.cn)

**发送请求前，须加载配置项或者逐个设置配置项**
**具体示例请参考v1/UFileSDKTest/src/cn/ucloud/ufile/sdk/test/UFilePutTest.java**

## 3. Eclipse使用说明
使用Eclipse导入项目UFileSDK和UFileSDKTest，若出现Java版本兼容问题等时，重新编译即可

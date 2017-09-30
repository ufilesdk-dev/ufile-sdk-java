===========================
===Version 1.0.0===
 * 支持普通上传 Put File
 * 支持下载文件 Get File
 * 支持删除文件 Delete File
 * 支持分片上传 MultipartUpload File
 * 支持表单上传 Post File
 * 支持秒速上传 Upload Hit File


===Version 1.0.1===
 * 根据文件名后缀识别文件的MIME
 * 生成访问私有Bucket的URL


===Version 1.0.2===
 * 若客户端未指定，SDK填写上传文件的默认MIME

===Version 1.0.3===
 * PutFile接口支持上传数据流（文件流和字节流）

===Version 1.0.4===
 * 下载和上传的url前缀区分开
 * 用户可以自由指定配置文件的路径

===Version 1.0.5===
 * 全部请求改成短连接
 * 增加并发put上传的Demo

===Version 1.0.6===
 * 并发分片上传时, 按照分片的编号顺序计算ETag

===Version 1.0.7===
 * 支持查询文件 Head File

===Version 1.0.8===
 * 修复分片上传超过2G的文件过程中文件偏移量出现负值的bug

===Version 1.0.9===
 * 对url的path和query部分进行encode

===Version 1.0.10===
 * POST接口上传可以指定中文的key

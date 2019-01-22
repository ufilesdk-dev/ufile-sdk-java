package cn.ucloud.ufile.sdk.test;

import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileConfig;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import cn.ucloud.ufile.body.FinishMultiBody;
import cn.ucloud.ufile.body.InitMultiBody;
import cn.ucloud.ufile.body.PartBody;
import cn.ucloud.ufile.sender.FinishMultiSender;
import cn.ucloud.ufile.sender.InitiateMultiSender;
import cn.ucloud.ufile.sender.UploadPartSender;
import com.google.gson.Gson;
import org.apache.http.Header;

import java.io.*;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 分片上传测试，包括初始化分片上传、多线程并发上传每个分片、完成分片上传
 *
 * @author york
 */
public class UFileMultiUpload {
    public static final int CONCURRENT_COUNT = 3;
    public static final String bucketName = "lapd";
    public static final String baseurl = "http://douxiang.ufile.ucloud.com.cn/";

    private static int calPartCount(String filePath, int partSize) {
        File file = new File(filePath);
        int partCount = (int) (file.length() / partSize);
        if (file.length() % partSize != 0) {
            partCount++;
        }
        return partCount;
    }

    private static UFileConfig uFileConfig = null;

    public static void multiUpload(UFileRequest request) {
        request.setBucketName(bucketName);
        InitUFileConfig();
        UFileClient ufileClient = null;
        // 初始化分片请求
        InitMultiBody initMultiBody = null;
        try {
            ufileClient = new UFileClient(uFileConfig);
            initMultiBody = initiateMultiUpload(ufileClient, request);
        } finally {
            ufileClient.shutdown();
        }

        if (null == initMultiBody) {
            System.out.println("failed to initiate the multipart Upload");
            return;
        }

        // 上传各个分片
        int partSize = initMultiBody.getBlkSize();
        String uploadId = initMultiBody.getUploadId();
        int partCount = calPartCount(request.getFilePath(), partSize);

        ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT_COUNT);

        SortedMap<Integer, String> eTags = Collections
                .synchronizedSortedMap(new TreeMap<Integer, String>());

        File file = new File(request.getFilePath());
        for (long i = 0; i < partCount; i++) {
            long start = partSize * i;
            long curPartSize = partSize < file.length() - start ? partSize
                    : file.length() - start;

            pool.execute(new UploadPartRunnable(request, uploadId, (int) i, partSize
                    * i, curPartSize, eTags));
        }

        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                pool.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (eTags.size() != partCount) {
            throw new IllegalStateException("One or more parts failed");
        }

        // 完成分片上传
        String newKey = "newly" + request.getKey();
        ufileClient = null;
        try {
            ufileClient = new UFileClient(uFileConfig);
            finishMultiUpload(ufileClient, request, uploadId, eTags, newKey);
        } finally {
            ufileClient.shutdown();
        }
    }

    private static void finishMultiUpload(UFileClient ufileClient,
                                          UFileRequest request, String uploadId,
                                          SortedMap<Integer, String> eTags, String newKey) {
        FinishMultiSender sender = new FinishMultiSender(uploadId, eTags,
                newKey);
        sender.makeAuth(ufileClient, request);
        UFileResponse finishRes = sender.send(ufileClient, request);
        if (finishRes != null) {
            if (finishRes.getStatusLine().getStatusCode() != 200) {
                if (finishRes.getContent() != null) {
                    ufileClient.closeErrorResponse(finishRes);
                }
            } else {
                InputStream inputStream = finishRes.getContent();
                Reader reader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                FinishMultiBody body = gson.fromJson(reader,
                        FinishMultiBody.class);
                String bodyJson = gson.toJson(body);
                System.out.println("[bodyJson]: " + bodyJson);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static InitMultiBody initiateMultiUpload(UFileClient ufileClient,
                                                     UFileRequest request) {
        InitiateMultiSender init = new InitiateMultiSender();
        init.makeAuth(ufileClient, request);
        UFileResponse initRes = init.send(ufileClient, request);
        if (initRes != null) {
            System.out.println("status line: " + initRes.getStatusLine());
            Header[] headers = initRes.getHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println("header " + headers[i].getName() + " : "
                        + headers[i].getValue());
            }

            if (initRes.getStatusLine().getStatusCode() != 200) {
                if (initRes.getContent() != null) {
                    ufileClient.closeErrorResponse(initRes);
                }
                return null;
            } else {
                InputStream inputStream = initRes.getContent();
                Reader reader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                InitMultiBody body = gson.fromJson(reader, InitMultiBody.class);
                String bodyJson = gson.toJson(body);
                System.out.println("[bodyJson]: " + bodyJson);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return body;
            }
        }
        return null;
    }

    private static class UploadPartRunnable implements Runnable {
        private UFileRequest request;
        private String uploadId;
        private int partNumber;
        private long start;
        private long size;
        private SortedMap<Integer, String> eTags;

        UploadPartRunnable(UFileRequest request, String uploadId,
                           int partNumber, long start, long partSize,
                           SortedMap<Integer, String> eTags) {
            try {
                this.request = (UFileRequest) request.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            this.uploadId = uploadId;
            this.partNumber = partNumber;
            this.start = start;
            this.size = partSize;
            this.eTags = eTags;
        }

        @Override
        public void run() {
            UFileClient ufileClient = null;
            try {
                ufileClient = new UFileClient(uFileConfig);
                String etag;
                etag = uploadPart(ufileClient, uploadId, partNumber, start,
                        size);
                if (etag != null) {
                    eTags.put(partNumber, etag);
                }
            } finally {
                ufileClient.shutdown();
            }
        }

        private String uploadPart(UFileClient ufileClient, String uploadId,
                                  int partNumber, long start, long size) {
            UploadPartSender sender = new UploadPartSender(uploadId,
                    partNumber, start, size);
            sender.makeAuth(ufileClient, request);
            UFileResponse partRes = sender.send(ufileClient, request);

            // consume the http response body
            if (partRes != null) {
                if (partRes.getStatusLine().getStatusCode() != 200) {
                    if (partRes.getContent() != null) {
                        ufileClient.closeErrorResponse(partRes);
                    }
                    return null;
                } else {
                    InputStream inputStream = partRes.getContent();
                    Reader reader = new InputStreamReader(inputStream);
                    Gson gson = new Gson();
                    PartBody body = gson.fromJson(reader, PartBody.class);
                    String bodyJson = gson.toJson(body);
                    System.out.println("[bodyJson]: " + bodyJson);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            Header[] headers = partRes.getHeaders();
            for (int i = 0; i < headers.length; i++) {
                if ("ETag".equalsIgnoreCase(headers[i].getName())) {
                    return headers[i].getValue();
                }
            }
            ufileClient.shutdown();
            return null;
        }
    }

    private static void InitUFileConfig() {
        uFileConfig = new UFileConfig();
        uFileConfig.setUcloudPublicKey("TOKEN_e6884790-98c4-4fe7-956e-9ef3d7c3b3d9");
        uFileConfig.setUcloudPrivateKey("42197a75-5e3b-4a98-b71c-896210db5c8e");
        uFileConfig.setProxySuffix(".cn-bj.ufileos.com");
        uFileConfig.setDownloadProxySuffix(".cn-bj.ufileos.com");
    }

    public static void main(String[] args) {
        String fileUrl = "/Users/joshua/Downloads/test.jpg";
        UFileRequest request = new UFileRequest();
        request.setFilePath(fileUrl);
        request.setKey("abcdefg.jpg");
        multiUpload(request);
        if (request.getInputStream() != null) {
            try {
                request.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.gc();
        del(fileUrl);
    }

    public static void del(String file1) {
        File file = new File(file1);
        boolean delete = file.delete();
        if (delete) {
            System.out.println("删除成功");
        } else {
            System.gc();
            boolean ISdelete = file.delete();
            if (ISdelete) {
                System.out.println("2次删除成功");
            } else {
                System.out.println("2次删除失败");
            }
        }
    }
}

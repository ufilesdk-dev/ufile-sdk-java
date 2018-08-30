package sdk.test;


import org.apache.http.Header;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileConfig;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import cn.ucloud.ufile.sender.GetSender;
import cn.ucloud.ufile.sender.PostSender;

/**
 * Post上传测试
 * @author york
 *
 */
public class UFilePostTest {
	public static void main(String args[]) {
		String bucketName = "";
		String key = "";
		String filePath = "";
		String saveAsPath = "";
		String configPath = "";
		
		UFileConfig.getInstance().loadConfig(configPath);
		
		UFileRequest request = new UFileRequest();
		request.setBucketName(bucketName);
		request.setKey(key);
		request.setFilePath(filePath);
		
		UFileClient ufileClient = null;
		System.out.println("PostFile BEGIN ...");
		try {
			ufileClient = new UFileClient();
			postFile(ufileClient, request);
		} finally {
			ufileClient.shutdown();
		}
		System.out.println("PostFile END ...\n\n");
		
		System.out.println("GetFile BEGIN...");
		try {
			ufileClient = new UFileClient();
			getFile(ufileClient, request, saveAsPath);
		} finally {
			ufileClient.shutdown();
		}
		System.out.println("GetFile END ...\n\n");
	}
	
	private static void postFile(UFileClient ufileClient, UFileRequest request) {
		PostSender sender = new PostSender();
		sender.makeAuth(ufileClient, request);
		
		UFileResponse response = sender.send(ufileClient, request);
		if (response != null) {
			
			System.out.println("status line: " + response.getStatusLine());
			
			Header[] headers = response.getHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println("header " + headers[i].getName() + " : " + headers[i].getValue());
			}
			
			System.out.println("body length: " + response.getContentLength());
			
			InputStream inputStream = response.getContent();
			if (inputStream != null) {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String s = "";
					while ((s = reader.readLine()) != null) {
						System.out.println(s);
					}		
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
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
	}
	
	private static void getFile(UFileClient ufileClient, UFileRequest request, String saveAsPath) {
		GetSender sender = new GetSender();
		sender.makeAuth(ufileClient, request);
		
		UFileResponse response = sender.send(ufileClient, request);

		if (response != null) {
			
			System.out.println("status line: " + response.getStatusLine());
		
			Header[] headers = response.getHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println("header " + headers[i].getName() + " : " + headers[i].getValue());
			}
		
			System.out.println("body length: " + response.getContentLength());
			
			//handler error response 
			if (response.getStatusLine().getStatusCode() != 200 && response.getContent() != null) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(response.getContent()));
					String input;
					while((input = br.readLine()) != null) {
						System.out.println(input);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (response.getContent() != null) {
						try {
							response.getContent().close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				try {
					inputStream = response.getContent();
					outputStream = new BufferedOutputStream(new FileOutputStream(saveAsPath));
			        int bufSize = 1024 * 4;
			        byte[] buffer = new byte[bufSize];
			        int bytesRead;
			        while ((bytesRead = inputStream.read(buffer)) > 0) {
			        	outputStream.write(buffer, 0, bytesRead);
			        }
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (outputStream != null) {
						try {
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}

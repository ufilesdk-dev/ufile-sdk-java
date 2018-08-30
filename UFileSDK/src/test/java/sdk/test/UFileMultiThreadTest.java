package sdk.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileConfig;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import cn.ucloud.ufile.sender.PutSender;

/**
 * 多个线程并发进行put上传，使用短连接，保证可用性
 * @author york
 *
 */
public class UFileMultiThreadTest {
	public static void main(String args[]) throws ExecutionException, InterruptedException {
		String bucketName = "";
		String configPath = "";
		//加载配置项
		UFileConfig.getInstance().loadConfig(configPath);
		
		// specify your concurrency
		int concurrency = 3;
		// total count in each thread
		int count = 3;

		ExecutorService taskExecutor =
				Executors.newFixedThreadPool(concurrency);

		ConcurrentHashMap<Integer, Long> result = new ConcurrentHashMap<Integer, Long>();

		AtomicInteger threadId = new AtomicInteger(0);

		LinkedList<Future> futures = new LinkedList<Future>();
		for (int i = 0; i < concurrency; ++i) {
			String key = String.format("m-test %d", i);
			String filePath = String.format("/Users/york/java-sdk-test/m-test-%d", i);
			PutFileRunner putFileRunner = new PutFileRunner(
					threadId, key, bucketName, filePath, count, result);
			futures.add(taskExecutor.submit(putFileRunner));
		}

		for (Future future : futures) {
			future.get();
		}

		for (Map.Entry<Integer, Long> entry : result.entrySet()) {
			System.out.println("thread id: " + entry.getKey() + ", average latency: " + entry.getValue());
		}

		taskExecutor.shutdown();
		while (!taskExecutor.isTerminated()) {
			try {
				taskExecutor.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static class PutFileRunner implements Runnable {

		private String key;
		private String bucket;
		private String filePath;
		private int count;
		private ConcurrentHashMap result;
		private int id;

		public PutFileRunner(AtomicInteger threadId, String key,
							 String bucket, String filePath,
							 int count, ConcurrentHashMap<Integer, Long> result) {
			this.key = key;
			this.bucket = bucket;
			this.filePath = filePath;
			this.count = count;
			this.result = result;
			this.id = threadId.getAndIncrement();
		}

		@Override
		public void run() {
			long elapsedTime = 0;

			for (int i = 0; i < count; i++) {
				UFileClient ufileClient = new UFileClient();	
				UFileRequest request = new UFileRequest();
				request.setBucketName(bucket);
				request.setKey(key);
				request.setFilePath(filePath);

				PutSender sender = new PutSender();
				sender.makeAuth(ufileClient, request);

				long begin = System.currentTimeMillis();
				UFileResponse response = sender.send(ufileClient, request);
				long end = System.currentTimeMillis();

				if(response!=null) {
					System.out.println("finish " + i + "th running");
					elapsedTime += (end - begin);
				}

				//consume the http response body
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

				ufileClient.shutdown();
			}

			result.put(id, elapsedTime / count);
		}
	}


}

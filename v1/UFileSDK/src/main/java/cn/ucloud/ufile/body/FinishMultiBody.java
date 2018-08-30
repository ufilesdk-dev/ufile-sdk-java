package cn.ucloud.ufile.body;

public class FinishMultiBody {
	private String Bucket;
	private String Key;
	private String FileSize;
	
	public FinishMultiBody() {
		
	}

	public String getBucket() {
		return Bucket;
	}

	public void setBucket(String bucket) {
		Bucket = bucket;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getFileSize() {
		return FileSize;
	}

	public void setFileSize(String fileSize) {
		FileSize = fileSize;
	}
	
	

}

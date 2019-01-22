package cn.ucloud.ufile.body;

public class InitMultiBody {

	private String UploadId;
	private int BlkSize;
	private String Bucket;
	private String Key;
	
	public InitMultiBody() {
		
	}

	public String getUploadId() {
		return UploadId;
	}

	public void setUploadId(String uploadId) {
		UploadId = uploadId;
	}

	public int getBlkSize() {
		return BlkSize;
	}

	public void setBlkSize(int blkSize) {
		BlkSize = blkSize;
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
}

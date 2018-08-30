package cn.ucloud.ufile;

import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.StatusLine;

public class UFileResponse {

	private StatusLine statusLine;
	private long contentLength;
	private InputStream content;
	private Header[] headers;


	public void setStatusLine(StatusLine statusLine) {
		this.statusLine = statusLine;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

	public StatusLine getStatusLine() {
		return statusLine;
	}

	public long getContentLength() {
		return contentLength;
	}

	public InputStream getContent() {
		return content;
	}

	public void setHeaders(Header[] allHeaders) {
		this.headers = allHeaders;
	}

	public Header[] getHeaders() {
		return headers;
	}
}

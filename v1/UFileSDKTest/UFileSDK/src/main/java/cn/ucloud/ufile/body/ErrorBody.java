package cn.ucloud.ufile.body;

public class ErrorBody {
	private int RetCode;
	private String ErrMsg;
	
	public ErrorBody() {
		
	}

	public int getRetCode() {
		return RetCode;
	}

	public void setRetCode(int retCode) {
		RetCode = retCode;
	}

	public String getErrMsg() {
		return ErrMsg;
	}

	public void setErrMsg(String errMsg) {
		ErrMsg = errMsg;
	}
}

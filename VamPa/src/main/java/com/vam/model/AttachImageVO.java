package com.vam.model;

public class AttachImageVO {
	
	/* 상품 id */
	private int bookId;
	
	/* 파일 이름 */
	private String fileName;
	
	/* 경로 */
	private String uploadPath;
	
	/* uuid */
	private String uuid;
	
	public AttachImageVO() {
		// TODO Auto-generated constructor stub
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "AttachImageVO [bookId=" + bookId + ", fileName=" + fileName + ", uploadPath=" + uploadPath + ", uuid="
				+ uuid + "]";
	}

		
	
}

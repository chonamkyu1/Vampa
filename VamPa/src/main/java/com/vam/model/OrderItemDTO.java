package com.vam.model;

public class OrderItemDTO {
	
	// 주문 번호
	private String orderId;
	// 상품 번호
	private int bookId;
	// 주문 수량
	private int bookCount;
	// vam_orderItem 기본키
	private int orderitemId;
	// 상품 한 개 가격
	private int bookPrice;
	// 상품 할인율
	private double bookDiscount;
	// 상품 한개 구매 시 획득 포인트
	private int savePoint;
	
	/* DB테이블에 존재 하지 않는 데이터*/
	// 할인 적용된 가격
	private int salePrice;
	// 총 가격(할인 적용된 가격 * 주문 수량)
	private int totalPrice;
	// 총 획득 포인트(상품 한개 구매 시 획득 포인트 * 수량)
	private int totalSavePoint;
	
	
	public OrderItemDTO() {
		// TODO Auto-generated constructor stub
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public int getBookId() {
		return bookId;
	}


	public void setBookId(int bookId) {
		this.bookId = bookId;
	}


	public int getBookCount() {
		return bookCount;
	}


	public void setBookCount(int bookCount) {
		this.bookCount = bookCount;
	}


	public int getOrderitemId() {
		return orderitemId;
	}


	public void setOrderitemId(int orderitemId) {
		this.orderitemId = orderitemId;
	}


	public int getBookPrice() {
		return bookPrice;
	}


	public void setBookPrice(int bookPrice) {
		this.bookPrice = bookPrice;
	}


	public double getBookDiscount() {
		return bookDiscount;
	}


	public void setBookDiscount(double bookdiscount) {
		this.bookDiscount = bookDiscount;
	}


	public int getSavePoint() {
		return savePoint;
	}


	public void setSavePoint(int savePoint) {
		this.savePoint = savePoint;
	}


	public int getSalePrice() {
		return salePrice;
	}


	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}


	public int getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}


	public int getTotalSavePoint() {
		return totalSavePoint;
	}


	public void setTotalSavePoint(int totalSavePoint) {
		this.totalSavePoint = totalSavePoint;
	}


	@Override
	public String toString() {
		return "OrderItemDTO [orderId=" + orderId + ", bookId=" + bookId + ", bookCount=" + bookCount + ", orderitemId="
				+ orderitemId + ", bookPrice=" + bookPrice + ", bookDiscount=" + bookDiscount + ", savePoint="
				+ savePoint + ", salePrice=" + salePrice + ", totalPrice=" + totalPrice + ", totalSavePoint="
				+ totalSavePoint + "]";
	}
	
	public void initSaleTotal() {
		this.salePrice = (int)(this.bookPrice * (1- this.bookDiscount));
		this.totalPrice = this.salePrice * this.bookCount;
		this.savePoint = (int)(Math.floor(this.salePrice*0.05));
		this.totalSavePoint = this.savePoint * this.bookCount;
		
	}
	 
}

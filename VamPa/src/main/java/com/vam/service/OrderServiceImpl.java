package com.vam.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vam.mapper.AttachMapper;
import com.vam.mapper.BookMapper;
import com.vam.mapper.CartMapper;
import com.vam.mapper.MemberMapper;
import com.vam.mapper.OrderMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.BookVO;
import com.vam.model.CartDTO;
import com.vam.model.MemberVO;
import com.vam.model.OrderDTO;
import com.vam.model.OrderItemDTO;
import com.vam.model.OrderPageItemDTO;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private AttachMapper attachMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private BookMapper bookMapper;
	
	
	@Override
	public List<OrderPageItemDTO> getGoodsInfo(List<OrderPageItemDTO> orders) {
		
		List<OrderPageItemDTO> result = new ArrayList<OrderPageItemDTO>();
		
		
		for(OrderPageItemDTO ord : orders ) {
			
			OrderPageItemDTO goodsInfo = orderMapper.getGoodsInfo(ord.getBookId());
			
			goodsInfo.setBookCount(ord.getBookCount());
			
//			System.out.println("(serviceImpl)goodsInfo initSale 전 : " + goodsInfo);
			
			goodsInfo.initSaleTotal();
			
//			System.out.println("(serviceImpl)goodsInfo initSale 후 : " + goodsInfo);
			
			List<AttachImageVO> imageList = attachMapper.getAttachList(goodsInfo.getBookId());
			
			goodsInfo.setImageList(imageList);
			
			
			result.add(goodsInfo);
			
			
		}
		
		
		return result;
	}


	@Override
	@Transactional
	public void order(OrderDTO ord) {
		
	// 사용할 데이터 가져오기
		// 회원 정보
		MemberVO member = memberMapper.getMemberInfo(ord.getMemberId());
		// 주문 정보
		List<OrderItemDTO> ords = new ArrayList<>();
		for (OrderItemDTO oit : ord.getOrders()) {
			OrderItemDTO orderItem = orderMapper.getOrderInfo(oit.getBookId());
			System.out.println("orderItem : " + orderItem);
			
			// 수량 셋팅
			orderItem.setBookCount(oit.getBookCount());
			
			// 기본정보 셋팅
			orderItem.initSaleTotal();
			
			// List 객체 추가
			ords.add(orderItem);
		}
		
		// orderDTO 셋팅
		ord.setOrders(ords);
		ord.getOrderPriceInfo();
		
	// DB 주문, 주문상품(,배송정보) 넣기
		
		// orderId 만들기 및 OrderDTO 객체 orderId에 저장
		Date date = new Date(0);
		SimpleDateFormat format = new SimpleDateFormat("_yyyyMMddmm");
		String orderId =  member.getMemberId() + format.format(date);
		ord.setOrderId(orderId);
		
		// DB 넣기
		orderMapper.enrollOrder(ord);					// vam_order 등록
		for(OrderItemDTO oit : ord.getOrders()) {		// vam_orderItem 등록
			oit.setOrderId(orderId);
			orderMapper.enrollOrderItem(oit);
		}
		
	// 비용 포인트 변동 적용
		// 비용 차감 & 변동 돈(money) member 객체 사용
		int calMoney = member.getMoney();
		calMoney -= ord.getOrderFinalSalePrice();
		member.setMoney(calMoney);
		
		// 포인트 차감, 포인트 증가 & 변동포인트(point) Member 객체 사용
		int calPoint = member.getPoint();
		calPoint = calPoint - ord.getUsePoint() + ord.getOrderSavePoint();	 // 기존 포인트 - 사용포인트 + 획득포인트
		member.setPoint(calPoint);
		
		// 변동 돈, 포인트  DB 적용
		orderMapper.deductMoney(member);
	
	// 재고 변동 적용
		for(OrderItemDTO oit : ord.getOrders()) {
			// 변동 재고값 구하기
			BookVO book = bookMapper.getGoodsInfo(oit.getBookId());
			book.setBookStock(book.getBookStock() - oit.getBookCount());
			
			// 변동값 DB 적용
			orderMapper.deductStock(book);
		}

	// 장바구니 제거
		for(OrderItemDTO oit : ord.getOrders()) {
			CartDTO dto = new CartDTO();
			dto.setMemberId(ord.getMemberId());
			dto.setBookId(oit.getBookId());
			
			orderMapper.deleteOrderCart(dto);
		}
	
	
	}

}

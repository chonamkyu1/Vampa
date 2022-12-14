package com.vam.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.CartDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class CartMapperTests {

	@Autowired
	private CartMapper mapper;
	
	
//	@Test
//	public void addCart() {
//		String memberId = "admin";
//		int bookId = 5;
//		int count = 2;
//		
//		CartDTO cart = new CartDTO();
//		
//		cart.setMemberId(memberId);
//		cart.setBookId(bookId);
//		cart.setBookCount(count);
//		
//		int result = 0;
//		result = mapper.addCart(cart);
//		
//		System.out.println("결과 : " + result);
//	}
//	
//	
//	@Test
//	public void deleteCartTest() {
//		int cartId = 5;
//		
//		mapper.deleteCart(cartId);
//	}
//	
//	@Test
//	public void modifyCartTest() {
//		int cartId = 8;
//		int count = 8;
//		
//		CartDTO cart = new CartDTO();
//		cart.setCartId(cartId);
//		cart.setBookCount(count);
//		
//		mapper.modifyCount(cart);
//	}
	
	// 카트 목록
	@Test
	public void getCartTest() {
		String memberId = "admin";
		
		List<CartDTO> list = mapper.getCart(memberId);
		
		
		for(CartDTO cart : list) {
			System.out.println("cart : " + cart);
			cart.initSaleTotal();
			System.out.println("init cart : " + cart);
		}
	}
	
//	// 카트 확인
//	@Test
//	public void checkCartTest() {
//		String memberId = "admin";
//		int bookId = 2;
//		
//		CartDTO cart = new CartDTO();
//		cart.setMemberId(memberId);
//		cart.setBookId(bookId);
//		
//		CartDTO resultCart = mapper.checkCart(cart);
//		System.out.println("결과 : " + resultCart);
//		
//	}
//	
	
}

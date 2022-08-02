package com.vam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.AttachMapper;
import com.vam.mapper.CartMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.CartDTO;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private AttachMapper attachMapper;
	
	
	@Override
	public int addCart(CartDTO cart) {
		
		CartDTO checkCart =  cartMapper.checkCart(cart);
			
		if (checkCart != null) { // 값이 있다면
			return 2;
		}
	
		// 장바구니 등록 & 에러시 0 반환
		try {
			return cartMapper.addCart(cart);
		} catch (Exception e) {
			return 0;
		}
	}


	@Override
	public List<CartDTO> getCartList(String memberId) {
		List<CartDTO> cart = cartMapper.getCart(memberId);
		
		for (CartDTO dto : cart) {
			
			// 종합 정보 초기화
			dto.initSaleTotal();
			System.out.println("dto : " + dto);
			
			// 이미지 정보 얻기
			int bookId = dto.getBookId();
			List<AttachImageVO> imageList = attachMapper.getAttachList(bookId);
			
			dto.setImageList(imageList);
			System.out.println("dto : " + dto);
			
		}
		
		System.out.println("cart : " + cart);
		
		return cart;
	}


	@Override
	public int modifyCount(CartDTO cart) {
		
		return cartMapper.modifyCount(cart);
	}


	@Override
	public int deleteCart(int cartId) {

		return cartMapper.deleteCart(cartId);
	}

}

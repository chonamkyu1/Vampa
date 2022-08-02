package com.vam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vam.model.CartDTO;
import com.vam.model.MemberVO;
import com.vam.service.CartService;

@Controller
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@ResponseBody
	@PostMapping("/cart/add")
	public String addCartPost(CartDTO cart, HttpServletRequest request) {
		// 로그인 체크
		HttpSession session =  request.getSession();
		MemberVO mvo =  (MemberVO)session.getAttribute("member");
		
		if (mvo == null) {
			return "5";
		}
		
		// 카트 등록
		int result = cartService.addCart(cart);
		System.out.println("result : " + result);
		
		return result + "";
	}
	
	// 장바구니목록
	@GetMapping("/cart/{memberId}")
	public String cartPageGET(@PathVariable("memberId") String memberId, Model model) {
		
		List<CartDTO> list = cartService.getCartList(memberId);
		System.out.println("list : " + list);
		model.addAttribute("cartInfo", list);
		
		
		return "/cart";
	}
	
	// 장바구니 수량 수정
	@PostMapping("/cart/update")
	public String updateCartPOST(CartDTO cart) {
		System.out.println("수량 수정 컨트롤러~~" + cart);
		
		cartService.modifyCount(cart);
		
		
		return "redirect:/cart/" + cart.getMemberId();
	}
	
	
	// 장바구니 수량 삭제
	@PostMapping("/cart/delete")
	public String deleteCartPOST(CartDTO cart) {
		System.out.println("카트 삭제 컨트롤러 : " + cart);
		
		cartService.deleteCart(cart.getCartId());
		
		return "redirect:/cart/" + cart.getMemberId();
	}
	
}

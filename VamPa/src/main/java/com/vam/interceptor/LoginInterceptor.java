package com.vam.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor  implements HandlerInterceptor{

	
	// 로그인할떄 일반 유저가 관리자 페이지 못들어가게 인터셉트 처리
	@Override
	public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		
		System.out.println("LoginInterceptor Handler 작동");
		
		HttpSession session =  request.getSession();
		
		session.invalidate();
		
		return true;
	}
}

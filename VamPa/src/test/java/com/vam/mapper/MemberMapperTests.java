package com.vam.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.MemberVO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class MemberMapperTests {

	@Autowired
	private MemberMapper memberMapper; // MemberMapper.java 인터페이스 의존성 주입
	
	
	
	/*
	// 회원가입 쿼리 테스트 메서드 
	@Test
	public void memberJoin() throws Exception {
		MemberVO member = new MemberVO();
		
		member.setMemberId("spring_test"); 			// id
		member.setMemberPw("spring_test"); 			// 비밀번호
		member.setMemberName("spring_test"); 		// 이름
		member.setMemberMail("spring_test"); 		// 메일 
		member.setMemberAddr1("spring_test"); 		// 우편번호
		member.setMemberAddr2("spring_test");		// 주소
		member.setMemberAddr3("spring_test");		// 상세주소
		
		memberMapper.memberJoin(member); // 쿼리 메서드 실행
	}
	*/
	
	
	/*
	// 아이디 중복검사
	@Test 
	public void memberIdChk() throws Exception {
		String id = "admin"; // 존재하는 아이디
		String id2 = "test123"; // 존재하지 않는 아이디
		
		int result1 = memberMapper.idCheck(id);
		int result2 =  memberMapper.idCheck(id2);
		System.out.println("result1 : " + result1 + ", result2 : " +result2);
	}
	
	*/
	
	@Test
	public void memberLogin() throws Exception {
		MemberVO member = new MemberVO(); // MemberVO 변수 선언 및 초기화
		
//		member.setMemberId("admin");
//		member.setMemberPw("admin");
		
		member.setMemberId("testestet");
		member.setMemberPw("123123");
		
		memberMapper.memberLogin(member);
		System.out.println("결과 값 : " + memberMapper.memberLogin(member));
	}
	
	
	
	
}

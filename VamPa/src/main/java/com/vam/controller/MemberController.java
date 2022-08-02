package com.vam.controller;

import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vam.model.MemberVO;
import com.vam.service.MemberService;

@Controller
@RequestMapping(value = "/member")
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private BCryptPasswordEncoder pwEncoder;
	
	
	//회원가입 페이지 이동
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public void joinGET() {
			
		logger.info("회원가입 페이지 진입");
	}
		
	// 회원가입
	
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String joinPOST(MemberVO member) throws Exception {
		logger.info("join 진입");
		
//		// 회원가입 서비스 실행
//		memberService.memberJoin(member);
//		logger.info("join service 성공");
		
		
		String rawPw = ""; // 인코딩 전 비밀번호
		String encodePw = ""; // 인코딩 후 비밀번호
		
		rawPw = member.getMemberPw();			// 비밀번호 데이터 얻음
		encodePw = pwEncoder.encode(rawPw); 	// 비밀번호 인코딩
		member.setMemberPw(encodePw);			// 인코딩된 비밀번호 member객체에 다시 저장
		
		
		// 회원가입 쿼리 실행
		memberService.memberJoin(member);
		
		
		return "redirect:/main";
		
	}
	
	// 중복아이디 체크
	@RequestMapping(value = "/memberIdChk", method= RequestMethod.POST)
	@ResponseBody // 해당 코드를 추가하지 않으면 join.jsp메서드로 결과 반환 안됨
	public String memberIdChkPOST(String memberId) throws Exception {
//		logger.info("memberIdChk() 진입");
		
		int result = memberService.idCheck(memberId);
		
//		logger.info("결과값 : " + result);
		
		if (result != 0) {
			return "fail"; // 0이 아니면 중복 아이디 존재
		} else {
			return "success"; // 0이면 중복아이디 x
		}
		
	}

	// 이메일 인증
	@RequestMapping(value = "/mailCheck", method=RequestMethod.GET)
	@ResponseBody
	public String mailCheckGET(String email) throws Exception {
		
		// 뷰(view)로부터 넘어온 데이터 확인
		logger.info("이메일 데이터 전송 확인");
		logger.info("인증번호 : " + email);

		// 인증번호(난수) 생성
		Random random = new Random();
		int checkNum = random.nextInt(888888) + 111111;
		logger.info("인증번호 : " + checkNum);
		
		
		// 이메일 보내기 !!!
		String setFrom = "wh15312@naver.com"; // root-context.xml에 입력한 자신의 이메일 계정 주소
		String toMail = email; // 수신받을 이메일  뷰로부터 받은 이메일 주소인 변수 email을 사용
		String title = "회원가입 인증 이메일 입니다."; // 자신이 보낼 제목
		String content =  // 자신이 보낼 내용
				"홈페이지를 방문해주셔서 감사합니다." +
				"<br><br>" +
				"인증 번호는 : " + checkNum + "입니다." + 
				"<br>" + 
				"해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
		
		// 네이버 이메일로 메일 전송!!!
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			helper.setText(content, true);
			mailSender.send(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String num = Integer.toString(checkNum);
		
		
		return num;
	}
	
		
	//로그인 페이지 이동
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void loginGET() {
		
		logger.info("로그인 페이지 진입");
		
	}

	// 로그인
	@RequestMapping(value="/login.do", method = RequestMethod.POST)
	public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttributes rttr) throws Exception {
		System.out.println("login 메서드 진입");
		System.out.println("전달된 데이터 : " + member); // jsp에서 아이디 비밀번호 입력 후 넘어온 값
		
		HttpSession session =  request.getSession();
		String rawPw = "";
		String encodePw = "";
		
		
		MemberVO lvo = memberService.memberLogin(member); // 제출한 아이디와 일치하는 아이디가 있는지
		
		if (lvo != null) {						// 일치하는 아이디 존재
			
			rawPw = member.getMemberPw(); 		// 사용자가 제출한 비밀번호
			encodePw = lvo.getMemberPw(); 		// 데이터베이스에 저장한 인코딩된 비밀번호
			
			if (true == pwEncoder.matches(rawPw, encodePw)) { // 비밀번호 일치 여부 판단~!!
//				lvo.setMemberPw("");					  	  // 인코딩된 비밀번호 지움
				session.setAttribute("member", lvo); // 일치하는 아이디, 비밀번호 인 경우(로그인 성공)
				return "redirect:/main";
				
			} else  {
				rttr.addFlashAttribute("result", 0);
				return "redirect:/member/login"; 	// 로그인 페이지로 이동
			}
			
		} else {									// 일치하는 아이디가 존재하지 않을 시 (로그인 실패)
			
			rttr.addFlashAttribute("result", 0);
			return "redirect:/member/login"; 	// 로그인 페이지로 이동
		}
		
	}
	
	
	// 로그아웃
	
	@RequestMapping(value = "/logout.do" , method=RequestMethod.GET)
	public String logoutMainGET(HttpServletRequest request) throws Exception {  
		logger.info("logoutMainGET 메서드 진입");
		
		HttpSession session =  request.getSession();
		
		session.invalidate(); // 세션 초기화 = 삭제
		
		System.out.println("세션 삭제");
		
		return "redirect:/main";
	}
	
	@RequestMapping(value="/logout.do", method  = RequestMethod.POST)
	@ResponseBody
	public void logoutPOST(HttpServletRequest request) throws Exception { // 특정 데이터를 반환할때는 String 쓰지만 메서드 작업만 할 경우 void 처리
		logger.info("비동기 로그아웃 메서드 진입");
		
		HttpSession session =  request.getSession();
		
		session.invalidate();
		
	}
}

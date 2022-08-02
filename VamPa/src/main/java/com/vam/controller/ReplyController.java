package com.vam.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vam.model.Criteria;
import com.vam.model.ReplyDTO;
import com.vam.model.ReplyPageDTO;
import com.vam.service.ReplyService;

@RestController // 뷰를 따로 만들지 않고 http body에 바로 데이터를 담아 반환하려고 사용
@RequestMapping("/reply")
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
	
	// 댓글 등록
	@PostMapping("/enroll")
	public void enrollReplyPOST(ReplyDTO dto) {
		replyService.enrollReply(dto);
		
	}
	
	// 댓글 체크
	// memberId, bookId 파라미터
	// 존재할경우 : 1 / 존재하지 않을 경우 : 0
	
	@PostMapping("/check")
	public String replyCheckPOST(ReplyDTO dto) {
		System.out.println("dto : " + dto);
		
		
		return replyService.checkReply(dto);
	}
	
	
	@GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ReplyPageDTO replyListPOST(Criteria cri) {
		System.out.println("cri : " + cri);
		
		return replyService.replyList(cri);
	}
	
	
	// 댓글 수정
	@PostMapping("/update")
	public void replyModifyPOST(ReplyDTO dto) {
		System.out.println("dto : " + dto);
		
		replyService.updateReply(dto);
	}
	
	// 댓글 삭제
	@PostMapping("/delete")
	public void replyDeletePOST(ReplyDTO dto) {
		System.out.println("dto : " + dto);
		
		replyService.deleteReply(dto);
	}
}

package com.vam.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vam.mapper.AttachMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.BookVO;
import com.vam.model.CateVO;
import com.vam.model.Criteria;
import com.vam.model.PageDTO;
import com.vam.model.ReplyDTO;
import com.vam.model.SelectDTO;
import com.vam.service.AttachService;
import com.vam.service.BookService;
import com.vam.service.ReplyService;

@Controller
public class BookController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookController.class); 
	
	@Autowired
	private AttachService attachService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ReplyService replyService;
	
	
	// 메인페이지 이동
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public void mainPageGET(Model model) {
		
		logger.info("메인 페이지 진입");
		
		List<CateVO> list1 = bookService.getCateCode1();
		System.out.println("list1: "+ list1);
		List<CateVO> list2 = bookService.getCateCode2();
		System.out.println("list2: " + list2);
		List<SelectDTO> ls = bookService.likeSelect();
		System.out.println("ls : " + ls);
		
		model.addAttribute("cate1", list1);
		model.addAttribute("cate2", list2);
		model.addAttribute("ls", ls);
		
	}
	
	@GetMapping("/display")
	public ResponseEntity<byte[]> getImage(String fileName) {
		
		logger.info("getImage()....... fileName : " + fileName);
		
		File file = new File("c:\\upload\\" + fileName); // c:\\upload\ 가 아닌 c:\\upload\\로 작성한 이유는 특수문자를 인식하려고
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			header.add("Content-type", Files.probeContentType(file.toPath()));
			result  = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;  
	}
	
	
	// 이미지 정보 반환
	@GetMapping(value="/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE) // 반환 데이터가 json 형식이 되도록 지정해주기 위해 produces 속성 추가
	public ResponseEntity<List<AttachImageVO>> getAttachList(int bookId) {
		
		logger.info("getAttachList ....... bookId : " + bookId);
		
		return new ResponseEntity<List<AttachImageVO>>(attachService.getAttachList(bookId), HttpStatus.OK);
	}
	
	
	// 상품 검색
	@GetMapping("search")
	public String searchGoodsGET(Criteria cri, Model model) {
		
		logger.info("cri : " + cri);

		List<BookVO> list = bookService.getGoodsList(cri);
		logger.info("pre list : " + list);
		
		if(!list.isEmpty()) {
			model.addAttribute("list", list);
			logger.info("list : " + list);
			
		} else {
			model.addAttribute("listCheck", "empty");
			
			return "search";
		}
		
		model.addAttribute("pageMaker", new PageDTO(cri, bookService.goodsGetTotal(cri)));
		
		String[] typeArr= cri.getType().split("");
		
		for (String s : typeArr) {
			if (s.equals("T") || s.equals("A")) {
				model.addAttribute("filter_info", bookService.getCateInfoList(cri));
			}
		}
		
		return "search";
		
	}
	
	
	@GetMapping("/goodsDetail/{bookId}") // 템플릿 변수 bookId
	public String goodsDetailGet(@PathVariable("bookId") int bookId, Model model) {
		logger.info("goodsDetailGet() ......................");
		
		model.addAttribute("goodsInfo", bookService.getGoodsInfo(bookId));
		
		return "/goodsDetail";
		
	}

	
	// 리뷰 쓰기
	@GetMapping("/replyEnroll/{memberId}")
	public String replyEnrollWindowGET(@PathVariable("memberId") String memberId, int bookId, Model model) {
		BookVO book = bookService.getBookIdName(bookId);
		model.addAttribute("bookInfo", book);
		model.addAttribute("memberId", memberId);
		
		return "/replyEnroll";
	}
	
	// 리뷰 수정 팝업창
	@GetMapping("/replyUpdate")
	public String replyUpdateReply(ReplyDTO dto, Model model) {
		BookVO book = bookService.getBookIdName(dto.getBookId());
		System.out.println("book : " + book);
		
		model.addAttribute("bookInfo", book);
		model.addAttribute("replyInfo", replyService.getUpdateReply(dto.getReplyId()));
		model.addAttribute("memberId", dto.getMemberId());
		
		
		return "/replyUpdate";
		
		
		
	}
	
}
 
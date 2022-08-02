package com.vam.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.AuthorVO;
import com.vam.model.BookVO;
import com.vam.model.Criteria;
import com.vam.model.PageDTO;
import com.vam.service.AdminService;
import com.vam.service.AuthorService;

import net.coobird.thumbnailator.Thumbnails;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private AdminService adminService;
	
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public void adminMainGET() throws Exception {
		logger.info("관리자 페이지 이동");
	}
	
	
	// 상품 관리 페이지 접속
	@RequestMapping(value = "goodsManage", method = RequestMethod.GET)
	public void goodsManageGET(Criteria cri, Model model) throws Exception {
		logger.info("상품 관리 페이지 접속");
		
		// 상품 리스트 데이터
		List list = adminService.goodsGetList(cri);
		
		if (!list.isEmpty()) { 
			model.addAttribute("list", list);
		} else {
			model.addAttribute("listCheck", "empty");
			return;
		}
		
		// 페이지 인터페이스 데이터
		model.addAttribute("pageMaker", new PageDTO(cri, adminService.goodsGetTotal(cri)));
		
	}
	
	// 상세 상품 조회 페이지
	@GetMapping({"/goodsDetail", "/goodsModify"})
	public void goodsGetInfoGET(int bookId, Criteria cri, Model model) throws JsonProcessingException {
		logger.info("goodsGetInfo()........ " + bookId);
		
		ObjectMapper mapper = new ObjectMapper();
		
		// 카테고리 리스트 데이터
		model.addAttribute("cateList", mapper.writeValueAsString(adminService.cateList()));
		
		// 목록 페이지 조건 정보
		model.addAttribute("cri", cri);
		
		// 조회 페이지 정보
		model.addAttribute("goodsInfo", adminService.goodsGetDetail(bookId));
		
		
	}
	
	// 상품 정보 수정
	@PostMapping("/goodsModify")
	public String goodsModifyPOST(BookVO vo, RedirectAttributes rttr) {
		logger.info("goodsModifyPOST .........." + vo);
		
		int result = adminService.goodsModify(vo);
		
		rttr.addFlashAttribute("modify_result", result); // 메서드가 작업을 정상적으로 수행했음을 알리는 메시지도 보냄
		
		return "redirect:/admin/goodsManage";
		
	}
	
	
	// 상품 삭제
	@PostMapping("/goodsDelete")
	public String goodsDeletePOST(int bookId, RedirectAttributes rttr) {
		logger.info("goodsDeletePOST......... " + bookId);
		
		// 상품 정보 삭제 시 이미지 먼저 삭제를 해줘야 db에 참조무결성을 위배하지 않음
		List<AttachImageVO> fileList = adminService.getAttachInfo(bookId);
		
		// 파일자체가 null값이면 굳이 실행될 필요가 없다
		if(fileList != null) {
			List<Path> pathList = new ArrayList();
			
			for(AttachImageVO vo : fileList) {
				
				// 원본 이미지
				Path path = Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName());
				System.out.println("path " + path);
				pathList.add(path);
				
				// 썸네일 이미지
				path = Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName());
				System.out.println("path " + path);
				pathList.add(path);
				
			}
			
			for (Path path : pathList) {
				path.toFile().delete();
			}
			
			
		}
		
		
		int result = adminService.goodsDelete(bookId);
		
		rttr.addFlashAttribute("delete_result", result);
		
		return "redirect:/admin/goodsManage";
	}
	
	
	
	// 상품 등록 페이지 접속	
	@RequestMapping(value = "goodsEnroll", method = RequestMethod.GET)
	public void goodsEnrollGet(Model model) throws Exception {
		logger.info("상품 등록 페이지 접속");
		
		ObjectMapper objm = new ObjectMapper(); // json 형식 데이터 변환
		
		List list = adminService.cateList();
		
		String cateList = objm.writeValueAsString(list);
		
		model.addAttribute("cateList", cateList);
		
		logger.info("변경 전 ..............." + list);
		logger.info("변경 후 ..............." + cateList);
		
	}
	
	// 작가 등록 페이지 접속
	@RequestMapping(value = "authorEnroll", method = RequestMethod.GET)
	public void authorEnrollGet() throws Exception {
		logger.info("작가 등록 페이지 접속");
	}
	
	// 작가 등록
	@RequestMapping(value = "authorEnroll.do", method = RequestMethod.POST)
	public String authorEnrollPOST(AuthorVO author, RedirectAttributes rttr) throws Exception { // redirect는 데이터 전송을 위한 모델 객체 같은 것
		logger.info("authorEnroll : " + author); // 뷰로부터 전달받은 데이터 확인하기 위해
		
		authorService.authorEnroll(author); // 작가 등록 쿼리 수행
		
		rttr.addFlashAttribute("enroll_result", author.getAuthorName()); // 등록 성공 메시지 (작가 이름 전송)
		// addFlashAttribute 사용 이유 : 작가 등록 성공 하였다는 경고창 뜨도록 함 -> 전송된 데이터가 계속 남아있다면 경고창 계속 뜸
		// 이런일 방지하고자 데이터가 일회성으로 사용될 수 있도록 addFlashAttribute 사용
		
		return "redirect:/admin/authorManage";
	}
	
	
	// 작가 관리 페이지 접속
	@RequestMapping(value = "authorManage", method = RequestMethod.GET)
	public void authorManageGet(Criteria cri, Model model) throws Exception {
		logger.info("작가 관리 페이지 접속");
		
		// 작가 목록 출력 데이터
		List list = authorService.authorGetList(cri);
		
		if (!list.isEmpty()) {
			model.addAttribute("list", list);			// 작가가 존재할 경우
		} else {
			model.addAttribute("listCheck", "empty");	// 작가가 존재하지 않을 경우
		}
		
		// 페이지 이동 인터페이스 데이터 !
		int total = authorService.authorGetTotal(cri);
		
		PageDTO pageMaker = new PageDTO(cri, total);
		
		System.out.println("pageMaker : " + pageMaker);
		
		model.addAttribute("pageMaker", pageMaker);
		
	}
	
	// 작가 상세 페이지
	@GetMapping({"/authorDetail", "/authorModify"})
	public void authorGetInfoGET(int authorId, Criteria cri, Model model) throws Exception {
		
		logger.info("authorDetail .....  : " + authorId);
		
		// 작가 관리 페이지 정보
		model.addAttribute("cri", cri);
		
		// 선택 작가 정보 (해당 페이지 선택한 작가 정보)
		
		AuthorVO vo = authorService.authorGetDetail(authorId);
		
		
		model.addAttribute("authorInfo", vo);
		
		System.out.println("vo :" + vo);
		
	}
	
	
	// 작가 수정
	@PostMapping("/authorModify")
	public String authorModifyPOST(AuthorVO author, RedirectAttributes rttr) throws Exception {
		logger.info("authorModifyPOST ....... " + author);
		
		int result = authorService.authorModify(author);
		
		System.out.println("result : " + result);
		
		rttr.addFlashAttribute("modify_result", result);
		
		return "redirect:/admin/authorManage";
	}
	
	
	// 상품 삭제
	@PostMapping("/authorDelete")
	public String authorDeletePOST(int authorId, RedirectAttributes rttr) {
		logger.info("authorDeletePOST ........... authorId : " + authorId);
		
		int result = 0;
		
		try { // try-catch 사용 -> 참조되지 않는 행을 지울떄는 상관 없으나 참조되고있는 행이 있으면 오류가 남
			System.out.println("result : " + result);
			result = authorService.authorDelete(authorId);
			
		} catch (Exception e) {
			e.printStackTrace();
			result = 2;
			rttr.addFlashAttribute("delete_result", result);
			return "redirect:/admin/authorManage";
		}
		
		System.out.println("result : " + result);
		rttr.addFlashAttribute("delete_result", result);
		return "redirect:/admin/authorManage";
	}
	
	
	
	
	
	// --------------------------   상 품  ----------------------------------------
	//상품 등록
	@PostMapping("/goodsEnroll")
	public String goodsEnrollPOST(BookVO book, RedirectAttributes rttr) throws Exception {
		logger.info("goodsEnrollPOST ..... book : " + book);
		
		adminService.bookEnroll(book);
		
		rttr.addFlashAttribute("enroll_result", book.getBookName());
		
		return "redirect:/admin/goodsManage";
		
	}
	
	// 작가 검색 팝업창
	@GetMapping("/authorPop")
	public void authorPopGET(Criteria cri, Model model) throws Exception {
		logger.info("authorPopGET .......");
		System.out.println("컨트롤러 cri : " + cri);
		
		cri.setAmount(5); 
		// 작가 게시물 출력 데이터
		List list = authorService.authorGetList(cri);
		
		if (!list.isEmpty()) {
			model.addAttribute("list", list); // 작가 존재 하는 경우
		} else {
			model.addAttribute("listCheck", "empty"); // 작가 존재하지 않는 경우
		}
		
		// 페이지 이동 인터페이스 데이터
		
		model.addAttribute("pageMaker", new PageDTO(cri, authorService.authorGetTotal(cri))); // 이 상태로 보내면 페이징 리스트 10개씩 전송, 155번쨰 줄 참고
		
	}

	
	// 첨부파일 업로드
	@PostMapping(value= "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<AttachImageVO>> uploadAjaxActionPOST(MultipartFile[] uploadFile) { // responsebody와 responseEntity 동일한데 차이점은 body부분
		logger.info("uploadAjaxActionPOST ............. uploadFile : " + uploadFile);
		
		// 향상된 for문
		for (MultipartFile multipartFile : uploadFile) {
			logger.info("파일 이름 : " + multipartFile.getOriginalFilename());
			logger.info("파일 타입 : " + multipartFile.getContentType());
			logger.info("파일 크기 : " + multipartFile.getSize());
		}
		
		// 이미지 파일 체크
		for (MultipartFile multipartFile : uploadFile) {
			File checkfile = new File(multipartFile.getOriginalFilename());
			String type = null;
			try {
				type = Files.probeContentType(checkfile.toPath());
				logger.info("MIME TPYE : " + type);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(!type.startsWith("image")) {
				List<AttachImageVO> list = null;
				ResponseEntity<List<AttachImageVO>> result =  new ResponseEntity<List<AttachImageVO>>(list, HttpStatus.BAD_REQUEST);
				System.out.println("result : " + result);
				return result;
			}
			
		} // for
		
		
		
		String uploadFolder = "C:\\upload"; // 저장 위치
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜형식 변환
		
		Date date = new Date();
		
		String str = sdf.format(date);
		
		String datePath = str.replace("-", File.separator); // '-' 구분자 -> yyyy\mm\dd
		
		// 폴더 생성
		File uploadPath = new File(uploadFolder, datePath); // c:\\upload\yyyy\mm\dd
		
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs(); // 한개 폴더 생성시 mkdir 여러개 폴더 생성시 mkdirs
		}
		
		
		// 이미지 정보를 담는 객체
		List<AttachImageVO> list = new ArrayList();
		
		
		// 파일이름
		
		for (MultipartFile multipartFile : uploadFile) {
			
			// 이미지 정보 객체
			AttachImageVO vo = new AttachImageVO();
			
			// 파일 이름
			String uploadFileName = multipartFile.getOriginalFilename();
			vo.setFileName(uploadFileName);
			vo.setUploadPath(datePath);

			// uuid 적용 파일 이름
			String uuid = UUID.randomUUID().toString();
			vo.setUuid(uuid);
			
			uploadFileName = uuid + "_" + uploadFileName;
			
			// 파일 위치, 파일 이름을 합친 File 객체
			File saveFile = new File(uploadPath, uploadFileName);
			
			
			// 파일 저장
			try {
				
				// 뷰로부터 전달 받은 파일을 지정한 폴더에 저장하기 위해서는 transferTo() 메서드 사용
				multipartFile.transferTo(saveFile);
				
				/* 방법 1*/
//				// 썸네일 생성
//				File thumnailFile = new File(uploadPath, "s_" + uploadFileName);
//				BufferedImage bo_image = ImageIO.read(saveFile);
//				
//				// 비율
//				double ratio =3;
//				// 널이, 높이
//				int width = (int)(bo_image.getWidth()/ ratio);
//				int height = (int)(bo_image.getHeight()/ ratio);
//				
//				BufferedImage bt_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//				Graphics2D graphic = bt_image.createGraphics();
//				
//				graphic.drawImage(bo_image, 0, 0, width, height, null);
//				
//				ImageIO.write(bt_image, "jpg", thumnailFile);
				
				/*방법 2*/
				File thumnailFile = new File(uploadPath, "s_" + uploadFileName);
				
				BufferedImage bo_image = ImageIO.read(saveFile);
				
				 //비율
				double ratio =3;
				// 널이, 높이
				int width = (int)(bo_image.getWidth()/ ratio);
				int height = (int)(bo_image.getHeight()/ ratio);
				
				
				Thumbnails.of(saveFile).size(width, height).toFile(thumnailFile);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			list.add(vo);
			
			System.out.println("list : " + list);
			
		}	// for
		
		// http 바디에 추가될 데이터는 list<AttachImageVO>
 		ResponseEntity<List<AttachImageVO>> result = new ResponseEntity<List<AttachImageVO>>(list, HttpStatus.OK);
 		
 		System.out.println("result : " + result);
 		
 		return result;
	}

	// 이미지 파일 삭제 
	@PostMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(String fileName) {
		logger.info("deleteFile ......... fileName : " + fileName);
		
		File file = null;
		
		try {
			
			
			// 썸네일 파일 삭제
			file = new File("c:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
			
			file.delete();
			
			// 원본 파일 삭제
			String originFileName = file.getAbsolutePath().replace("s_", "");
			logger.info("originFileName : " + originFileName);
			
			file = new File(originFileName);
			
			file.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// 이미지 파일 삭제 요청을 정상적으로 못했다는 의미 = 실패
			return new ResponseEntity<String>("fail", HttpStatus.NOT_IMPLEMENTED);
		}
		
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
				
		
	}
	
	
}

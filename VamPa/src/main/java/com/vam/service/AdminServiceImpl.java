package com.vam.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vam.mapper.AdminMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.BookVO;
import com.vam.model.CateVO;
import com.vam.model.Criteria;

@Service
public class AdminServiceImpl implements AdminService{
	
	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
	
	@Autowired
	private AdminMapper adminMapper;
	
	
	// 상품 등록
	
	
	@Transactional
	@Override
	public void bookEnroll(BookVO book) {
		logger.info("(service)bookEnroll...........!!");
		
		System.out.println("bookEnroll serviceImpl~~~!!");
		
		
		
		adminMapper.bookEnroll(book);
		
		if (book.getImageList() == null || book.getImageList().size() <= 0) {
			return;
		}
		
//		// 일반적 for 문
//		for (int i=0; i < book.getImageList().size(); i++) {
//			book.getBookId();
//		}
		
		System.out.println("book.getImageList() : " + book.getImageList());
		
//		 향상된 for 문 , 이미지 등록
		for (AttachImageVO attach : book.getImageList()) {
			attach.setBookId(book.getBookId());
			System.out.println("attach : " + attach);
			adminMapper.imageEnroll(attach);
		}
		
	}


	@Override
	public List<CateVO> cateList() {
		logger.info("(service)cateList");
		
		
		return adminMapper.cateList();
	}


	@Override
	public List<BookVO> goodsGetList(Criteria cri) {
		logger.info("(service)goodsGetList().........");
		return adminMapper.goodsGetList(cri);
	}


	@Override
	public int goodsGetTotal(Criteria cri) {
		logger.info("(service)goodsGetTotal().............");
		return adminMapper.goodsGetTotal(cri);
	}


	@Override
	public BookVO goodsGetDetail(int bookId) {
		logger.info("(service)goodsGetDetail().............");
		return adminMapper.goodsGetDetail(bookId);
	}


	@Transactional
	@Override
	public int goodsModify(BookVO vo) {
		logger.info("goodsModify........");
//		return adminMapper.goodsModify(vo);
		
		int result = adminMapper.goodsModify(vo);
		
//		if (result == 1 && vo.getImageList() != null && vo.getImageList().size() > 0 ) {
//			
//			adminMapper.deleteImageAll(vo.getBookId());
//			
//			for (AttachImageVO attach : vo.getImageList()) {
//				attach.setBookId(vo.getBookId());
//				adminMapper.imageEnroll(attach);
//			}
//		}
			
			
		if (result == 1 && vo.getImageList() != null && vo.getImageList().size() > 0 ) {
		
			adminMapper.deleteImageAll(vo.getBookId());
			
			vo.getImageList().forEach(attach -> {
				attach.setBookId(vo.getBookId());
				adminMapper.imageEnroll(attach);
				});
		}
	
			
			
			
		
		return result;
		
	}

	
	@Override
	@Transactional // 2개의 쿼리문이 실행되기 떄문에 트랜잭션 처리를 해줘야함
	public int goodsDelete(int bookId) {
		logger.info("goodsDelete........");
		
		// DB 데이터 삭제 (상품 정보, 이미지 정보)
		
		adminMapper.deleteImageAll(bookId);
		
		
		return adminMapper.goodsDelete(bookId);
	}


	@Override
	public List<AttachImageVO> getAttachInfo(int bookId) {
		logger.info("(service) getAttachInfo...........");
		
		return adminMapper.getAttachInfo(bookId);
	}

}

package com.vam.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.AdminMapper;
import com.vam.mapper.AttachMapper;
import com.vam.mapper.BookMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.BookVO;
import com.vam.model.CateFilterDTO;
import com.vam.model.CateVO;
import com.vam.model.Criteria;
import com.vam.model.SelectDTO;

@Service
public class BookServiceImpl implements BookService {
	private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
	
	
	@Autowired
	private BookMapper bookMapper;
	
	@Autowired
	private AttachMapper attachMapper;
	
	@Autowired
	private AdminMapper adminMapper;
	
	
	// 상품 검색
	@Override
	public List<BookVO> getGoodsList(Criteria cri) {
		logger.info("(service)getGoodsList()..............");
		System.out.println("serviceImpl getGoodsList~~~~");
		
		String type = cri.getType();
		String[] typeArr = type.split("");
		String[] authorArr = bookMapper.getAuthorIdList(cri.getKeyword());
		
		System.out.println("authorArr : " + authorArr.toString());
		
		
		if(type.equals("A") || type.equals("AC") || type.equals("AT") || type.equals("ACT")) {
			if(authorArr.length == 0) {
				return new ArrayList();
			}
		}
		
		for (String t : typeArr) {
			if(t.equals("A")) {
				cri.setAuthorArr(authorArr);
			}
		}
		
		List<BookVO> list = bookMapper.getGoodsList(cri);
		
		for (BookVO book : list) {
			int bookId = book.getBookId();
			
			List<AttachImageVO> imageList = attachMapper.getAttachList(bookId);
			
			book.setImageList(imageList);
		}
		
		
		
		return list;
//		return bookMapper.getGoodsList(cri);
	}

	// 상품 총 갯수
	@Override
	public int goodsGetTotal(Criteria cri) {
		logger.info("(service)goodsGetTotal()..............");
		return bookMapper.goodsGetTotal(cri);
	}
	
	// 국내 카테고리 리스트
	@Override
	public List<CateVO> getCateCode1() {
		logger.info("(service)getCateCode1()..............");
		System.out.println("serviceImpl getCateCode1~~~~");
		return bookMapper.getCateCode1();
	}

	// 국외 카테고리 리스트
	@Override
	public List<CateVO> getCateCode2() {
		logger.info("(service)getCateCode2()..............");
		System.out.println("serviceImpl getCateCode2~~~~");
		return bookMapper.getCateCode2();
	}

	@Override
	public List<CateFilterDTO> getCateInfoList(Criteria cri) {
		List<CateFilterDTO> filterInfoList = new ArrayList<CateFilterDTO>();
		
		
		String[] typeArr = cri.getType().split("");
		String[] authorArr;
		
		for(String type : typeArr) {
			if(type.equals("A")) {
				authorArr = bookMapper.getAuthorIdList(cri.getKeyword());
				if(authorArr.length == 0) {
					return filterInfoList;
				}
				cri.setAuthorArr(authorArr);
			}
		}
		
		// 카테고리 코드 반환해주는 getAuthorIdList 호출
		String[] cateList = bookMapper.getCateList(cri);
		
		String tempCateCode = cri.getCateCode();
		
		for (String cateCode : cateList) {
			cri.setCateCode(cateCode);
			CateFilterDTO filterInfo = bookMapper.getCateInfo(cri);
			filterInfoList.add(filterInfo);
		}
		cri.setCateCode(tempCateCode);
		
		return filterInfoList;
	}

	@Override
	public BookVO getGoodsInfo(int bookId) {
		
		BookVO goodsInfo = bookMapper.getGoodsInfo(bookId);
		
		// 이미지 정보 추가
		goodsInfo.setImageList(adminMapper.getAttachInfo(bookId));
		
		
		return goodsInfo;
	}

	@Override
	public BookVO getBookIdName(int bookId) {
		return bookMapper.getBookIdName(bookId);
	}

	@Override
	public List<SelectDTO> likeSelect() {
		List<SelectDTO> list = bookMapper.likeSelect();
		System.out.println("list : " + list);
		
		for(SelectDTO dto : list) {
			int bookId = dto.getBookId();
			
			List<AttachImageVO> imageList = attachMapper.getAttachList(bookId);
			dto.setImageList(imageList);
		}
			
		return list;
	}

}

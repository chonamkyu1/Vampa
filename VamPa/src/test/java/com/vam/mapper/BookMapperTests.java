package com.vam.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.BookVO;
import com.vam.model.Criteria;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class BookMapperTests {
	
	@Autowired
	private BookMapper mapper;
	
	
	/*
	@Test
	public void getGoodsListTest() {
		Criteria cri = new Criteria();
		
		// 테스트 키워드
		 cri.setKeyword("test");
		System.out.println("cri : " + cri);
		
		List<BookVO> list = mapper.getGoodsList(cri);
		System.out.println("list : " + list);
		
		System.out.println("===========================");
		int goodsTotal = mapper.goodsGetTotal(cri);
		System.out.println("total : " + goodsTotal);
	}
	*/
	
	
	// 작가 id 리스트 요청
	/*
	@Test
	public void getAuthorId() {
		String keyword = "폴";
		
		String[] list = mapper.getAuthorIdList(keyword);
		
		System.out.println("결과 : " + list.toString());
		
		for(String id :  list) {
			System.out.println("개별 결과 : " + id);
		}
	}
	*/
	
//	
//	// 검색 (동적 쿼리 적용) - 작가
//	@Test
//	public void getGoodsListTest1() {
//		Criteria cri = new Criteria();
//		String type = "A";
//		String keyword = "김난도";  	// DB에 등록된 작가
////		String keyword = "없음";		// DB에 등록되지 않은 작가
//		String cateCode = "";	
//		
//		cri.setType(type);
//		cri.setKeyword(keyword);
//		cri.setCateCode(cateCode);
//		cri.setAuthorArr(mapper.getAuthorIdList(keyword));
//		
//		List<BookVO> list = mapper.getGoodsList(cri);
//		System.out.println("작가 cri : " + cri);
//		System.out.println("작가 list : " + list);
//		System.out.println("================================================");
//	}
//	
//	
//	
//	// 검색 (동적 쿼리 적용) - 책제목
//	@Test
//	public void getGoodsListTest2() {
//		Criteria cri = new Criteria();
//		String type ="T";
//		String keyword = "부자"; 		// 테이블에 등록된 책 제목 데이터
////		String keyword = "없음";			// 테이블에 등록되지 않은 데이터
//		String cateCode = "";
//		
//		cri.setType(type);
//		cri.setKeyword(keyword);
//		cri.setAuthorArr(mapper.getAuthorIdList(keyword));
//		cri.setCateCode(cateCode);
//		
//		List<BookVO> list = mapper.getGoodsList(cri);
//		System.out.println("책제목 cri : " + cri);
//		System.out.println("책제목 list : " + list);
//		System.out.println("================================================");
//		
//		
//	}
//	
//	// 검색 (동적 쿼리 적용) - 카테고리
//	@Test
//	public void getGoodsListTest3() {
//		Criteria cri = new Criteria();
//		String type = "C";
//		String keyword = "";
//		String cateCode = "102002";
//		
//		cri.setType(type);
//		cri.setKeyword(keyword);
//		cri.setAuthorArr(mapper.getAuthorIdList(keyword));
//		cri.setCateCode(cateCode);
//		
//		List<BookVO> list = mapper.getGoodsList(cri);
//		System.out.println("카테고리 cri : " + cri);
//		System.out.println("카테고리 list : " + list);
//		System.out.println("================================================");
//	}
//	
//	// 검색 (동적 쿼리 적용) - 카테고리 + 작가
//	@Test
//	public void getGoodsListTest4() {
//		Criteria cri = new Criteria();
//		String type = "AC";
//		String keyword = "김난도";
////		String keyword = "머스크";
//		String cateCode = "102002";
//		
//		
//		cri.setType(type);
//		cri.setKeyword(keyword);
//		cri.setAuthorArr(mapper.getAuthorIdList(keyword));
//		cri.setCateCode(cateCode);
//		
//		List<BookVO> list = mapper.getGoodsList(cri);
//		System.out.println("카테 + 작가 cri : " + cri);
//		System.out.println("카테 + 작가 list : " + list);
//		System.out.println("================================================");
//		
//	}
//	
//	// 검색 (동적 쿼리 적용) - 카테고리 + 책제목
//	@Test
//	public void getGoodsListTest5() {
//		Criteria cri = new Criteria();
//		String type = "CT";
//		String keyword = "부자";		// 카테고리 존재하는책
////		String keyword = "없음"; 			// 카테고리 존재하지 않는 책
//		String cateCode = "102002";
//		
//		cri.setType(type);
//		cri.setKeyword(keyword);
//		cri.setAuthorArr(mapper.getAuthorIdList(keyword));
//		cri.setCateCode(cateCode);
//		
//		
//		List<BookVO> list = mapper.getGoodsList(cri);
//		System.out.println("카테 + 책제목 cri : " + cri);
//		System.out.println("카테 + 책제목 list : " + list);
//				
//	}
//	
	
//	// 카테고리 리스트
//	@Test
//	public void getCateListTest1() {
//		Criteria cri = new Criteria();
//		
//		String type = "TC";
//		String keyword = "test";
//		
//		cri.setType(type);
//		cri.setKeyword(keyword);
//		
//		String[] cateList = mapper.getCateList(cri);
//		for (String codeNum : cateList) {
//			System.out.println("cateNum ::::: " + codeNum);
//		}
//	}
//	
//	@Test
//	public void getCateInfoTest1() {
//		Criteria cri = new Criteria();
//		
//		String type ="TC";
//		String keyword = "부자";
//		String cateCode = "203000";
//		
//		cri.setType(type);
//		cri.setKeyword(keyword);
//		cri.setCateCode(cateCode);
//		
//		mapper.getCateInfo(cri);
//		
//		System.out.println(mapper.getCateInfo(cri));
//	}
		
	
	@Test
	public void getGoodsInfo() {
		int bookId = 14340;
		BookVO goodsInfo = mapper.getGoodsInfo(bookId);
		System.out.println("=================================");
		System.out.println("goodsInfo : " + goodsInfo);
		System.out.println("=================================");
	}
		
}

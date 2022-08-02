package com.vam.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.AuthorMapper;
import com.vam.model.AuthorVO;
import com.vam.model.Criteria;

@Service
public class AuthorServiceImpl implements AuthorService{

	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);
	
	
	@Autowired
	private AuthorMapper authorMapper;
	
	
	// 작가 등록
	@Override
	public void authorEnroll(AuthorVO author) throws Exception {
		authorMapper.authorEnroll(author);
	}

	// 작가 목록
	@Override
	public List<AuthorVO> authorGetList(Criteria cri) throws Exception {
		logger.info("(service)authorGetlist() ..... " + cri);
		System.out.println("(service)authorGetlist() ..... " + cri);
		
		
		return authorMapper.authorGetList(cri);
	}

	// 작가 총 수
	@Override
	public int authorGetTotal(Criteria cri) throws Exception {
		logger.info("(service)authorGetTotal() ...."+ cri);
		System.out.println("(service)authorGetTotal() ...."+ cri);
		return authorMapper.authorGetTotal(cri);
	}

	@Override
	public AuthorVO authorGetDetail(int authorId) throws Exception {
		logger.info("authorgetDetail ...... " + authorId);
		System.out.println("authorgetDetail ...... " + authorId);
		
		return authorMapper.authorGetDetail(authorId);
	}

	@Override
	public int authorModify(AuthorVO author) throws Exception {
		logger.info("(service) authorModify........." + author);
		return authorMapper.authorModify(author);
	}

	@Override
	public int authorDelete(int authorId) {
		logger.info("(service) authorDelete........." + authorId);
		return authorMapper.authorDelete(authorId);
	}

}

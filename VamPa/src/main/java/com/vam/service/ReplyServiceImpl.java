package com.vam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.ReplyMapper;
import com.vam.model.Criteria;
import com.vam.model.PageDTO;
import com.vam.model.ReplyDTO;
import com.vam.model.ReplyPageDTO;
import com.vam.model.UpdateReplyDTO;

@Service
public class ReplyServiceImpl implements ReplyService{

	@Autowired
	private ReplyMapper replyMapper;
	
	
	// 댓글 등록
	@Override
	public int enrollReply(ReplyDTO dto) {
			
		int result = replyMapper.enrollReply(dto);
		
		setRating(dto.getBookId());
		
		
		return result;
	}


	@Override
	public String checkReply(ReplyDTO dto) {

		Integer result = replyMapper.checkReply(dto);
		System.out.println("(serviceImpl)result : " + result);
		
		if (result == null) {
			return "0";
			
		} else {
			return "1";
		}
		
	}


	@Override
	public ReplyPageDTO replyList(Criteria cri) {

		ReplyPageDTO dto = new ReplyPageDTO();
		
		dto.setList(replyMapper.getReplyList(cri));
		dto.setPageInfo(new PageDTO(cri, replyMapper.getReplyTotal(cri.getBookId())));
		
		System.out.println("dto : " + dto);
		
		return dto;
	}


	@Override
	public int updateReply(ReplyDTO dto) {
		int result = replyMapper.updateReply(dto);
		
		setRating(dto.getBookId());
		
		return result;
	}


	@Override
	public ReplyDTO getUpdateReply(int replyId) {

		return replyMapper.getUpdateReply(replyId);
	}


	@Override
	public int deleteReply(ReplyDTO dto) {
		
		int result = replyMapper.deleteReply(dto.getReplyId());
		System.out.println("result : " + result);
		setRating(dto.getBookId());
		
		
		return result;
	}

	
	// 평균 평점 반영 (댓글 등록, 수정, 삭제 에서 사용)
	public void setRating(int bookId) {
		Double ratingAvg = replyMapper.getRatingAverage(bookId);
		
		if(ratingAvg == null) {
			ratingAvg = 0.0;
		}
		System.out.println("소숫점 첫째자리 전 ratingAvg : " +  ratingAvg);
		ratingAvg = (double)(Math.round(ratingAvg*10));
		
		ratingAvg = ratingAvg / 10;
		System.out.println("소숫점 첫째자리 후 ratingAvg : " +  ratingAvg);
		
		
		UpdateReplyDTO urd = new UpdateReplyDTO();
		urd.setBookId(bookId);
		urd.setRatingAvg(ratingAvg);
		
		System.out.println("urd : " + urd);
		
		replyMapper.updateRating(urd);
	}
	
	
	
}

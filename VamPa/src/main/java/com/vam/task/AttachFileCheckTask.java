package com.vam.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vam.mapper.AdminMapper;
import com.vam.model.AttachImageVO;


@Component
public class AttachFileCheckTask {
	private static final Logger logger = LoggerFactory.getLogger(AttachFileCheckTask.class);
	
	@Autowired
	private AdminMapper mapper;
	
	
	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		System.out.println("cal : " + cal);
		
		cal.add(Calendar.DATE, -1);
		
		String str = sdf.format(cal.getTime());
		System.out.println("str : " + str);
		
		return str.replace("-", File.separator); // 파일 구분자 \
		
	}
	
	
	@Scheduled(cron = "0 0 1 * * *") // 새벽 1시마다 배치
//	@Scheduled(cron = "0 * * * * *") // 1분마다 배치
	public void checkFiles() throws Exception {
		logger.warn("File Check Task Run .............");
		logger.warn("날짜 : ", new Date());
		logger.warn("=======================================");
		
		// DB에 저장된 파일 리스트
		List<AttachImageVO> fileList = mapper.checkFileList(); 
		
		// 비교 기준 파일 리스트(Path 객체)
		List<Path> checkFilePath = new ArrayList<Path>();
		
		// 원본 이미지
		for (AttachImageVO vo : fileList) {
			Path path = Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" +  vo.getFileName());
			checkFilePath.add(path);
		}
		// 썸네일 이미지
		for (AttachImageVO vo : fileList) {
			Path path = Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" +  vo.getFileName());
			checkFilePath.add(path);
		}
		
		// 디렉토리 파일 리스트
		File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();
		System.out.println("targetDir : " + targetDir);
		File[] targetFile = targetDir.listFiles();
		System.out.println("targetFile : " + targetFile);
		
		// 삭제 대상 파일 리스트(분류) 골라내기
		List<File> removeFileList = new ArrayList<File>(Arrays.asList(targetFile));
		System.out.println("removeFileList : " + removeFileList);
		for (File file : targetFile) {
			for (Path path : checkFilePath) {
				if(file.toPath().equals(path)) {
					removeFileList.remove(file);
				}
			}
		}
		
		// 삭제 대상 파일 제거
		logger.warn("file Delete : ");
		
		for (File file : removeFileList) {
			logger.warn("file : " + file);
			file.delete();
		}
		
		logger.warn("==================================");
		
		
	}	
	
}

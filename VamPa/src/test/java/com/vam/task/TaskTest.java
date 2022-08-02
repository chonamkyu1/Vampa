package com.vam.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.mapper.AdminMapper;
import com.vam.model.AttachImageVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class TaskTest {

	
	@Autowired
	private AdminMapper mapper;

	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		System.out.println("cal : " + cal);
		System.out.println("cal.getTime() : " + cal.getTime());
		
		String str = sdf.format(cal.getTime());
		
		System.out.println("str : " + str);
		
		return str.replace("-", File.separator);
		
		
	}
	
	
	
	
	@Test
	public void taskTest() {
		List<AttachImageVO> fileList =  mapper.checkFileList();
		System.out.println("fileList : ");
		
		
		// 향상된 for문
		for (AttachImageVO list :  fileList ) {
			System.out.println("list : " + list);
		}
		
		// 람다 for문
//		fileList.forEach(list -> System.out.println("list : " + list));
		
		List<Path> checkFilePath = new ArrayList<Path>();	// 비교를 위해 각 리스트의 파일 정보드를 path 객체로 변환
		
		// 향상된 for문
 		for (AttachImageVO vo : fileList) {
 			Path path = Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName());
 			checkFilePath.add(path);
 		}
 		
 		System.out.println("ckeckFilePath : ");
 		
 		for (Path list : checkFilePath ) {
 			System.out.println("list : " + list);
 		}
 		
 		// 람다
// 		checkFilePath.forEach(list -> System.out.println("list : " + list));
 		System.out.println("========================================");
 		
 		
 		// 향상된 for문 , 썸네일
 		for (AttachImageVO vo : fileList) {
 			Path path = Paths.get("C:\\upload", vo.getUploadPath(), "s_" +  vo.getUuid() + "_" + vo.getFileName());
 			checkFilePath.add(path);
 		}
 		
 		System.out.println("checkFilePath(썸네일 이미지 정보 추가 후 ) : ");
 		for (Path list : checkFilePath ) {
 			System.out.println("list : " + list);
 		}
 		System.out.println("========================================");
 		
 		// 내 디렉토리에 저장된 파일들
 		File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();
 		
 		System.out.println("targetDir ~~ : " + targetDir);
 		
 		File[] targetFile = targetDir.listFiles();
 		
 		System.out.println("targetFile : ");
 		for (File file : targetFile) {
 			System.out.println("file : " + file);
 		}
 		
 		System.out.println("========================================");
 		
 		
 		// 제거해야할 리스트 !
 		List<File> removeFileList = new ArrayList<File>(Arrays.asList(targetFile));
 		
 		System.out.println("removeFileList(필터 전) : ");
 		
 		for(File file : removeFileList) {
 			System.out.println("file : " + file);
 		}
 		
 		System.out.println("========================================");
 		
 		for (File file : targetFile) {
 			for (Path list :  checkFilePath) {
 				if(file.toPath().equals(list)) {
 					removeFileList.remove(file);
 				}
 			}
 		}
 		
 		System.out.println("removeFileList(필터 후) : ");

 		for(File file : removeFileList) {
 			System.out.println("file : " + file);
 		}
 		System.out.println("========================================");
 		
 		// 파일 삭제
 		for (File file : removeFileList) {
 			System.out.println("삭제 : " + file);
 			file.delete();
 		}
 		
 		
 		
	}
}

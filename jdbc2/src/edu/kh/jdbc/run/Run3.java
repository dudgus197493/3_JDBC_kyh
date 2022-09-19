package edu.kh.jdbc.run;

import java.util.Scanner;

import edu.kh.jdbc.model.service.TestService;
import edu.kh.jdbc.model.vo.TestVO;

public class Run3 {

	public static void main(String[] args) {
		// 번호, 제목, 내용을 입력받아
		// 번호가 일치하는 행의 제목, 내용 수정
		
		// 수정 성공 시 -> 수정되었습니다.
		// 수정 실패 시 -> 일치하는 번호가 없습니다.
		// 예외 발생 시 -> 수정 중 예외가 발생했습니다.

		TestService service = new TestService();
		
		
		Scanner sc = new Scanner(System.in);							// 번호, 제목 입력 받기
		System.out.print("행 번호 : ");
		int testNo = sc.nextInt();
		sc.nextLine();
		System.out.print("수정할 제목 : ");
		String testTitle = sc.nextLine();
		
		System.out.print("수정할 내용 : ");
		String testContent = sc.nextLine();
		
		try {
			int result = service.update(testNo, testTitle, testContent);	// 번호가 일치하는 행의 제목, 내용 수정
			
			if(result > 0) {
				System.out.println("수정되었습니다.");
			} else {
				System.out.println("일치하는 번호가 없습니다.");
			}
		} catch(Exception e) {												// 예외 발생 시
			System.out.println("수정 중 예외가 발생했습니다.");
			e.printStackTrace();
		}
	}
}

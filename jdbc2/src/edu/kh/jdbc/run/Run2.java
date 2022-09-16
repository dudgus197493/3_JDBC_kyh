package edu.kh.jdbc.run;

import edu.kh.jdbc.model.service.TestService;
import edu.kh.jdbc.model.vo.TestVO;

public class Run2 {

	public static void main(String[] args) {
		TestService service = new TestService();
		// TB_TEST 테이블에 한 번에 3행 삽입 (INSERT 3번)
		
		TestVO vo1 = new TestVO(40, "제목40", "내용40 입니다.");
		TestVO vo2 = new TestVO(50, "제목50", "내용50 입니다.");
		TestVO vo3 = new TestVO(60, "제목60", null);
		
		try {
			int result = service.insert(vo1, vo2, vo3);
			
			if(result > 0 ) {
				System.out.println("삽입 성공");
			} else {
				System.out.println("실패..."); // 실패하는 경우 => 예외는 발생하지 않았지만 원하는 결과가 아닌경우
											  //               제약조건 위배 : 예외
											  //               1행 INSERT 했지만 0행 INSERT되는 경우 : 실패
			}
		}catch(Exception e) {
			// service, dao 수행 중 발생한 예외를 처리
			System.out.println("SQL 수행 중 오류 발생");
			e.printStackTrace();
		}
	}
}

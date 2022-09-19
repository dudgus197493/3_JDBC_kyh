package edu.kh.jdbc.main.run;

import edu.kh.jdbc.main.view.MainView;

// 실행용 클래스
public class MainRun {

	public static void main(String[] args) {
		new MainView().mainMenu();
		
		// System.exit(0); // JVM 종료, 매개변수는 0, 0이 아니면 비정상 종료
						   // main메서드 내부에 존재(컴파일러가 자동 추가)
	}

}

package edu.kh.jdbc.member.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.main.view.MainView;
import edu.kh.jdbc.member.model.service.MemberService;
import edu.kh.jdbc.member.vo.Member;

/** 회원 메뉴 입/출력 클래스
 * @author Tonic
 *
 */
public class MemberView {
	private Scanner sc = new Scanner(System.in);
	
	// 회원 관련 서비스를 제공하는 객체 생성
	private MemberService service = new MemberService();
	
	// 로그인 회원 정보 
	private Member loginMember;
	
	// 메뉴 번호를 입력 받기 위한 변수
	private int input = -1;
	
	/** 회원 기능 메뉴
	 * @param loginMember(로그인된 회원 정보) 
	 */
	public void memberMenu(Member loginMember) {
//		int input = -1; // 필드로 이동
		
		// 전달 받은 로그인 회원 정보 필드에 저장
		this.loginMember = loginMember;
		
		
		 /* 1. 내 정보 조회
		 * 2. 회원 목록 조회(아이디, 이름, 성별)
		 * 3. 내 정보 수정(이름, 성별)
		 * 4. 비밀번호변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인)
		 * 5. 회원 탈퇴 */
		do {
			try {
				System.out.println("\n***** 회원 기능 *****\n");
				System.out.println("1. 내 정보 조회");
				System.out.println("2. 회원 목록 조회(아이디, 이름, 성별)");
				System.out.println("3. 내 정보 수정(이름, 성별)");
				System.out.println("4. 비밀번호 변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인)");	// 1. 한번에 받기
																						// 2. 비밀번호 일치 선확인 후 변경할 비밀번호 받기
				System.out.println("5. 회원 탈퇴");
				System.out.println("0. 메인메뉴로 이동");
				
				System.out.print("\n메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine();
				
				switch(input) {
				case 1: selectMyInfo(); break;
				case 2: selectAll(); break;
				case 3: updateMember(); break;
				case 4: updatePwV2(); break;
				case 5: secession(); break;
				case 0: System.out.println("[메인 메뉴로 이동합니다.]"); break;
				default : System.out.println("메뉴에 작성된 번호만 입력해주세요.");
				}
				
			}catch(InputMismatchException e) {
				System.out.println("\n <<입력 형식이 올바르지 않습니다.>>");
			}			
		}while(input != 0);
	}
	
	/**
	 * 내 정보 조회
	 */
	private void selectMyInfo() {
		System.out.println("\n[내 정보 조회]\n");
		System.out.println("회원 번호 : " + loginMember.getMemberNo());
		System.out.println("아이디 : " + loginMember.getMemberId());
		System.out.println("이름 : " + loginMember.getMemberName());
		System.out.println("성별 : " + (loginMember.getMemberGender().equals("M") ? "남" : "여"));
		System.out.println("가입일 : " + loginMember.getEnrollDate());
	}
	
	/**
	 * 회원 목록 조회
	 */
	private void selectAll() {
		System.out.println("\n[회원 목록 조회]\n");
		
		// DB에서 회원 목록 조회(탈퇴 회원 미포함)
		// + 가입일 내림차순
		try {
			// 회원 목록 조회 서비스 호출 후 결과 반환 받기
			List<Member> memberList = service.selectAll();
			
			System.out.println("   아이디    |  이름  | 성별 " );
			System.out.println("----------------------------------");
			if(memberList.isEmpty()) {			// 없으면 "조회 결과가 없습니다." 출력
				System.out.println("[조회 결과가 없습니다.]");
			} else {							// 조회 결과가 있으면 모두 출력
				for(Member mem : memberList) {
					 System.out.printf(" %7s  | %4s | %s \n", 
							 mem.getMemberId(), mem.getMemberName(), mem.getMemberGender());
				}
			} 
		}catch(Exception e) {
			System.out.println("\n<< 회원 목록 조회 중 예외 발생 >>\n");
			e.printStackTrace();
		}
	}
	
	private void updateMember() {
		try {
			System.out.println("\n[회원 정보 수정]\n");
			
			System.out.print("변경할 이름 : ");
			String memberName = sc.next();
			
			String memberGender = null;
			while(true) {
				System.out.print("변경할 성별( M / F ) : ");
				memberGender = sc.next().toUpperCase();
				if(memberGender.equals("M") || memberGender.equals("F")) {
					break;
				} else {
					System.out.println("M 또는 F만 입력해주세요.");
				}
			}
			
			// 서비스로 전달할 Member 객체 생성
			Member member = new Member();
			member.setMemberNo(loginMember.getMemberNo());
			member.setMemberName(memberName);
			member.setMemberGender(memberGender);
			
			// 회원 정보 수정 서비스 메서드 호출 후 결과 반환 받기
			int result = service.updateMember(member);
			
			if(result > 0) {
				// loginMember에 저장된 값과
				// DB에 수정된 값을 동기화 하는 작업 필요
				loginMember.setMemberName(memberName);
				loginMember.setMemberGender(memberGender);
				
				System.out.println("\n[회원 정보가 수정되었습니다]\n");
			} else {
				System.out.println("\n[수정 실패]\n");
			}
		}catch(Exception e) {
			System.out.println("\n<< 회원 정보 수정 중 예외 발생 >>\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * 비밀번호 변경
	 */
	public void updatePw() {
		System.out.println("\n[비밀번호 변경]\n");
		try {	
			System.out.print("현재 비밀번호 : ");
			String currentPw = sc.next();
			
			String newPw1 = null;
			String newPw2 = null;
			while(true) {
				System.out.print("새 비밀번호 : ");
				newPw1 = sc.next();
				
				System.out.print("새 비밀번호 확인 : ");
				newPw2 = sc.next();
				
				if(newPw1.equals(newPw2)) break;
				else {
					System.out.println("\n새 비밀번호가 일치하지 않습니다. 다시 입력해주세요.\n");
				}
			} // while end
			
			// 서비스 호출 후 결과 반환 받기
			int result = service.updatePw(currentPw, newPw1, loginMember.getMemberNo()); 
										// 현재 비밀번호, 새 비밀번호, 로그인한 회원 번호
			if(result > 0) {
				System.out.println("\n[비밀번호가 변경되었습니다.]\n");
			} else {
				System.out.println("\n[현재 비밀번호가 일치하지 않습니다.]\n");
			}
		} catch (Exception e){
			System.out.println("\n<< 비밀번호 변경 중 예외 발생 >>\n");
		}
	}
	
	/**
	 * 비밀번호 변경 (비밀번호 확인 )
	 */
	public void updatePwV2() {
		System.out.println("\n[비밀번호 변경]\n");
		try {
			// 비밀번호 확인
			String currentPw = null;
			// 비밀번호 입력 (while, true)
			while(true) {
				System.out.print("비밀번호 인증 (이전화면 [0] ) : ");
				currentPw = sc.next();
				if(currentPw.equals("0")) return;
				
				// service 호출 후 결과 받기
				int result = service.checkPw(currentPw, loginMember.getMemberNo());
				
				// result 가 0 이상이면 break; 
				if(result > 0) {
					System.out.println("\n[비밀번호 인증 완료]\n");
					break; 
				} else {
					System.out.println("\n[현재 비밀번호가 일치하지 않습니다.]\n");
				}
			}

			String newPw1 = null;
			String newPw2 = null;
			
			while(true) {
				// 새 비밀번호 입력
				System.out.print("새 비밀번호 : ");
				newPw1 = sc.next();
				// 새 배밀번호 확인 입력
				System.out.print("새 비밀번호 확인 : ");
				newPw2 = sc.next();
				// 두 값이 같으면 
				if(newPw1.equals(newPw2)) {
					System.out.println("\n[비밀번호 확인 완료]\n");
					break;
				} else {
					System.out.println("\n새 비밀번호가 일치하지 않습니다. 다시 입력해주세요.\n");
				}
			}
			// service.updatePwV2() 호출후 결과 받기
			int result = service.updatePw(currentPw, newPw1, loginMember.getMemberNo());
			// 결과가 0보다 크면 성공 출력
			if(result > 0) {
				System.out.println("\n[비밀번호가 변경되었습니다.]\n");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 회원 탈퇴
	 */
	public void secession() {
		System.out.println("\n[회원 탈퇴]\n");
		try {
			System.out.print("비밀번호 입력 : ");
			String memberPw = sc.next();
			
			while(true) {
				System.out.print("정말 탈퇴하시겠습니까? (Y / N) : ");
				char ch = sc.next().toUpperCase().charAt(0);
				
				if(ch == 'Y') {
					// 서비스 호출 후 결과 반환 받기
					int result = service.secession(memberPw, loginMember.getMemberNo());
					
					if(result > 0) {
						System.out.println("\n[탈퇴 되었습니다...]\n");
						// 로그아웃
						MainView.loginMember = null;
						
						// 메인 메뉴로 이동
						input = 0;
					} else {
						System.out.println("\n[비밀번호가 일치하지 않습니다.]\n");
					}
					break;
				} else if (ch == 'N') {
					System.out.println("\n[취소 되었습니다.]\n");
					break;
				} else {
					System.out.println("\n[Y 또는 N만 입력해주세요.]\n");
				}
			}
		} catch(Exception e) {
			System.out.println("\n<< 회원 탈퇴 중 예외 발생 >>\n");
			e.printStackTrace();
		}
	}
}

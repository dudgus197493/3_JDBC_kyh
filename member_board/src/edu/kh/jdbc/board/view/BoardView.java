package edu.kh.jdbc.board.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.board.model.service.BoardService;
import edu.kh.jdbc.board.model.service.CommentService;
import edu.kh.jdbc.board.model.vo.Board;
import edu.kh.jdbc.board.model.vo.Comment;
import edu.kh.jdbc.main.view.MainView;

public class BoardView {
	private Scanner sc = new Scanner(System.in);
	
	private BoardService bService = new BoardService();
	private CommentService cService = new CommentService();
	
	/**
	 * 게시판 기능 메뉴 화면
	 */
	public void boardMenu() {
		int input = -1;
		
		do {
			try {
				System.out.println("\n***** 게시판 메뉴 *****\n");
				System.out.println("1. 게시글 목록 조회");
				System.out.println("2. 게시글 상세 조회 (+ 댓글 기능)");
				System.out.println("3. 게시글 작성");
				System.out.println("4. 게시글 검색");
				System.out.println("0. 로그인 메뉴로 이동");
				
				System.out.print("\n메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine();
				
				System.out.println();
				switch(input) {
				case 1: selectAllBoard(); break;	// 게시글 목록 조회
				case 2: selectBoard(); break;		// 게시글 상세 조회
				case 3: break;
				case 4: break;
				case 0: System.out.println("[로그인 메뉴로 이동합니다.]"); input = 0; break;
				default: System.out.println("메뉴에 작성된 번호만 입력해주세요.");
				}
			}
			catch(InputMismatchException e) {
				System.out.println("\n <<입력 형식이 올바르지 않습니다.>>");
				sc.nextLine();
			}
		} while(input != 0);
	}
	
	/**
	 * 게시글 목록 조회
	 */
	private void selectAllBoard() {
		System.out.println("\n[게시글 목록 조회]\n");
		
		try {
			List<Board> boardList = bService.selectAllBoard();
			// DAO에서 new ArrayList<>(); 구문으로 인해 반환되느 조회 결과는 NULL이 될 수 없다.
			
			
			if(boardList.isEmpty()) {		// 조회 결과 없을 경우.
				System.out.println("\n[조회된 게시글이 없습니다.]\n");
			} else {						// 조회 결과 있을 경우
				System.out.println("글번호 |     글 제목     | 작성자 |    작성일    | 댓글수");
				System.out.println("-------------------------------------------------");
				for(Board b : boardList) {
					System.out.printf("  %d | %-10s[%d] | %-4s | %9s | %d\n", 
							b.getBoardNo(), b.getBoardTitle(), b.getCommentCount(), b.getMemberName(), b.getCreateDate(), b.getReadCount());
				}
			}
		}catch(Exception e) {
			System.out.println("\n<< 게시글 목록 조회 중 예외 발생 >>\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * 게시글 상세 조회
	 */
	private void selectBoard() {
		System.out.println("\n[게시글 상세 조회]\n");
		
		try {
			System.out.print("게시글 번호 입력 : ");
			int boardNo = sc.nextInt();
			sc.nextLine();
			
			// 게시글 상세 조회 서비스 호출 후 결과 반환 받기
			Board board = bService.selectBoard(boardNo, MainView.loginMember.getMemberNo());
																// 자신의 글 조회수 증가 
	         if (board != null) {
	            System.out.println(" --------------------------------------------------------");
	            System.out.printf("글번호 : %d | 제목 : %s\n", board.getBoardNo(), board.getBoardTitle());
	            System.out.printf("작성자ID : %s | 작성일 : %s | 조회수 : %d\n", 
	                  board.getMemberName(), board.getCreateDate().toString(), board.getReadCount());
	            System.out.println(" --------------------------------------------------------");
	            System.out.println(board.getBoardContent());
	            System.out.println(" --------------------------------------------------------");
	         
	            // 댓글 목록
	            if(!board.getCommentList().isEmpty()) {
	               for(Comment c : board.getCommentList()) {
	                  System.out.printf("댓글번호: %d   작성자: %s  작성일: %s\n%s\n",
	                        c.getCommentNo(), c.getMemberName(), c.getCreateDate(), c.getCommentContent());
	                  System.out.println(" --------------------------------------------------------");
	               }
	            }
	            // 댓글 등록, 수정, 삭제
	            // 수정/삭제 메뉴
//	            subBoardMenu(board);
	         } else {
	            System.out.println("해당 번호의 게시글이 존재하지 않습니다.");
	         }
			
		}catch(Exception e) {
			System.out.println("\n<< 게시글 상세 조회 중 예외 발생>>\n");
			e.printStackTrace();
		}
	}
}

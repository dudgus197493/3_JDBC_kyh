package edu.kh.jdbc.member.model.service;

import static edu.kh.jdbc.common.JDBCTemplate.close;
import static edu.kh.jdbc.common.JDBCTemplate.getConnection;
import static edu.kh.jdbc.common.JDBCTemplate.commit;
import static edu.kh.jdbc.common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.member.model.dao.MemberDAO;
import edu.kh.jdbc.member.vo.Member;

public class MemberService {
	private MemberDAO dao = new MemberDAO();

	/** 회원 목록 조회 서비스
	 * @return memberList
	 * @throws Exception
	 */
	public List<Member> selectAll() throws Exception {
		Connection conn = getConnection(); // 커넥션 생성
		
		List<Member> memberList = dao.selectAll(conn);
		
		close(conn);
		
		return memberList;
	}

	/** 회원 정보 수정 서비스
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int updateMember(Member member) throws Exception{
		// 커넥션 생성
		Connection conn = getConnection();
		
		// dao 호출 후 결과 반환
		int result = dao.updateMember(conn, member);
				
		// 트랜잭션 제어
		if(result > 0) commit(conn);
		else		   rollback(conn);
		
		// 커넥션 반환
		close(conn);
		
		// 결과 반환
		return result;
	}

	/** 비밀번호 변경 서비스
	 * @param currentPw
	 * @param newPw1
	 * @param memberNo
	 * @return result
	 * @throws Exception
	 */
	public int updatePw(String currentPw, String newPw1, int memberNo) throws Exception{
		// 커넥션 생성
		Connection conn = getConnection();
		
		// dao 호출 후 결과 반환
		int result = dao.updatePw(conn, currentPw, newPw1, memberNo);
		
		// 트랜잭션 제어
		if(result > 0) commit(conn);
		else		   rollback(conn);
		
		// 커넥션 반환
		close(conn);
		
		// 결과 반환
		return result;
	}

	/** 회원 탈퇴 서비스
	 * @param memberPw
	 * @param memberId
	 * @return result
	 * @throws Exception
	 */
	public int secession(String memberPw, int memberNo) throws Exception{
		// 커넥션 생성
		Connection conn = getConnection();
		
		// dao 호출 후 결과 반환
		int result = dao.secession(conn, memberPw, memberNo);
		
		// 트랜잭션 제어
		if(result > 0) commit(conn);
		else		   rollback(conn);
		
		// 커넥션 반환
		close(conn);
		
		// 결과 반환
		return result;
	}

	/** 비밀번호 인증 서비스
	 * @param currentPw
	 * @param memberNo
	 * @return result
	 * @throws Exception
	 */
	public int checkPw(String currentPw, int memberNo) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.checkPw(conn, currentPw, memberNo);
		
		close(conn);
		
		return result;
	}
}

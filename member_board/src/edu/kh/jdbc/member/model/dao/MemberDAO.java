package edu.kh.jdbc.member.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.kh.jdbc.member.vo.Member;

public class MemberDAO {
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties prop;
	
	public MemberDAO() { 
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("member-query.xml"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** 회원 목록 조회 DAO
	 * @param conn
	 * @return memberList
	 * @throws Exception
	 */
	public List<Member> selectAll(Connection conn) throws Exception{
		// 결과 저장용 변수 선언
		List<Member> memberList = new ArrayList<>();
		
		try {
			// SQL 가져오기
			String sql = prop.getProperty("selectAll");
			
			// Statement 생성
			Statement stmt = conn.createStatement();
			
			// SQL 실행 후 결과 반환
			rs = stmt.executeQuery(sql);
			
			// 반복문(while)을 이용해 조회 결과의 각 행에 접근 후
			// 컬럼 갑을 얻어와 Membmer 객체에 저장 후 List에 추가
			while(rs.next()) {
//				memberList.add(new Member(
//						rs.getString("MEMBER_ID"),
//						rs.getString("MEMBER_NM"),
//						rs.getString("MEMBER_GENDER")
//				)); --> 매개변수 생성자가 여기서 밖에 쓰이지 않는다면 더 비효율적이다.
//					--> 아래 방식을 더 많이 씀					
				Member member = new Member();
				member.setMemberId(rs.getString("MEMBER_ID"));
				member.setMemberName(rs.getString("MEMBER_NM"));
				member.setMemberGender(rs.getString("MEMBER_GENDER"));
				memberList.add(member);
			}
			
		}finally {
			// JDBC 객체 자원 반환
			close(rs);
			close(stmt);
		}
		
		// 조회 결과 옮겨 담은 List 반환
		return memberList;
	}

	public int updateMember(Connection conn, Member member) throws Exception{
		// 결과용 변수 생성
		int result = 0;				// UPDATE 반영 결과 행의 개수(정수형)를 저장하기 위한 변수
		
		try {
			// SQL 가져오기
			String sql = prop.getProperty("updateMember");
			
			// PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// ?에 값 설정
			pstmt.setString(1, member.getMemberName());
			pstmt.setString(2, member.getMemberGender());
			pstmt.setInt(3, member.getMemberNo());
			
			// SQL 수행 후 결과 반환
			result = pstmt.executeUpdate();
		}finally {
			// JDBC 자원 반환
			close(pstmt);
		}
		
		// 결과 반환
		return result;
	}

	/** 비밀번호 변경 DAO
	 * @param conn
	 * @param currentPw
	 * @param newPw1
	 * @param memberNo
	 * @return result
	 * @throws Exception
	 */
	public int updatePw(Connection conn, String currentPw, String newPw, int memberNo) throws Exception{
		// 결과용 변수 생성
		int result = 0;				// UPDATE 반영 결과 행의 개수(정수형)를 저장하기 위한 변수
		
		try {
			// SQL 가져오기
			String sql = prop.getProperty("updatePw");
			
			// PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// ?에 값 설정
			pstmt.setString(1, newPw);
			pstmt.setInt(2, memberNo);
			pstmt.setString(3, currentPw);
			
			// SQL 수행 후 결과 반환
			result = pstmt.executeUpdate();
		}finally {
			// JDBC 자원 반환
			close(pstmt);
		}
		
		// 결과 반환
		return result;
	}

	/** 회원 탈퇴 DAO
	 * @param conn
	 * @param memberPw
	 * @param memberId
	 * @return result
	 * @throws Exception
	 */
	public int secession(Connection conn, String memberPw, int memberNo) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("secession");
			
			pstmt = conn.prepareStatement(sql);
					
			pstmt.setInt(1, memberNo);
			pstmt.setString(2, memberPw);
			
			result = pstmt.executeUpdate();
		}finally {
			close(pstmt);
		}
		return result;
	}

	/** 비밀번호 인증 DAO
	 * @param conn
	 * @param currentPw
	 * @param memberNo
	 * @return result
	 * @throws Exception
	 */
	public int checkPw(Connection conn, String currentPw, int memberNo) throws Exception{
		int result = 0;
		
		try {
			// sql 가져오기
			String sql = prop.getProperty("checkPw");
			// PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			// ? 값 세팅
			pstmt.setInt(1, memberNo);
			pstmt.setString(2, currentPw);
			// SQL 수행 후 결과 반환
			rs = pstmt.executeQuery();
			// result에 결과 담기
			if(rs.next()) {
				result = rs.getInt("CHECK");
			}
		}finally {
			// JDBC 자원 반환
			close(rs);
			close(pstmt);
		}
		// 결과 반환
		return result;
	}
	
	
	
}

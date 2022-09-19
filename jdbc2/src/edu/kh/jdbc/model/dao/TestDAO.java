package edu.kh.jdbc.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import edu.kh.jdbc.model.vo.TestVO;

// DAO(Data Access Object) : 데이터가 저장된 DB에 접근하는 객체
//						-> SQL 수행, 결과 반환 받는 기능을 수행
public class TestDAO {
	
	// JDBC 객체를 참조하기 위한 참조변수 선언
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private Properties prop;
	
	// 기본 생성자
	public TestDAO() {
		
		// TestDAO 객체 생성 시
		// test-query.xml 파일의 내용을 읽어와
		// Properties 객체에 저장
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("test-query.xml"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// XML로 SQL을 다룰 예정 -> Properties 객체 사용
	public int insert(Connection conn, TestVO vo) throws SQLException{
		// throws SQLException
		// -> 호출한 곳으로 발생한 SQLException을 던짐
		// -> 예외 처리를 모아서 진행하기 위해서
		
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		/* 예외를 throws로 던져도 try ~ finally를 쓰는 이유
		 * 
		 * try문이 없을 경우 예외가 생기면 아래 코드를 수행하지 않고 종료 -> pstmt가 닫히지 않음
		 * finally 구문을 사용하여 어떤 상황에서도 무조건 pstmt.close()가 실행될 수 있게 코드 작성.
		 */
		
		
		try {
			// 2. SQL 작성(test-query.xml에 작성된 SQL 얻어오기) --> prop이 저장하고 있음
			String SQL = prop.getProperty("insert");
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(SQL);
			// -> throws 예외 처리 사용
			
			// 4. ?(위치홀더)에 알맞은 값 세팅
			pstmt.setInt(1, vo.getTestNo());
			pstmt.setString(2, vo.getTestTitle());
			pstmt.setString(3, vo.getTestContent());
			
			result = pstmt.executeUpdate();
			
		} finally {		
			// 6. 사용한 JDBC 객체 자원 반환( close() )
			close(pstmt);
		}
		
		// 7. SQL 수행 결과 반환
		return result;
	}

	/** 입력 받은 번호의 제목, 내용 수정 DAO
	 * @param conn
	 * @param testNo
	 * @param updateTitle
	 * @param updateContent
	 * @return SQL 수행 후 수정된 행의 갯수
	 * @throws SQLException
	 */
	public int update(Connection conn, int testNo, String updateTitle, String updateContent) throws SQLException{
		String SQL = prop.getProperty("update");	// SQL 가져옴
		int result = 0;								// 반환 결과
		try {
			pstmt = conn.prepareStatement(SQL);		// PreparedStatement 생성
			pstmt.setString(1, updateTitle);		
			pstmt.setString(2, updateContent);
			pstmt.setInt(3, testNo);				// pstmt 세팅
			
			result = pstmt.executeUpdate();			// SQL 수행 후 실행 결과 반환
		} finally {
			close(pstmt);
		}
		return result;
	}
}

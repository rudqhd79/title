package DAO;

import java.sql.*;
import java.util.*;

import DTO.Board;

public class BoardDAO {
	final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";

	// DB와 연결 수행 메소드
	public Connection open() throws Exception {
		Connection conn = null;
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(JDBC_URL, "test", "test1234");

		return conn;
	}

	// 게시판 리스트 가져오기
	public ArrayList<Board> getList() throws Exception {
		Connection conn = open();
		ArrayList<Board> boardList = new ArrayList<>(); // 데이터 저장할 배열
		String sql = "select board_no, title, user_id, to_char(reg_date, 'yyyy.mm.dd') reg_date, views, content from board";
		PreparedStatement ps = conn.prepareStatement(sql); // 쿼리문 등록 -> 컴파일
		ResultSet rs = ps.executeQuery(); // 쿼리문 실행 -> 데이터 베이스 결과 저장

		// 리소스 자동 닫기(try-with-resource)
		// try 괄호안에 변수명을 적으면 알아서 적용이 된다
		try (conn; ps; rs) {
			while (rs.next()) {
				Board b = new Board();
				b.setBoard_no(rs.getInt(1));
				b.setTitle(rs.getString(2));
				b.setUser_id(rs.getString(3));
				b.setReg_date(rs.getString(4));
				b.setViews(rs.getInt(5));

				boardList.add(b);
			}
			// 메소드 타입 자체가 List이기 때문에 리턴도 List타입으로 해준다
			return boardList;
		}
	}

	public Board getView(int board_no) throws Exception {
		Connection conn = open();
		Board b = new Board();
		String sql = "select board_no, title, user_id, to_char(reg_date, 'yyyy.mm.dd') reg_date, views, content from board where board_no = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, board_no); // ?의 1번 값은 board_no이 된다
		ResultSet rs = ps.executeQuery();

		try (conn; rs; ps) {
			while (rs.next()) {
				b.setBoard_no(rs.getInt(1));
				b.setTitle(rs.getString(2));
				b.setUser_id(rs.getString(3));
				b.setReg_date(rs.getString(4));
				b.setViews(rs.getInt(5));
				b.setContent(rs.getString(6));
			}
		}
		return b;
	}

	//클릭하면 조회수 1씩 증가하는 메소드
	public void updateViews(int board_no) throws Exception {
		Connection conn = open();

		String sql = "update board set views = (views + 1) where board_no = ?";
		PreparedStatement ps = conn.prepareStatement(sql); // 쿼리문 등록 -> 컴파일

		try (conn; ps;) {
			ps.setInt(1, board_no);
			ps.executeUpdate();
		}
	}
	
	public void insertBoard(Board b) throws Exception {
		
		Connection conn = open();
		String sql = "insert into board (board_no, user_id, title, content, reg_date, views)";
		//(1씩 증가 시퀀스, ?, ?, ?, 오늘 날짜, 조회수 시작점 0)
		sql += " values(board_seq.nextval, ?, ?, ?, sysdate, 0)";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		try (conn; ps) {
			ps.setString(1, b.getUser_id());
			ps.setString(2, b.getTitle());
			ps.setString(3, b.getContent());
			ps.executeUpdate();
		}
	}
}

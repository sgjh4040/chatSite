package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import user.UserDTO;

public class BoardDAO {
	DataSource dataSource;
	
	public BoardDAO() {
		try {
			InitialContext initContext = new InitialContext();
			//context만들어서 DB에 접근할수 있도록 만들어준다
			Context envContext =(Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/UserChat");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//회원 가입
	public int write(String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "insert into Board Select ?, IFNULL((select MAX(boardID) +1 From Board),1), ? , ? ,now(), 0, ?, ?, IFNULL((Select max(boardGroup) + 1 From board), 0), 0, 0";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, boardTitle);
			pstmt.setString(3, boardContent);
			pstmt.setString(4, boardFile);
			pstmt.setString(5, boardRealFile);
			return pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; //데이터 베이스 오류
		
	}
	
	//boardID 의 정보를 뽑아내는 메소드
	public BoardDTO getBoard(String boardID) {
		BoardDTO board = new BoardDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "select * from Board where boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				board.setUserID(rs.getString("userID"));
				board.setBoardID(rs.getInt("boardID"));
				board.setBoardTitle(rs.getString("boardTitle").replaceAll(" ", "nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				board.setBoardContent(rs.getString("boardContent").replaceAll(" ", "nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				board.setBoardDate(rs.getString("boardDate").substring(0,11));
				board.setBoardHit(rs.getInt("boardHit"));
				board.setBoardfile(rs.getString("boardFile"));
				board.setBoardRealFile(rs.getString("boardRealFile"));
				board.setBoardGroup(rs.getInt("boardGroup"));
				board.setBoardSequence(rs.getInt("boardSequence"));
				board.setBoardLevel(rs.getInt("boardLevel"));
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return board; //데이터 베이스 오류
		
	}
	//boardID 의 정보를 뽑아내는 메소드
		public ArrayList<BoardDTO> getList() {
			ArrayList<BoardDTO> boardList = null;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "select * from Board ORDER By boardGroup DESC, boardSequence ASC";
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				rs = pstmt.executeQuery();
				boardList = new ArrayList<BoardDTO>(); 
				while(rs.next()) {
					BoardDTO board = new BoardDTO();
					board.setUserID(rs.getString("userID"));
					board.setBoardID(rs.getInt("boardID"));
					board.setBoardTitle(rs.getString("boardTitle").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
					board.setBoardContent(rs.getString("boardContent").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
					board.setBoardDate(rs.getString("boardDate").substring(0,11));
					board.setBoardHit(rs.getInt("boardHit"));
					board.setBoardfile(rs.getString("boardFile"));
					board.setBoardRealFile(rs.getString("boardRealFile"));
					board.setBoardGroup(rs.getInt("boardGroup"));
					board.setBoardSequence(rs.getInt("boardSequence"));
					board.setBoardLevel(rs.getInt("boardLevel"));
					
					boardList.add(board);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return boardList; //데이터 베이스 오류
			
		}
		//게시물 카운드 업
		public int hit(String boardID) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			String SQL = "Update board set boardhit = boardhit+1 where boardID = ?";
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, boardID);
				return pstmt.executeUpdate();
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return -1; //데이터 베이스 오류
			
		}
		
		public String getFile(String boardID) {
			BoardDTO board = new BoardDTO();
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "Select boardFile FROM board where boardID = ?";
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, boardID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getString("boardFile");
					
				}
				return "";
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return ""; //데이터 베이스 오류
		}
		public String getRealFile(String boardID) {
			BoardDTO board = new BoardDTO();
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "Select boardRealFile FROM board where boardID = ?";
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, boardID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getString("boardRealFile");
					
				}
				return "";
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return ""; //데이터 베이스 오류
		}
		//board 수정
		public int update(String boardID, String boardTitle, String boardContent, String boardFile, String boardRealFile) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			String SQL = "update board set boardTitle = ?, boardContent=?, boardFile = ? , boardRealFile= ? where boardID= ?";
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, boardTitle);
				pstmt.setString(2, boardContent);
				pstmt.setString(3, boardFile);
				pstmt.setString(4, boardRealFile);
				pstmt.setInt(5, Integer.parseInt(boardID));
				return pstmt.executeUpdate();
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return -1; //데이터 베이스 오류
			
		}
		
		//글 삭제 메소드
		public int delete(String boardID) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			String SQL = "delete from board where boardID =?";
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, boardID);
				return pstmt.executeUpdate();
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return -1; //데이터 베이스 오류
			
		}
		
		//답변 기능
		public int reply(String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile, BoardDTO parent) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			String SQL = "insert into Board Select ?, IFNULL((select MAX(boardID) +1 From Board),1), ? , ? ,now(), 0 , ? , ? , ? , ? , ?";
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				pstmt.setString(2, boardTitle);
				pstmt.setString(3, boardContent);
				pstmt.setString(4, boardFile);
				pstmt.setString(5, boardRealFile);
				pstmt.setInt(6, parent.getBoardGroup());
				pstmt.setInt(7, parent.getBoardSequence()+1);
				pstmt.setInt(8, parent.getBoardLevel()+1);
				
				return pstmt.executeUpdate();
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return -1; //데이터 베이스 오류
			
		}
		//부몰
		public int replyUpdate(BoardDTO parent) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			String SQL = "update board set boardSequence = boardSequence+1 where boardGroup = ? AND boardSequence >?";
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, parent.getBoardGroup());
				pstmt.setInt(2, parent.getBoardSequence());
				
				return pstmt.executeUpdate();
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return -1; //데이터 베이스 오류
			
		}


}

package user;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

/**
 * Servlet implementation class UserProfileServlet
 */
@WebServlet("/UserProfileServlet")
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		MultipartRequest multi = null;
		int fileMaxSize= 10* 1024 * 1024;
		String savePath = request.getRealPath("/upload").replace("\\\\", "/");
		try {
			multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8",new DefaultFileRenamePolicy());
		}catch(Exception e) {
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "파일크기는 10MB를 넘을수 없습니다.");
			response.sendRedirect("profileUpdate.jsp");
			return;
		}
		String userID = multi.getParameter("userID");
		HttpSession session = request.getSession();
		if(!userID.equals((String)session.getAttribute("userID"))) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("index.jsp");
			return;
		}
		String fileName= "";
		File file = multi.getFile("userProfile");
		if(file != null) {
			String ext = file.getName().substring(file.getName().lastIndexOf(".")+1); //확장자 확인
			if(ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {			//jsp,png,gif만 처리
				String prev = new UserDAO().getUser(userID).getUserProfile();
				File prevFile = new File(savePath + "/"+ prev);
				//사진 파일이 이미 존재하는 경우 지워질수 있도록
				if(prevFile.exists()) {
					prevFile.delete();
				}
				fileName = file.getName();		//새로운 파일 이름으로 변경함
			}else {
				session.setAttribute("messageType", "오류 메시지");
				session.setAttribute("messageContent", "이미지 파일만 업로드 가능합니다..");
				response.sendRedirect("profileUpdate.jsp");
				return;
			}
					
		}
		new UserDAO().profile(userID, fileName);		//업데이트 처리
		session.setAttribute("messageType", "성공 메시지");
		session.setAttribute("messageContent", "성공적으로 프로필이 변경되었습니다.");
		response.sendRedirect("index.jsp");
		return;
	}

}

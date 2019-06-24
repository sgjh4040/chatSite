package chat;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ChatBoxServlet
 */
@WebServlet("/ChatBoxServlet")
public class ChatBoxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userID = request.getParameter("userID");
		if(userID == null || userID.equals("")) {
			response.getWriter().write("");
		}else {
			try {
				userID = URLDecoder.decode(userID,"UTF-8");
				response.getWriter().write(getBox(userID));
				
			}catch(Exception e) {
				response.getWriter().write("");
			}
			
		}
	}
	public String getBox(String userID) {
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getBox(userID);
		if(chatList.size() == 0) return "";
		
		for(int i = 0; i<chatList.size();i++) {
			result.append("[{\"value\":\""+ chatList.get(i).getFromID()+"\"},");
			result.append("{\"value\":\""+ chatList.get(i).getToID()+"\"},");
			result.append("{\"value\":\""+ chatList.get(i).getChatContent()+"\"},");
			result.append("{\"value\":\""+ chatList.get(i).getChatTime()+"\"}]");
			//i가 마지막 원소가 아니면 ,로 추가한다. 메세지가 더 있다는것을 알려줌
			if(i != chatList.size() - 1) result.append(",");
			
		}
		result.append("], \"last\":\""+chatList.get(chatList.size()-1).getChatID()+"\"}");
		return result.toString();
	}

}

package chat;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
@WebServlet("/ChatListServlet")
public class ChatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String listType = request.getParameter("ListType");
		if(fromID == null || fromID.equals("")||toID == null || toID.equals("")||listType == null || listType.equals("")) response.getWriter().write("");
		else if(listType.equals("ten")) response.getWriter().write(getTen(URLDecoder.decode(fromID,"UTF-8"),URLDecoder.decode(toID,"UTF-8")));
		else {
			try {
				response.getWriter().write(getID(URLDecoder.decode(fromID,"UTF-8"),URLDecoder.decode(toID,"UTF-8"),listType));
			}catch(Exception e){
				response.getWriter().write("");
			}
		}
	}
	
	public String getTen(String fromID, String toID) {
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByRecent(fromID, toID, 100);
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
		//메시지를 읽었을떄 읽었음을 확인
		chatDAO.readChat(fromID, toID);
		return result.toString();
	}
	public String getID(String fromID, String toID,String chatID) {
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByID(fromID, toID, chatID);
		for(int i = 0; i<chatList.size();i++) {
			result.append("[{\"value\":\""+ chatList.get(i).getFromID()+"\"},");
			result.append("{\"value\":\""+ chatList.get(i).getToID()+"\"},");
			result.append("{\"value\":\""+ chatList.get(i).getChatContent()+"\"},");
			result.append("{\"value\":\""+ chatList.get(i).getChatTime()+"\"}]");
			//i가 마지막 원소가 아니면 ,로 추가한다. 메세지가 더 있다는것을 알려줌
			if(i != chatList.size() - 1) result.append(",");
			
		}
		result.append("], \"last\":\""+chatList.get(chatList.size()-1).getChatID()+"\"}");
		//메시지를 읽었을떄 읽었음을 확인
		chatDAO.readChat(fromID, toID);
		return result.toString();
	}

}

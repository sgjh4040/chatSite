<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@page import="user.UserDTO" %>
	<%@page import="user.UserDAO" %>
<!DOCTYPE html>
<html>
<%
		String userID = null;
		if (session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		if(userID == null){
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "현재 로그인이 되어 있지 않습니다.");
			response.sendRedirect("index.jsp");
			return;
		}
		
		UserDTO user = new UserDAO().getUser(userID);
		
	%>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/custom.css">
<title>Ajax 실시간 회원제 채팅서비스</title>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script src="js/bootstrap.js"></script>
<script type="text/javascript">
		function getUnread() {
			
			$.ajax({
				type : "POST",
				url : "./chatUnread",
				data : {
					userID : encodeURIComponent('<%=userID%>'),
		
				},
				success : function(result) {
					if(result>=1){
						showUnread(result);
					}else{
						showUnread('');
					}
				}
		
			});
		}
		function getInfiniteUnread(){
			setInterval(function(){
				getUnread();
			}, 4000);
		}
		function showUnread(result){
			$('#unread').html(result);
		}
		function passwordCheckFunction() {
			var userPassword1 = $('#userPassword1').val();
			var userPassword2 = $('#userPassword2').val();
			if (userPassword1 != userPassword2) {
				$('#passwordCheckMessage').html("비밀번호가 일치하지 않습니다.");
			} else {
				$('#passwordCheckMessage').html("");
			}
		}



</script>
</head>
<body>
<div>test:<%=user.getUserGender()%></div>
	
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.jsp">실시간 회원제 채팅 서비스</a>
		</div>
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="index.jsp">메인</a>
				<li><a href="find.jsp">친구찾기</a></li>
				<li><a href="box.jsp">메시지함 <span id="unread" class="label label-info"></span> </a></li>
			</ul>
			
					<ul class="nav navbar-nav navbar-right">
				<!-- 아래쪽 내려서 추가적으로 항목 나올수 있게 dropdown -->
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspoput="ture"
					aria-expanded="false">회원관리 <span class="caret"> </span>
				</a>
					<ul class="dropdown-menu">
						<li><a href="./update.jsp">회원정보수정</a></li>
						<li><a href="./logoutAction.jsp">로그아웃</a></li>
						
					</ul>
					</li>
					</ul> 
		</div>

	</nav>
	
	<!-- 회원 정보 수정 양식--------------------------->
	<div class="container">
		<form method="post" action="./userUpdate">
			<table class="table table-bordered table-hover"
				style="text-align: center; border: 1px solid #ddddd">
				<thead>
					<tr>
						<th colspan="3"><h4>회원정보 수정양식</h4></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width: 110px;"><h5>아이디</h5></td>
						<td><h5><%=user.getUserID() %></h5></td>
						<input type="hidden" name="userID" value="<%=user.getUserID()%>">
					</tr>
					<tr>
						<td style="width: 110px;"><h5>비밀번호</h5></td>
						<td colspan="2" style="width: 110px;"><input
							onkeyup="passwordCheckFunction();" class="form-control"
							id="userPassword1" type="password" name="userPassword1"
							placeholder="비밀번호를 입력하세요"></td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>비밀번호 확인</h5></td>
						<td colspan="2" style="width: 110px;"><input
							onkeyup="passwordCheckFunction();" class="form-control"
							id="userPassword2" type="password" name="userPassword2"
							placeholder="비밀번호 확인을 입력하세요"></td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>이름</h5></td>
						<td colspan="2" style="width: 110px;"><input
							class="form-control" id="userName" type="text" name="userName"
							placeholder="이름을 입력하세요" value="<%=user.getUserName()%>"></td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>나이</h5></td>
						<td colspan="2" style="width: 110px;"><input
							class="form-control" id="userAge" type="number" name="userAge"
							placeholder="나이를 입력하세요" value="<%=user.getUserAge()%>"></td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>성별</h5></td>
						<td colspan="2">
							<div class="form-group"
								style="text-align: center; nmargin: 0 auto;">
								<div class="btn-group" data-toggle="buttons">
									<label class="btn btn-primary <% if(user.getUserGender().equals("남자")) out.print("active");%>"> <input
										type="radio" name="userGender" autocomplete="off" value=
										<% if(user.getUserGender().equals("남자")) out.print("checked");%>>남자
									</label> <label class="btn btn-primary <% if(user.getUserGender().equals("여자")) out.print("active");%>"> <input type="radio"
										name="userGender" autocomplete="off" value="여자" <% if(user.getUserGender().equals("여자")) out.print("checked");%>>여자
									</label>
								</div>

							</div>


						</td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>이메일</h5></td>

						<td colspan="2" style="width: 110px;"><input
							class="form-control" id="userEmail" type="email" name="userEmail"
							placeholder="이메일을 입력하세요" value="<%=user.getUserEmail()%>"></td>
					</tr>
					<tr>
						<td style="text-align:left;" colspan="3"><h5 style="color:red;"id="passwordCheckMessage"></h5><input class="btn btn-primary pull-right" type="submit"value="수정"></td>
					</tr>


				</tbody>

			</table>

		</form>
	</div>

	<%
		String messageContent = null;
		if (session.getAttribute("messageContent") != null) {
			messageContent = (String) session.getAttribute("messageContent");
		}
		String messageType = null;
		if (session.getAttribute("messageType") != null) {
			messageType = (String) session.getAttribute("messageType");
		}
		if (messageContent != null) {
	%>
	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div
					class="modal-content <%if (messageType.equals("오류 메시지")) out.println("panel-warning");else out.println("panel-success");%>">
					<div class="modal-header panel-heading">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times</span> <span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							<%=messageType%>
						</h4>
					</div>
					<div class="modal-body">
						<%=messageContent%>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
				</div>


			</div>
		</div>
	</div>
	<script>
		$('#messageModal').modal("show");
	</script>



	<%
		session.removeAttribute("messageContent");
	session.removeAttribute("messageType");
		};
	%>
		<%
	if(userID != null){
	%>
		<script type="text/javascript">
		
		$(document).ready(function(){
			getUnread();
			getInfiniteUnread();
		});
		
		
		</script>
		<%
	}
		%>

</body>
</html>
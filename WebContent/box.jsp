<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		
		function chatBoxFunction() {
			var userID = '<%=userID%>';
			$.ajax({
				type : "POST",
				url : "./chatBox",
				data : {
					userID : encodeURIComponent(userID),
		
				},
				success : function(data) {
					if(data=="") return;
					$('#boxTable').html('');
					var parsed = JSON.parse(data);
					var result = parsed.result;
					for(var i = 0; i<result.length; i++){
						if(result[i][0].value == userID){
							result[i][0].value = result[i][1].value;
						}else{
							result[i][1].value = result[i][0].value;
						}
						
						addBox(result[i][0].value,result[i][1].value,result[i][2].value,result[i][3].value);
					}
					lastID = Number(parsed.last);
					
				}
		
			});
		}
		function addBox(lastID, toID, chatContent, chatTime){
			$('#boxTable').append('<tr onclick="location.href=\'chat.jsp?toID='+ encodeURIComponent(toID)+'\'">'+
					'<td style="width: 150px;"><h5>'+ lastID + '</h5></td>'+
					'<td>'+
					'<h5>' + chatContent + '</h5>'+
					'<div class="pull-right">' + chatTime + '</div>'+
					'</td>'+
					'</tr>');
		}
		function getInfiniteBox(){
			setInterval(function(){
				chatBoxFunction(lastID);
			}, 3000);
		}



</script>
</head>
<body>
	
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
				<li ><a href="index.jsp">메인</a>
				<li><a href="find.jsp">친구찾기</a></li>
				<li class="active"><a href="box.jsp">메시지함 <span id="unread" class="label label-info"></span> </a></li>
			</ul>
			<%
				if (userID == null) {
			%>
			<!-- 오른쪽 정렬 -->
			<ul class="nav navbar-nav navbar-right">
				<!-- 아래쪽 내려서 추가적으로 항목 나올수 있게 dropdown -->
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspoput="ture"
					aria-expanded="false">접속하기 <span class="caret"> </span>
				</a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li><a href="join.jsp">회원가입</a></li>
					</ul>
					</li>
					</ul> <%
				}else{
				
				%>
					<ul class="nav navbar-nav navbar-right">
				<!-- 아래쪽 내려서 추가적으로 항목 나올수 있게 dropdown -->
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspoput="ture"
					aria-expanded="false">회원관리 <span class="caret"> </span>
				</a>
					<ul class="dropdown-menu">
						<li><a href="./logoutAction.jsp">로그아웃</a></li>
						
					</ul>
					</li>
					</ul> <%
				}
			%>
		</div>

	</nav>
	<div class="container">
		<table class="table" style="margin:0 auto;">
			<thead>
			<tr>
				<th><h4>주고받은 메시지 목록</h4></th>
			</tr>
			</thead>
			<div style="overflow-y: auto; width: 100%; max-height: 450px;">
				<table class="table table-bordered table-hover" style="text-align: center; border: 1px solid #dddddd; margin:0 auto;" >
					<tbody id="boxTable">
					</tbody>
				</table>
			</div>
		</table>
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
			chatBoxFunction();
			getInfiniteBox();
		});
		
		
		</script>
		<%
	}
		%>

</body>
</html>
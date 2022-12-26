<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Insert title here</title>
<link rel="stylesheet" href="./css/style.css" />
</head>
<body>
	<div class="board_wrap">
		<div class="board_title">
			<strong>자유게시판</strong>
			<p>자유게시판 입니다.</p>
		</div>
		<div class="board_write_wrap">
		<!-- 게시판의 내용은 노출되지 않기 위해 get 방식 대신 post 방식으로 한다 -->
			<form name="frm" method="post" action="insert">
				<div class="board_write">
					<div class="title">
						<dl>
							<dt>제목</dt>
							<dd>
								<input type="text" name="title" placeholder="제목 입력" maxlength="50" />
							</dd>
						</dl>
					</div>
					<div class="info">
						<dl>
							<dt>글쓴이</dt>
							<dd>
							<!-- name을 일관성 있게 해준다 -->
							<!-- DB의 byte에 맞춰서 maxlength로 길이를 정해준다 (한글은 3byte) -->
								<input type="text" name="user_id" placeholder="글쓴이 입력" maxlength="10" />
							</dd>
						</dl>
				   <!-- <dl>
						    <dt>비밀번호</dt>
						    <dd>
							   <input type="password" name="user_id" placeholder="비밀번호 입력" />
						    </dd>
					    </dl> -->
					</div>
					<div class="cont">
						<textarea name="content" placeholder="내용 입력"></textarea>
					</div>
				</div>
			</form>
			<div class="bt_wrap">
				<a href="#" class="on" onclick="chkForm(); return false;">등록</a>
				<a href="list">취소</a>
			</div>
		</div>
	</div>
	<script>
	  <c:if test="${error != null}">
	    alert("${error}");
	  </c:if>
	</script>
	<script type="text/javascript" src="./script.js"></script>
</body>
</html>
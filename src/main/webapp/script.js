function chkForm() {
	var f = document.frm; //from 태그
	
	if (f.title.value == "") {
		alert("제목을 입력해주십시오.");
		return false;
	}
	
	if (f.user_id.value == "") {
		alert("아이디를 입력해주십시오.");
		return false;
	}
	
	f.submit(); //폼태그 전송
}
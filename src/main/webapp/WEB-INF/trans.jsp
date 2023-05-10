<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>   

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.3.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Gamja+Flower&family=Jua&family=Lobster&family=Nanum+Pen+Script&family=Single+Day&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css">
<style>
	body, body *{
		font-family: 'Jua'
	}
</style>
</head>
<body>
<h3><b>Naver Cloud Papago 번역기</b></h3>
<h5>pipeline 배포 연습</h5>
<textarea style="width: 100%;height: 150px;" id="content"
class="form-control">우리 다같이 바다보러 갈까요?</textarea>
<br>
<button type="button" class="btntrans btn btn-outline-danger" lang="en">영어로 번역하기</button>
<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>
<br>
<button type="button" class="btntrans btn btn-outline-info" lang="ja">일어로 번역하기</button>
<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>
<br>
<button type="button" class="btntrans btn btn-outline-success" lang="fr">프랑스로 번역하기</button>
<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>
<br>
<button type="button" class="btntrans btn btn-outline-secondary" lang="zh-CN">중국어로 번역하기</button>
<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>
<br>
<button type="button" class="btntrans btn btn-outline-primary" lang="de">독일어로 번역하기</button>
<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>

<script type="text/javascript">
	$(".btntrans").click(function(){
		let lang=$(this).attr("lang");
		let texttrans=$(this).next();
		$.ajax({
			type:"post",
			url:"./trans",
			data:{"message":$("#content").val(),"lang":lang},
			dataType:"text",
			success:function(res){
				//alert(res);
				let m=JSON.parse(res);
				console.log(m);
				console.log(typeof(m));
				console.log(m.message.result.translatedText);
				$(texttrans).html(m.message.result.translatedText);
			}
		});
	});
</script>

</body>
</html>

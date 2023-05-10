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
 <script src="https://voice.luzspat.com/js/cloudvoice.js"></script>
<style>
	body, body *{
		font-family: 'Jua'
	}
	
	.speak{
		 cursor: pointer;
	}
</style>
<script type="text/javascript">
$(function(){
	/* $(document).on("click",".speak",function(){
		let lang=$(this).attr("lang");		
		console.log(lang);
		if(lang=='zh-CN'){
			alert("중국어 음성 오류 발생!!");
			return;
		}
		let text=$(this).prev().prev().text();
		console.log(text);
		talk(lang,text);
	});	 */
	
	$(document).on("click",".speak",function(){
		let lang=$(this).attr("lang");		
		console.log(lang);
		//https://api.ncloud-docs.com/docs/ai-naver-papagonmt-translation
		if(lang=='en'||lang=='ja'||lang=='zh-CN'|lang=='es'){
			let text=$(this).prev().prev().text();
			$.ajax({
				type:"get",
				url:"./voice",
				data:{"message":text,"lang":lang},
				dataType:"text",
				success:function(res){
					//alert(res);
					let audio=new Audio(res);
					audio.play();					
				}
			});
		}else{
			alert("현재 영어,일어,중국어,스페인어만 목소리를 지원하고 잇습니다");
		}		
	});	
});
</script>
</head>
<body>
<div style="margin-left: 20px;">
	<h3><b>Naver Cloud Papago 번역기</b></h3>
	<b style="background-color: pink;color:blue;font-size: 20px;font-family: 'Gamja Flower'">pipeline 배포 연습-Github webhook연동</b>
	<textarea style="width: 100%;height: 150px;" id="content"
	class="form-control">우리 다같이 바다보러 갈까요?</textarea>
	<br>
	<button type="button" class="btntrans btn btn-outline-danger" lang="en">영어로 번역하기</button>
	<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>

	<br>
	<button type="button" class="btntrans btn btn-outline-info" lang="ja">일어로 번역하기</button>
	<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>
	<br>
	<button type="button" class="btntrans btn btn-outline-secondary" lang="zh-CN">중국어로 번역하기</button>
	<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>
	<br>
	<button type="button" class="btntrans btn btn-outline-success" lang="es">스페인어로 번역하기</button>
	<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>
	<br>	
	<button type="button" class="btntrans btn btn-outline-primary" lang="de">독일어로 번역하기</button>
	<div class="texttrans"  style="margin-top: 20px;font-size: 20px;width: 100%;"></div>
</div>
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
				$(texttrans).html(`<span>\${m.message.result.translatedText}</span>
						<br><i class="bi bi-megaphone speak" 
						lang=\${lang}></i>&nbsp;<sapn style='font-size:14px'>스피커를 클릭하면 음성으로 읽어드립니다</span>`);
			
			}
		});
	});
</script>

</body>
</html>

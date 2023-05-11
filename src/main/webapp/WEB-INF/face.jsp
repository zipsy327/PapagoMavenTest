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
<div>
<h2><b>Naver Clova Face Recognition(얼굴인식)</b></h2>
<br>
<h5><b>얼굴이 많이 포함된 사진으로 업로드해주세요</b></h5>
<input type="file" id="upload" class="form-conntrol">
<br>
<div class="jdata" style="width: 100%;"></div>
<h6><b>원본 이미지</b></h6>
<img src="" id="photo"  style="max-width: 400px;">
<div id="photoinfo" style="margin-left: 5px;font-size: 15px;width:300px;"></div>

<script type="text/javascript">

$("#upload").change(function(){
		let form=new FormData();
		form.append("upload",$("#upload")[0].files[0]);//선택한 1개의 파일만 업로드
		
		$.ajax({
			type:"post",
			dataType:"json",
			url:"./facerec",
			data:form,
			processData:false,
			contentType:false,
			success:function(res){  	
				if(res==null)
					return false;
				//alert(res);
				$("#photo").attr("src",res.photo);
				let jdata=res.jdata;
				//$(".jdata").text(jdata);
				
				let j=JSON.parse(jdata);
				let s=					
					`<h4 class='alert alert-danger'>총 발견된 얼굴수 : \${j.info.faceCount}개</h4>
					`;				
				
				$.each(j.faces,function(i,ele){
					s+=
						`
						\${i+1}번째 얼굴 정보<br>
						연령대 : \${ele.age.value}<br>
						사진방향 : \${ele.pose.value}<br>
						emotion(감정?) : \${ele.emotion.value}<br>
						성별 : \${ele.gender.value}<br>
						<hr>
						`
				});
				$("#photoinfo").html(s);
				
			}
		});
	});
</script>
</div>
</body>
</html>

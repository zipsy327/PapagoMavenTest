package bit.study.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FaceController {
	
	@GetMapping("/face")
	public String facePage()
	{
		return "face";
	}
	
	//사진에서 얼굴 인식하는 서비스((AI.NAVER CLOVA Face Recognition)
	//https://api.ncloud-docs.com/docs/ai-naver-clovafacerecognition-face
	@PostMapping("/facerec")
	@ResponseBody public Map<String, String> clovaFace(HttpServletRequest request,MultipartFile upload)
	{
		StringBuffer reqStr = new StringBuffer();
		String clientId = "suavgsp5x9";//애플리케이션 클라이언트 아이디값";
		String clientSecret = "p7NbTU2hTgayrLxhx9GTfJUGuMP09DcUzQbT4MZH";//애플리케이션 클라이언트 시크릿값";

		//사진 업로드(webapp)
		String realPath=request.getSession().getServletContext().getRealPath("/");

		try {
			upload.transferTo(new File(realPath+"/"+upload.getOriginalFilename()));
		} catch (IllegalStateException | IOException e) {
			System.out.println("사진 업로드시 오류:"+e.getMessage());
		}

		try {
			String paramName = "image"; // 파라미터명은 image로 지정			

			String imgFile = realPath+"/"+upload.getOriginalFilename();

			File uploadFile = new File(imgFile);
			String apiURL = "https://naveropenapi.apigw.ntruss.com/vision/v1/face"; // 얼굴 감지
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);
			// multipart request
			String boundary = "---" + System.currentTimeMillis() + "---";
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
			OutputStream outputStream = con.getOutputStream();
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);
			String LINE_FEED = "\r\n";
			// file 추가
			String fileName = uploadFile.getName();
			writer.append("--" + boundary).append(LINE_FEED);
			writer.append("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
			writer.append("Content-Type: "  + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.flush();
			FileInputStream inputStream = new FileInputStream(uploadFile);
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.flush();
			inputStream.close();
			writer.append(LINE_FEED).flush();
			writer.append("--" + boundary + "--").append(LINE_FEED);
			writer.close();
			BufferedReader br = null;
			int responseCode = con.getResponseCode();
			if(responseCode==200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {  // 오류 발생
				System.out.println("error!!!!!!! responseCode= " + responseCode);
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();			
			//System.out.println(response.toString());
			Map<String, String> map=new HashMap<>();
			map.put("photo", upload.getOriginalFilename());
			map.put("jdata", response.toString());
			return map;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}

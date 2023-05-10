package bit.study.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

//네이버 Papago Text Translation API 예제
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TransController {
	
	@GetMapping("/")
	public String home(HttpServletRequest request)
	{		
		delete(request);//처음 실행시 그전 mp3 파일 모두 삭제
		return "trans";
	}
	
	@PostMapping("/trans")
	@ResponseBody String trans(String message,String lang)
	{
		String clientId = "e1in5hdlaj";
		String clientSecret = "Jjautr41v2sIx0pwt9s9kcrAHnKowQuIiortcg8M";//애플리케이션 클라이언트 시크릿값";
	     try {
	         String text = URLEncoder.encode(message, "UTF-8");
	         String apiURL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";
	         URL url = new URL(apiURL);
	         HttpURLConnection con = (HttpURLConnection)url.openConnection();
	         con.setRequestMethod("POST");
	         con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
	         con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
	         // post request
	         String postParams = "source=ko&target="+lang+"&text=" + text;
	         con.setDoOutput(true);
	         DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	         wr.writeBytes(postParams);
	         wr.flush();
	         wr.close();
	         int responseCode = con.getResponseCode();
	         BufferedReader br;
	         if(responseCode==200) { // 정상 호출
	             br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	         } else {  // 오류 발생
	             br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	         }
	         String inputLine;
	         StringBuffer response = new StringBuffer();
	         while ((inputLine = br.readLine()) != null) {
	             response.append(inputLine);
	         }
	         br.close();
	         System.out.println(response.toString());
	         return response.toString();
	     } catch (Exception e) {
	         System.out.println(e);
	         return null;
	     }
	}
	
	@GetMapping("/voice")
	@ResponseBody public String selectVoice(String message,String lang,HttpServletRequest request)
	{
	    String clientId = "suavgsp5x9";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "p7NbTU2hTgayrLxhx9GTfJUGuMP09DcUzQbT4MZH";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(message, "UTF-8"); // 13자
            String apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            // post request
            //목소리, 언어 선택
            //https://api.ncloud-docs.com/docs/ai-naver-clovavoice-ttspremium 참조
            String selectVoice="jinho";
            if(lang.equals("en"))
            	selectVoice="djoey";
            else if(lang.equals("ja"))
            	selectVoice="dayumu";
            else if(lang.equals("zh-CN"))
            	selectVoice="meimei";
            else if(lang.equals("es"))
            	selectVoice="jose";
            
            	
            String postParams = "speaker="+selectVoice+"&volume=0&speed=0&pitch=0&format=mp3&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                // 랜덤한 이름으로 mp3 파일 생성
                String tempname = Long.valueOf(new Date().getTime()).toString();
                String uploadPath=request.getSession().getServletContext().getRealPath("/");
                File f = new File(uploadPath+"/"+tempname + ".mp3");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while ((read =is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                is.close();                
              
                return f.getName();
            } else {  // 오류 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
	}
	
	public void delete(HttpServletRequest request)
	{
		String uploadPath=request.getSession().getServletContext().getRealPath("/");
        
		File files=new File(uploadPath+"/");
		String []list=files.list();
		System.out.println(list.length);
		for(String fn:list)
		{
			System.out.println(fn);
			File f=new File(uploadPath+"/"+fn);
			if(fn.endsWith("mp3"))
			{
				if(f.exists()) {
					System.out.println(fn+" 파일 삭제");
					f.delete();
				}
			}			
		}
	}
}








package com.example.demo.src.Kakao;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.User.UserProvider;
import com.example.demo.src.User.model.PostUserReq;
import com.example.demo.src.User.model.PostUserRes;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.KAKAO_CODE_EMPTY;

@Service
public class KakaoService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String KAKAO_CLIENT_ID="c122074d216f863250738e4212064fe1";
    private final static String KAKAO_REDIRECT_URI="https://localhost:9000/users/login/kakao";
    private final static String PROFILE_API_URL= "https://kapi.kakao.com/v2/user/me";
    private final static String KAKAO_OAUTH_URL = "https://kauth.kakao.com/oauth/token";

    //카카오 로그인 액세스 토큰
    @Transactional
    public String getKakaoAccessToken (String code) {
        String access_Token = "";
        String refresh_Token = "";

        try {
            URL url = new URL(KAKAO_OAUTH_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+KAKAO_CLIENT_ID); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri="+KAKAO_REDIRECT_URI); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }


    //카카오 유저 정보 얻어오기
    public String getKakaoUserInfo(String token) throws BaseException {
        String email = "";
        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(PROFILE_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            int id = element.getAsJsonObject().get("id").getAsInt();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();

            if(hasEmail){
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            br.close();
            return email;

        } catch (IOException e) {
            e.printStackTrace();
            throw  new  BaseException(KAKAO_CODE_EMPTY);
        }

    }

}

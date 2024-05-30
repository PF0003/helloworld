package com.spring.helloworld.kakaoLogin.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.springframework.stereotype.Service;
 
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
 
//īī�� �α���
@Service
public class KakaoAPI {
    //��ū �� �޾ƿ���
    public String getAccessToken (String authorize_code) {
        String access_Token = "***REMOVE***";
        String refresh_Token = "***REMOVE***";
        String reqURL = "***REMOVE***";
        
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            //    POST ��û�� ���� �⺻���� false�� setDoOutput�� true��
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            
            //    POST ��û�� �ʿ�� �䱸�ϴ� �Ķ���� ��Ʈ���� ���� ����
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=e68c9069996ed8ece88f23c25ea43167");
            sb.append("&redirect_uri=http://localhost:8080/helloworld/member/kakaoLogin.do");
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();
            
            //    ��� �ڵ尡 200�̶�� ����
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
 
            //    ��û�� ���� ���� JSONŸ���� Response �޼��� �о����
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            
            // Gson ���̺귯���� ���Ե� Ŭ������ JSON�Ľ� ��ü ����
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
            
            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);
            
            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        return access_Token;
    }
    // ����� ���� �޾ƿ���
    public HashMap<String, Object> getUserInfo (String access_Token) {
        
        // ��û�ϴ� Ŭ���̾�Ʈ���� ���� ������ �ٸ� �� �ֱ⿡ HashMapŸ������ ����
//    	HashMap<String, Object> userInfo = new HashMap<>();
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            
            // ��û�� �ʿ��� Header�� ���Ե� ����
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String line = "";
            String result = "";
            
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            
//          JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            //String nickname = kakao_account.getAsJsonObject().get("nickname").getAsString();

            userInfo.put("email", email);
            //userInfo.put("name", nickname);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return userInfo;
    }
}
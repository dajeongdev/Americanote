package com.coffee.americanote.navermap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application-naver-token.properties")
public class AddressToCoordinate {

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    public String[] addressToCoordinate(String address) {
        String[] result = {"", ""};
        // 주소 입력 -> 위도, 경도 좌표 추출.
        try {
            String addr = URLEncoder.encode(address, "UTF-8");

            // Geocoding 개요에 나와있는 API URL 입력.
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;    // JSON

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Geocoding 개요에 나와있는 요청 헤더 입력.
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            System.out.println("clientId = " + clientId);
            System.out.println("clientSecret = " + clientSecret);

            // 요청 결과 확인. 정상 호출인 경우 200
            int responseCode = con.getResponseCode();

            BufferedReader br;

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;

            StringBuffer response = new StringBuffer();

            while((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            JSONTokener tokener = new JSONTokener(response.toString());
            JSONObject object = new JSONObject(tokener);
            JSONArray arr = object.getJSONArray("addresses");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject temp = (JSONObject) arr.get(i);
                //System.out.println("address : " + temp.get("roadAddress"));
                //System.out.println("jibunAddress : " + temp.get("jibunAddress"));
                result[0] = (String) temp.get("y"); // 위도
                result[1] = (String) temp.get("x"); // 경도
            }
        } catch (Exception  e) {
            System.out.println(e);
        }
        return result;
    }
}

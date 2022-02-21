package site.metacoding.hospapp.ex01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

// 목적 다운로드
public class DownloadTest {

    public static void main(String[] args) {

        // 1. URL주소 만들기
        StringBuffer sbUrl = new StringBuffer();

        // String key = URLEncoder.encode(""); 세이프한 http요청 하게 해줌.

        sbUrl.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService");
        sbUrl.append("?serviceKey="); // 서비스키
        sbUrl.append("wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D"); // 서비스키
        sbUrl.append("&pageNo="); // 몇번 째 페이지인지
        sbUrl.append("1");
        sbUrl.append("&numOfRows=");
        sbUrl.append("10"); // 한 페이지당 몇개씩 가져올지
        sbUrl.append("&_type=");
        sbUrl.append("json"); // 데이터 포맷은 JSON

        // 2. 다운로드 받기
        try {
            // URL safe가 적용되어 있음. URL safe가 되어 있으면서 더 이상 반영 안함! 매우 좋은 라이브러리이다.
            URL url = new URL(sbUrl.toString()); // http or https 일 수도 있다
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // return을 부모타입으로, 알아서 다운캐스팅 해라
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8")); // conn에서 오는
                                                                                                           // 데이터를 받기
                                                                                                           // 때문에
                                                                                                           // conn.get으로
                                                                                                           // 표현

            StringBuffer sbDownload = new StringBuffer(); // 통신결과 모아두기!!
            while (true) {
                String input = br.readLine(); // 버퍼에 들어오는 내용을 Input변수에 넣어줌

                if (input == null) {
                    break;
                } // 버퍼에 들어오는 내용이 없으면 break로 끝냄
                sbDownload.append(input);
            }

            // 3.검증받기
            System.out.println(sbDownload.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

package site.metacoding.hospapp.ex04;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.script.SimpleBindings;

import com.google.gson.Gson;

// 목적 :  List<Hospital> 클래스로 옮겨담기,, 아이템에서  hospital로 이주하기
public class MigrationTest {

    public static int getTotalCount() {

        int totalCount = 0;

        try {

            // 1. URL 주소 만들기 - totalCount 확인용
            StringBuffer sb = new StringBuffer();

            sb.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService");
            sb.append("?serviceKey="); // 서비스키
            sb.append("wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g==");
            sb.append("&pageNo=?"); // 몇번째 페이지 인지
            sb.append("1");
            sb.append("&numOfRows=");
            sb.append("2"); // totalCount 체크만 할 것이기 때문에 2개만 받아도 된다. (왜 2개냐면 1개만 받으면 List가 아니라 Object로 받더라)
            sb.append("&_type=");
            sb.append("json"); // 데이터 포맷은 JSON

            // 2. 다운로드 받기 - totalCount 확인용

            URL url = new URL(sb.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));

            StringBuffer sbDownload = new StringBuffer(); // 통신결과 모아두기
            while (true) {
                String input = br.readLine();
                if (input == null) {
                    break;
                }
                sbDownload.append(input);
            }

            // 3. 검증 - totalCountCheck
            // System.out.println(sb.toString());

            // 4. 파싱
            Gson gson = new Gson();
            ResponseDto responseDto = gson.fromJson(sbDownload.toString(), ResponseDto.class);

            // 5. totalCount 담기
            totalCount = responseDto.getResponse().getBody().getTotalCount();
            System.out.println("totalCount : " + totalCount);
            return totalCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    public static ResponseDto download() {

        ResponseDto responseDto = null;

        try {
            // 6. 전체 데이터 받기
            // (1) URL 주소 만들기
            int totalCount = getTotalCount();

            if (totalCount == 0) {
                System.out.println("totalCount를 제대로 받지 못하였습니다.");
                return null;
            }

            StringBuffer sb = new StringBuffer();

            sb.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService");
            sb.append("?serviceKey="); // 서비스키
            sb.append("wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g==");
            sb.append("&pageNo=?"); // 몇번째 페이지 인지
            sb.append("1");
            sb.append("&numOfRows=");
            sb.append(totalCount); // totalCount 체크만 할 것이기 때문에 2개만 받아도 된다. (왜 2개냐면 1개만 받으면 List가 아니라 Object로 받더라)
            sb.append("&_type=");
            sb.append("json"); // 데이터 포맷은 JSON

            // (2) 다운로드 받기
            URL url = new URL(sb.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));

            StringBuffer sbDownload = new StringBuffer(); // 통신결과 모아두기
            while (true) {
                String input = br.readLine();
                if (input == null) {
                    break;
                }
                sbDownload.append(input);
            }

            // (3) 파싱
            Gson gson = new Gson();
            responseDto = gson.fromJson(sbDownload.toString(), ResponseDto.class);

            // 7. 사이즈 검증
            System.out.println("아이템 사이즈 : " + responseDto.getResponse().getBody().getItems().getItem().size());
            System.out.println("totalCount : " + totalCount);
            if (responseDto.getResponse().getBody().getItems().getItem().size() == totalCount) {
                System.out.println("성공~~~~~~~~~~~~~~~~");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseDto;
    }

    public static List<Hospital> migration() {
        ResponseDto responseDto = download();
        List<Item> list = responseDto.getResponse().getBody().getItems().getItem();

        List<Hospital> hospitals = new ArrayList<>(); // hospitals에 list로 옮기면 끝!
        // hospitals.add(list.get(0)); new hospitals 해서 넣어야 됨

        // hospitals가 아닌 list 만큼 돌아야 한다(?)
        for (Item item : list) {
            Hospital hs = new Hospital();
            hs.objectCopy(item); // copy 메서드의 목적, 다른 타입의 item을 받아서 hospital에 옮겨담기
            hospitals.add(hs);
        }
        // 내용들이 잘 들어갔나 검증
        System.out.println(hospitals.get(5681).getYadmNm());
        return hospitals;
    }

    public static void main(String[] args) {

    }
}
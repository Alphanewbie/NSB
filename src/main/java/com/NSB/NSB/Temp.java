package com.NSB.NSB;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Temp
{
    static public class Morpheme {
        final String text;
        final String type;

        Morpheme(String text, String type) {
            this.text = text;
            this.type = type;
        }
    }

    private final static char[] KO_INIT_S =
            {
                    'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
                    'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
            }; // 19

    private final static String[] KO_INIT_M =
            {
                    "ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅗ,ㅏ", "ㅗ,ㅐ", "ㅗ,ㅣ", "ㅛ", "ㅜ", "ㅜ,ㅓ",
                    "ㅜ,ㅔ", "ㅜ,ㅣ", "ㅠ", "ㅡ", "ㅡ,ㅣ", "ㅣ"
            }; // 21
    private final static String[] KO_INIT_E =
            {
                    "","ㄱ", "ㄲ", "ㄱ,ㅅ", "ㄴ", "ㄴ,ㅈ", "ㄴ,ㅎ", "ㄷ", "ㄹ", "ㄹ,ㄱ", "ㄹ,ㅁ", "ㄹ,ㅂ", "ㄹ,ㅅ", "ㄹ,ㅌ", "ㄹ,ㅍ",
                    "ㄹ,ㅎ", "ㅁ", "ㅂ", "ㅂ,ㅅ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
            };

    private String openApiURL;
    private String accessKey;
    private String analysisCode;   // 언어 분석 코드
    private Gson gson = new Gson();
    private Map<String, Object> request = new HashMap<>();
    private Map<String, String> argument = new HashMap<>();
    private ArrayList<Morpheme> SList = new ArrayList<>(); //스트링분석결과의 리스트
    private HashMap<String, String> dic = new HashMap<>();

    // 사전 dic.put(형태소 분석시 나오는 단어, 해당 단어에 맞는 모션 이름);
    private void CreateDic(){
        dic.put("안녕","안녕하세요");
        dic.put("고맙다","고맙다");
        dic.put("감사","고맙다");
        dic.put("가방","");
        dic.put("는데","");
        dic.put("려고","");
        dic.put("만","");
        dic.put("무엇","");
        dic.put("ㅂ니다","");
        dic.put("보다","");
        dic.put("사다","");
        dic.put("습니까","");
        dic.put("어서오세요","");
        dic.put("얼마","");
        dic.put("오다","");
        dic.put("원","");
        dic.put("으로","");
        dic.put("이것","");
        dic.put("주다","");
        dic.put("피스","");
        dic.put("열중쉬어","");
        dic.put("차렷","");
        dic.put("가다","");
        dic.put("건너","건너편");
        dic.put("길","");
        dic.put("되다","");
        dic.put("로","");
        dic.put("맞다","");
        dic.put("맞나다","맞다");
        dic.put("묻다","물어보다");
        dic.put("소유","");
        dic.put("실례","실례합니다");
        dic.put("어디","");
        dic.put("에서","");
        dic.put("여기","");
        dic.put("역","");
        dic.put("있다","");
        dic.put("정류","정류장");
        dic.put("타다","");
        dic.put("하다","");
        dic.put("해요","하다");
        dic.put("아니다","아니");
        dic.put("너","허락");
        dic.put("그것","");
        dic.put("물론","");
        dic.put("앞","");
        dic.put("번호","");
        dic.put("적다","");

    }

    // 객채 처음 생성될 때 초기화하는 부분
    Temp() {
        Key key = new Key();
        openApiURL= key.getOpenApiURL();
        accessKey = key.getAccessKey();
        analysisCode = "ner";
        CreateDic();
    }

    class doAction {
        URL url;
        HttpURLConnection con=null;
        Map<String, Object> responseBody = null;

        doAction() {
            onPreExecute();
            doInBackground();
            onPostExecute();
        }

        // API 연결 시작
        void onPreExecute() {
            try {
                url = new URL(openApiURL);
                con = (HttpURLConnection)url.openConnection();
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // API 연결 끝
        void onPostExecute() {
            try {
                con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 형태소 분석 시작
        void doInBackground() {
            try {

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(gson.toJson(request).getBytes("UTF-8"));
                wr.flush();
                wr.close();

                Integer responseCode = con.getResponseCode();
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                String responseBodyJson = sb.toString();

                // http 요청 오류 시 처리
                if ( responseCode != 200 ) {
                    // 오류 내용 출력
                    System.out.println("[error] " + responseBodyJson);
                    return;
                }

                responseBody = gson.fromJson(responseBodyJson, Map.class);
                Integer result = ((Double) responseBody.get("result")).intValue();
                Map<String, Object> returnObject;
                List<Map> sentences;

                // 분석 요청 오류 시 처리
                if ( result != 0 ) {
                    // 오류 내용 출력
                    System.out.println("[error] " + responseBody.get("result"));
                    return;
                }

                // 분석 결과 활용
                returnObject = (Map<String, Object>) responseBody.get("return_object");
                sentences = (List<Map>) returnObject.get("sentence");

                // slist 초기화
                SList.clear();
                for( Map<String, Object> sentence : sentences ) {
                    // 형태소 분석기 결과 수집 및 정렬
                    List<Map<String, Object>> morphologicalAnalysisResult = (List<Map<String, Object>>) sentence.get("morp");   //리스트를 생성 분석 된 형태소의 수 만큼
                    for (Map<String, Object> morphemeInfo : morphologicalAnalysisResult) {     //리스트의 크기만큼 반복한다.
                        String lemma=(String) morphemeInfo.get("lemma");
                        String type= (String) morphemeInfo.get("type");

                        if(TypeSearch(type)) {
                            if (type.startsWith("V")) {
                                lemma += "다";
                            }

                            Morpheme alpha=new Morpheme(lemma,type);
                            //lemma+=morpheme.count;    //중복 불가능하게 하고 싶다면
                            SList.add(alpha);
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean TypeSearch(String type) {
            return type.startsWith("V") || type.startsWith("N") || type.startsWith("E") || type.equals("SN");
        }
    }

    //자모음 분리기
    private static String toKoJaso(String text) {
        if (text == null) { return null; }

        // StringBuilder의 capacity가 0으로 등록되는 것 방지.
        if (text.length() == 0) { return ""; }

        // 한글자당 최대 3글자가 될 수 있다.
        // 추가 할당 없이 사용하기위해 capacity 를 최대 글자 수 만큼 지정하였다.
        StringBuilder rv = new StringBuilder(text.length() * 3);

        for (char ch : text.toCharArray()) {
            if (ch >= '가' && ch <= '힣') {
                // 한글의 시작부분을 구함
                int ce = ch - '가';
                // 초성을 구함
                rv.append(KO_INIT_S[ce / (588)]); // 21 * 28
                rv.append(",");
                // 중성을 구함
                rv.append(KO_INIT_M[(ce = ce % (588)) / 28]); // 21 * 28
                rv.append(",");
                // 종성을 구함
                if ((ce = ce % 28) != 0)
                {
                    rv.append(KO_INIT_E[ce]);
                    rv.append(",");
                }
            }
            else {
                rv.append(ch);
            }
        }
        return rv.toString();
    }

    // 메인 함수
    public String Func(String input) {
        // 결과 스트링 저장하는 객체
        StringBuilder gamma = new StringBuilder();

        argument.put("analysis_code", analysisCode);
        argument.put("text", input);
        request.put("access_key", accessKey);
        request.put("argument", argument);

        // 형태소 분석 시작
        new doAction();

        //해쉬맵 검색
        for(Morpheme sentence: SList){
            String lemma = sentence.text;
            String type = sentence.type;

            // 수사일경우
            if(type.equals("SN")||type.equals("NR"))
            {
                if(lemma.equals("하나")||lemma.equals("일")||lemma.equals("한")) lemma = "1";
                if(lemma.equals("둘")||lemma.equals("이")||lemma.equals("두")) lemma = "2";
                if(lemma.equals("셋")||lemma.equals("삼")||lemma.equals("세")) lemma = "3";
                if(lemma.equals("넷")||lemma.equals("사")||lemma.equals("네")) lemma = "4";
                if(lemma.equals("다섯")||lemma.equals("오")) lemma = "5";
                if(lemma.equals("여섯")||lemma.equals("육")) lemma = "6";
                if(lemma.equals("일곱")||lemma.equals("칠")) lemma = "7";
                if(lemma.equals("여덟")||lemma.equals("팔")) lemma = "8";
                if(lemma.equals("아홉")||lemma.equals("구")) lemma = "9";
                if(lemma.equals("열")||lemma.equals("십")) lemma = "10";
                if(lemma.equals("공")||lemma.equals("영")) lemma = "0";

                gamma.append(lemma);
                gamma.append(",");
            }

            // 어미 어간이 아니고 사전에 없을경우(대부분 고유명사)
            // 조사 까지 다 바꿀경우 (dic.get(lemma) == null && !type.startsWith("E"))
            else if(dic.get(lemma) == null && type.startsWith("N") && !lemma.equals("편")) {
                gamma.append(toKoJaso(lemma));
                //그리고 지화로
            }

            // 사전에 있는 단어의 경우(시제나 기타단어도 사전에 넣어야 함)
            else if(dic.get(lemma) != null) {
                if(!dic.get(lemma).equals(""))
                    gamma.append(dic.get(lemma));
                else
                    gamma.append(lemma);
                gamma.append(",");
            }
            //나머지의 경우에는 해쉬 테이블로
        }

        // Idle 붙히고 마지막콤마(,) 제거 후 결과 반환
        gamma.insert(0, "Idle,");
        gamma = new StringBuilder(gamma.substring(0, gamma.length() - 1));
        return gamma.toString();
//        return "Idle,안녕하세요,고맙다";
    }
}

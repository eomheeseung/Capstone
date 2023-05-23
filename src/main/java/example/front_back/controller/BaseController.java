package example.front_back.controller;

import example.front_back.logic.BaseLogic;
import example.front_back.service.ComparisonOfSimilarities;
import example.front_back.service.CrimeDataLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BaseController {
    private final BaseLogic logic;
    private final ComparisonOfSimilarities cos;

    private final CrimeDataLogic crimeDataLogic;

    // 이것만 있으면 됨.
    @PostMapping("/test")
    public void method1(@RequestBody String jsonString) {
        String decodedJsonStr = null;
        try {
            decodedJsonStr = java.net.URLDecoder.decode(jsonString, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //넘어오는 쿼리 디스크립터 Double[]로 넣기 위해 파싱
        String[] keys = decodedJsonStr.replace("{", "").replace("}", "").replace("=", "").split(",");
        Double[] queryDescriptor = new Double[keys.length];
        //쿼리 디스크립터 초기화
        for (int i = 0; i < keys.length; i++) {
            String[] keyValue = keys[i].split(":");
            queryDescriptor[i] = Double.parseDouble(keyValue[1]);
        }

        //범죄자 중 가장 낮은 유사도와 낮은 유사도를 갖는 범죄자 인덱스 찾기 위한 변수들
        int findIndex=0;
        double findMinSimilarities=1;


        System.out.println(cos.criminals.size());
        //파싱한 쿼리 디스크립터와 범죄자의 디스크립터를 비교
        for (int i=0; i<cos.criminals.size(); i++){

            //유사도 비교를 위해 범죄자 디스크립터의 JSONObject 타입을 Double[]로 변환
            JSONArray ArrayCriminalDescriptor = new JSONArray(cos.criminals.get(i).get("5").toString());

            Double[] DoubleCriminalDescriptor = new Double[ArrayCriminalDescriptor.length()];
            for (int j = 0; j < ArrayCriminalDescriptor.length(); j++) {
                DoubleCriminalDescriptor[j] = ArrayCriminalDescriptor.getDouble(j);
            }

            // 범죄자 중 가장 낮은 유사도를 찾음
            double findSimilarities = cos.compareSimilarity(DoubleCriminalDescriptor, queryDescriptor);
            if (findMinSimilarities > findSimilarities){
                findMinSimilarities = findSimilarities;
                findIndex = i;
            }
        }

        if (cos.threshold > findMinSimilarities){
            log.info("가장 유사한 범죄자 발견: "+cos.criminals.get(findIndex));
            log.info("유사도: "+findMinSimilarities);
        }

    }

    /**
     * 1. 이더리움에 저장된 데이터를 가져온 변수는 crime
     * 2. crimeDataLogic.stringToJson의 return은 descriptors 128개의 double 값
     *
     * @param crime
     * @return
     */
    @PostMapping("/crime")
    public String crimeTest(@RequestBody String crime) {
        //json으로 변환
        JSONArray jsonArray = new JSONArray(crime);

        //jsonObject로 변환 후 cos.criminals에 저장
        for (int i = 0; i < jsonArray.length(); i++) {

            //기존 5번 키값의 이상한 값들을 제거하고 descriptor 값만 뽑음
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String descriptorString = (String) jsonObject.get("5");
            JSONObject descriptorJson = new JSONObject(descriptorString);
            JSONArray descriptorArray = (JSONArray) descriptorJson.get("descriptors");

            // 디스크립터의 대괄호가 2개 있어서 json 형태를 하나 벗김으로써 괄호 제거
            JSONArray innerDescriptors = descriptorArray.getJSONArray(0);

            //기존 5번 키값 제거하고 새로 파싱한 descriptor 추가
            jsonObject.remove("5");
            jsonObject.put("5", innerDescriptors);

            cos.criminals.add(jsonObject);
        }

        // cos.criminals에 저장된 객체 출력
        for (int i = 0; i < cos.criminals.size(); i++) {
            log.info("<저장 완료> 범죄자 " + i + ": " + cos.criminals.get(i));
        }

        return "ok";
    }

    @PostMapping("/response/test")
    public String responseMethod(@RequestBody String dto) {
        log.info(dto);
        String result = flagRun(cleaning(dto));
        return result;
    }

    private JSONObject cleaning(String target) {
        String id = target.substring(6, 8);
        String idValue = target.substring(17, 21);

        String pw = target.substring(30, 38);
        String pwValue = target.substring(47, 51);
        log.info(id + ": " + idValue + ", " + pw + ": " + pwValue);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(id, idValue);
        jsonObject.put(pw, pwValue);
        return jsonObject;
    }

//    private JSONObject parseString(String target) {
//        JSONParser parser = new JSONParser();
//
//        try {
//            Object parse = parser.parse(target);
//            JSONObject jsonObject = (JSONObject) parse;
//            return jsonObject;
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }


    /**
     * 테스트 용 메소드
     *
     * @param target
     * @return
     */
    private String flagRun(JSONObject target) {
        if (target.get("id").equals("aaaa") && target.get("password").equals("1234")) {
            return "valid";
        } else {
            return "invalid";
        }
    }
}

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

        String[] keys = decodedJsonStr.replace("{", "").replace("}", "").replace("=", "").split(",");
        Double[] values = new Double[keys.length];

        for (int i = 0; i < keys.length; i++) {
            String[] keyValue = keys[i].split(":");
            values[i] = Double.parseDouble(keyValue[1]);
        }

        double compareSimilarity = cos.compareSimilarity(cos.labeledDescriptor, values);
        log.info(String.valueOf(compareSimilarity));
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
//        log.info("범죄자 정보"+crime);
        JSONArray jsonArray = new JSONArray(crime);

        // 객체를 저장할 배열
        JSONObject[] objects = new JSONObject[jsonArray.length()];

        // 데이터를 객체로 변환하여 배열에 저장
        for (int i = 0; i < jsonArray.length(); i++) {
            org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
            objects[i] = jsonObject;
        }

        // 배열에 저장된 객체 출력 (예시)
        for (int i = 0; i < objects.length; i++) {
            log.info("Object " + i + ": " + objects[i]);
        }
//        crimeDataLogic.stringToJson(crime);
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

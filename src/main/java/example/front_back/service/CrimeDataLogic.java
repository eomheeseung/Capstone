package example.front_back.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CrimeDataLogic {

    /*단, 현재 범죄자 데이터는 1명에 대한 데이터만 들어옴.
    나중에 수정*/
    public void stringToJson(String target) {
        // 이더리움에 저장되어 있는 데이터를 json으로 바꾸는 코드
        JSONObject jsonObject = new JSONObject(target);

        // 5번째 value가 이더리움에 저장되어 있는 사진의 descriptors 값이므로 get
        String savedDescriptors = (String) jsonObject.get("5");
        log.info(savedDescriptors);

        // 다시 string -> json
        JSONObject descriptorJson = new JSONObject(savedDescriptors);

        // log를 확인해보면 "descriptors"라는 키를 이용해서 128개의 데이터를 가져옴
        // usingData 변수 사용해서 웹캠으로 들어온 데이터랑 비교하면 됨.
        Object usingData = descriptorJson.get("descriptors");
        log.info(usingData.toString());

//        return descriptorJson;
    }

    /**
     * 범죄자인지 아닌지 구별 후 flag에 값을 넘겨줌.
     * @param flag
     * @param target
     * @return
     */
    public JSONObject responseData(String flag, String target) {
        JSONObject responseJson = new JSONObject();

        if (flag.equals("2")) {
            return null;
        }

        if (flag.equals("1")) {
            JSONObject jsonObject = new JSONObject(target);
            responseJson.put("name", jsonObject.get("0"))
                    .put("country", jsonObject.get("1"))
                    .put("age", jsonObject.get("2"))
                    .put("crime", jsonObject.get("3"))
                    .put("bool", jsonObject.get("4"))
                    .put("value", jsonObject.get("6"));
        }

        return responseJson;
    }
}

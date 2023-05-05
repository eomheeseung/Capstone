package example.front_back.logic;

import example.front_back.dto.RequestDTO;
import example.front_back.dto.RequestDTO2;
import example.front_back.dto.RequestDTO3;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BaseLogic {
    public String accept(RequestDTO dto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", dto.getEmail());
        jsonObject.put("pw", dto.getPw());

        log.info(jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }

    public String accept2(RequestDTO2 dto2) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", dto2.getName());
        jsonObject.put("phoneNumber", dto2.getPhoneNumber());
        jsonObject.put("flag", dto2.isFlag());

        log.info(jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }

    public String accept3(RequestDTO3 dto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", dto.getId());
        jsonObject.put("password", dto.getPassword());

        log.info(jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }
}

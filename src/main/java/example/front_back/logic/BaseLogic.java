package example.front_back.logic;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class BaseLogic {
    private Map<String, Object> map = new ConcurrentHashMap<>();

    public String findCrimeLogic(JSONObject findCrime) {
        map.put("name", findCrime.get("0"));
        map.put("country", findCrime.get("1"));
        map.put("age", findCrime.get("2"));
        map.put("criminal", findCrime.get("3"));
        map.put("flag", findCrime.get("4"));
        map.put("num", findCrime.get("6"));

        JSONObject jsonObject = new JSONObject(map);
        map.clear();

        return jsonObject.toString();
    }
}

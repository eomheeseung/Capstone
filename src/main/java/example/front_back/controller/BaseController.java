package example.front_back.controller;

import example.front_back.logic.BaseLogic;
import example.front_back.service.ComparisonOfSimilarities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BaseController {
    private final BaseLogic logic;
    private final ComparisonOfSimilarities cos;

    // 이것만 있으면 됨.
    @PostMapping("/test")
    public void method1(@RequestBody String jsonString){
        String decodedJsonStr=null;
        try {
            decodedJsonStr = java.net.URLDecoder.decode(jsonString, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] keys = decodedJsonStr.replace("{", "").replace("}", "").replace("=","").split(",");
        Double[] values = new Double[keys.length];

        for (int i = 0; i < keys.length; i++) {
            String[] keyValue = keys[i].split(":");
            values[i] = Double.parseDouble(keyValue[1]);
        }

        double compareSimilarity = cos.compareSimilarity(cos.labeledDescriptor, values);
        log.info(String.valueOf(compareSimilarity));
    }
}

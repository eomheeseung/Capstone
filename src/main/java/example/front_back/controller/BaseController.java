package example.front_back.controller;

import example.front_back.logic.BaseLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BaseController {
    private final BaseLogic logic;

    // 이것만 있으면 됨.
    @PostMapping("/test")
    public void method1(@RequestBody String jsonString){
        log.info(jsonString);
    }
}

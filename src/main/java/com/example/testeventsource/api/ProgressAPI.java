package com.example.testeventsource.api;


import com.example.testeventsource.utils.Progress;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ProgressAPI {

    @GetMapping("/events")
    public SseEmitter handle() {
        return Progress.BuildSseEmitterConnect();
    }

}

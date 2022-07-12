package com.example.testeventsource.service;

import com.example.testeventsource.utils.Progress;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.example.testeventsource.utils.Progress.getSseEmitter;

@Service
public class UploadFileService {

    public void upTest(String uuid) {

        SseEmitter emitter = getSseEmitter(uuid);

        for (int i = 0; i <= 500; i++) {
            Progress.SendBuilder.builder().emitter(emitter).uuid(uuid).percent(i).count(500).send();
            sleep(1000);
        }

        emitter.complete();

    }

    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

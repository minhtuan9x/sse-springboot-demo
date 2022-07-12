package com.example.testeventsource.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class Progress {

    public static final Map<String, SseEmitter> EMITTERS = Collections.synchronizedMap(new HashMap<>());

    public static String getProgress(float element, float count) {
        return String.format("%.1f", (element / count) * 100);
    }

    private static void logCount() {
        log.warn("Client map size: {}", EMITTERS.size());
    }

    public static SseEmitter BuildSseEmitterConnect() {

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        UUID uuid = UUID.randomUUID();

        try {
            sseEmitter.send(SseEmitter.event().name("GUI_ID").data(uuid));
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
        }

        EMITTERS.put(uuid.toString(), sseEmitter);
        log.info("Connected: {}", uuid);
        logCount();

        sseEmitter.onCompletion(() -> {
            EMITTERS.remove(uuid.toString());
            log.info("Completed: {}", uuid);
            logCount();
        });

        sseEmitter.onTimeout(() -> EMITTERS.remove(uuid.toString()));
        sseEmitter.onError((e) -> EMITTERS.remove(uuid.toString()));

        return sseEmitter;
    }

    public static SseEmitter getSseEmitter(String uuid) {
        SseEmitter emitter = EMITTERS.get(uuid);
        log.info("Got a emitter: {}", uuid);
        EMITTERS.remove(uuid);
        logCount();
        return emitter;
    }


    public static class SendBuilder {

        private SseEmitter emitter;
        private String uuid;
        private String message;
        private float percent;
        private float count;

        public static SendBuilder builder() {
            return new SendBuilder();
        }

        public SendBuilder emitter(SseEmitter emitter) {
            this.emitter = emitter;
            return this;
        }

        public SendBuilder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public SendBuilder message(String message) {
            this.message = message;
            return this;
        }

        public SendBuilder percent(float percent) {
            this.percent = percent;
            return this;
        }

        public SendBuilder count(float count) {
            this.count = count;
            return this;
        }

        public void send() {
            try {
                if (message != null)
                    emitter.send(SseEmitter.event().name(uuid).data(message));
                else
                    emitter.send(SseEmitter.event().name(uuid).data(getProgress(percent, count)));
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }

    }
}

package com.example.quanlisanbay.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonMessageSource {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> getMessages(String lang) throws IOException {
        String filePath = "src/main/resources/static/lang/" + (lang.equals("vi") ? "tieng-viet.json" : "english.json");

        try (InputStream inputStream = new FileSystemResource(filePath).getInputStream()) {
            JsonNode rootNode = objectMapper.readTree(inputStream);
            Map<String, String> messages = new HashMap<>();
            rootNode.fields().forEachRemaining(entry -> messages.put(entry.getKey(), entry.getValue().asText()));
            return messages;
        }
    }

    public String getMessage(String key, String lang) throws IOException {
        Map<String, String> messages = getMessages(lang);
        return messages.getOrDefault(key, "Key not found: " + key);
    }
}

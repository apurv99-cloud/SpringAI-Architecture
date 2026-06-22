package com.example.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeminiEmbeddingService {

    @Value("${spring.ai.google.genai.api-key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    public float[] embed(String text) {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent?key="
                + apiKey;

        EmbeddingRequest request = new EmbeddingRequest(
                new Content(
                        List.of(new Part(text))));

        EmbeddingResponse response = restClient.post()
                .uri(url)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<EmbeddingResponse>() {
                });

        List<Float> values = response.embedding().values();

        float[] result = new float[values.size()];

        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }

        return result;
    }

    public record EmbeddingRequest(Content content) {
    }

    public record Content(List<Part> parts) {
    }

    public record Part(String text) {
    }

    public record EmbeddingResponse(Embedding embedding) {
    }

    public record Embedding(List<Float> values) {
    }
}
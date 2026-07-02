package com.example.demo.Services;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Component;

@Component
public class GeminiEmbeddingModel implements EmbeddingModel {

    private final GeminiEmbeddingService embeddingService;

    public GeminiEmbeddingModel(GeminiEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {

        List<String> texts = request.getInstructions();

        List<Embedding> embeddings = texts.stream()
                .map(text -> new Embedding(embeddingService.embed(text), 0))
                .toList();

        return new EmbeddingResponse(embeddings);
    }

    @Override
    public float[] embed(Document document) {
        return embeddingService.embed(document.getText());
    }
}
package com.example.demo.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Services.GeminiEmbeddingService;

@RestController

public class GeminiController {

    private ChatClient chatClient;
    
    @Autowired
    private GeminiEmbeddingService geminiEmbeddingService;

    @Autowired
    private VectorStore vectorStore;

    // private final EmbeddingModel embeddingModel;

    public GeminiController(GoogleGenAiChatModel chatModel, GeminiEmbeddingService geminiEmbeddingService) {
        this.chatClient = ChatClient.create(chatModel);
        this.geminiEmbeddingService = geminiEmbeddingService;

    }

    @GetMapping("/api/{message}")
    public String chat(@PathVariable String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @PostMapping("/api/recommend")
    public String recommend(@RequestParam String type, @RequestParam String year, @RequestParam String lang) {

        String temp = """
                I want to watch movie of a {type} tonight with good rating, looking for movies around this year {year}. The langauge am looking for is {lang}.
                Suggest me one specific movie and tell me also cast and length of the movie.

                        """;
        PromptTemplate promptTemplate = new PromptTemplate(temp);
        Prompt prompt = promptTemplate.create(Map.of("type", type, "year", year, "lang", lang));
        String response = chatClient
                .prompt(prompt)
                .call()
                .content();

        return response;

    }

    @PostMapping("/api/embedding")
    public float[] embeddings(@RequestParam String text) {
        return geminiEmbeddingService.embed(text);

    }

    // @PostMapping("/api/similarity")
    // public double getSimilarity(@RequestParam String text1, @RequestParam String
    // text2) {
    // float[] emb1 = geminiEmbeddingService.embed("I love Java");
    // float[] emb2 = geminiEmbeddingService.embed("I love Java");
    // }

    @PostMapping("/api/similarity")
    public double similarity(
            @RequestParam String text1,
            @RequestParam String text2) {

        float[] emb1 = geminiEmbeddingService.embed(text1);
        float[] emb2 = geminiEmbeddingService.embed(text2);

        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (int i = 0; i < emb1.length; i++) {

            dotProduct += emb1[i] * emb2[i];

            norm1 += emb1[i] * emb1[i];
            norm2 += emb2[i] * emb2[i];
        }

        return dotProduct /
                (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    @PostMapping("api/product")
    public List<Document> getProducts(@RequestParam String text) {

        return vectorStore.similaritySearch(text);
    }

    @PostMapping("/api/ask")
    public String getAnswerUsingRag(@RequestParam String query) {

        return chatClient.prompt(query)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())

                .call()
                .content();
    }

}
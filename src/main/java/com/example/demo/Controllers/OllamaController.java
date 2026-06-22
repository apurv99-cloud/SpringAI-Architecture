// package com.example.demo.Controllers;

// import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
// import org.springframework.ai.chat.memory.ChatMemory;
// import org.springframework.ai.chat.memory.MessageWindowChatMemory;
// import org.springframework.ai.chat.model.ChatResponse;
// import org.springframework.ai.ollama.OllamaChatModel;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// public class OllamaController {

//         private final ChatClient chatClient;

//         private final ChatMemory chatMemory = MessageWindowChatMemory.builder()
//                         .maxMessages(20)
//                         .build();

//         public OllamaController(OllamaChatModel chatModel) {
//                 this.chatClient = ChatClient.create(chatModel);
//         }

//         // public OllamaController(ChatClient.Builder builder) {

//         // this.chatClient = builder
//         // .defaultAdvisors(
//         // MessageChatMemoryAdvisor.builder(chatMemory)
//         // .build())
//         // .build();
//         // }

//         @GetMapping("/api/{conversationId}/{message}")
//         public ResponseEntity<String> chat(
//                         @PathVariable String conversationId,
//                         @PathVariable String message) {

//                 ChatResponse chatResponse = chatClient.prompt()
//                                 .user(message)
//                                 .advisors(advisor -> advisor.param(
//                                                 ChatMemory.CONVERSATION_ID,
//                                                 conversationId))
//                                 .call()
//                                 .chatResponse();

//                 String response = chatResponse.getResult()
//                                 .getOutput()
//                                 .getText();

//                 return ResponseEntity.ok(response);
//         }
// }


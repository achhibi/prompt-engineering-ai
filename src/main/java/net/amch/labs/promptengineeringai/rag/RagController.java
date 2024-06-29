package net.amch.labs.promptengineeringai.rag;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rag")
public class RagController {
    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public RagController(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }


    @GetMapping
    public String find(String question) {
        var similarDocuments =
                vectorStore.similaritySearch(SearchRequest.query(question).withTopK(2));

        String information = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));

        PromptTemplate promptTemplate = new SystemPromptTemplate("""
                You are a helpful assistant.                              
                Use only the following information to answer the question :
                ----------------------
                IFORMATION: {information}
                -----------------------
                QUESTION: {question}
                """);

        Map<String, Object> model = Map.of("information", information, "question", question);

        return chatClient.prompt(promptTemplate.create(model)).call().content();

    }
}

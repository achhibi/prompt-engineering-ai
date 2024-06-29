package net.amch.labs.promptengineeringai.rag;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rag")
public class RagController {
    private final RagService ragService;
    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public RagController(RagService ragService, VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.ragService = ragService;
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }


    @GetMapping
    public String find(String name) {
        ragService.storeEmbeddingsFromTextFile();
        var similarDocuments =
                vectorStore.similaritySearch(SearchRequest.query(name).withTopK(2));
        String information = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
        System.out.println(similarDocuments);
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("""
                You are a helpful assistant.                              
                Use the following information to answer the question in francais:
                {information}
                """);
        var systemMessage = systemPromptTemplate.createMessage(
                Map.of("information", information));
        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
                Tell me about {name}
                """);
        Map<String, Object> model = Map.of("name", name);
        var userMessage = new UserMessage(userMessagePromptTemplate.create(model).getContents());

        var prompt = new Prompt(List.of(systemMessage, userMessage));

        return chatClient.prompt(prompt).call().content();

    }
}

package net.amch.labs.promptengineeringai.usecases.controller;

import net.amch.labs.promptengineeringai.usecases.model.Person;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/extraction")
public class TextExtractionController {

    private final ChatClient chatClient;
    @Value("classpath:prompts/textExtraction.st")
    private Resource prompt;

    public TextExtractionController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping(value = "text")
    public Person textExtraction(@RequestBody Prompt input) {

        return chatClient.prompt()
                .user(userSpec -> userSpec.text(prompt)
                        .param("text", input.text())
                )
                .call()
                .entity(Person.class);
    }
    record Prompt(String text){}
}

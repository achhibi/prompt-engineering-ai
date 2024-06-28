package net.amch.labs.promptengineeringai.controller;

import net.amch.labs.promptengineeringai.model.Person;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/extraction")
public class TextExtractionController {

    private final ChatClient chatClient;

    public TextExtractionController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    @GetMapping(value = "text")
    public Person textExtraction(String message) {

        return  chatClient.prompt()
                .user(userSpec -> userSpec.text("""
                            Extrayez les données structurées du texte fourni.
                            --------
                            TEXT: 
                            {text}
                            --------
                        """)
                        .param("text", message)
                )
                .call()
                .entity(Person.class);
    }
}

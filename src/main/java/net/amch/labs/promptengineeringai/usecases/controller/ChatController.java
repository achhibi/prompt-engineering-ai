package net.amch.labs.promptengineeringai.usecases.controller;

import net.amch.labs.promptengineeringai.usecases.model.Invoice;
import net.amch.labs.promptengineeringai.usecases.model.SpringVersion;
import net.amch.labs.promptengineeringai.usecases.model.Team;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Value("classpath:prompts/worldcap.st")
    private Resource worldcap;

    private final ChatClient chatClient;

    public ChatController(final ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping(value = "spring", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SpringVersion> chatEntityList() {
        return chatClient.prompt()
                .system("""
                        Donne-moi toutes les versions du Spring Framework.
                        """)
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
    }

    @GetMapping(value = "football", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team chatEntity(@RequestParam("year") String year) {


        return chatClient.prompt()
                .user(userSpec -> userSpec.text(worldcap)
                        .param("year", year))
                .call()
                .entity(Team.class);
    }
}
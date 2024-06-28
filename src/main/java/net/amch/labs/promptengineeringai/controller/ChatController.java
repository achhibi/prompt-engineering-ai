package net.amch.labs.promptengineeringai.controller;

import net.amch.labs.promptengineeringai.model.Invoice;
import net.amch.labs.promptengineeringai.model.SpringVersion;
import net.amch.labs.promptengineeringai.model.Team;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Value("classpath:images/facture.jpeg")
    private Resource imageResource;
    private final ChatClient chatClient;

    public ChatController(final ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    @GetMapping(value = "football", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team chatEntity(@RequestParam("year") String year) {


        return chatClient.prompt()
                .system("""
                        Donne-moi l'équipe qui a gagné la Coupe du Monde de football de l'année indiquée.
                        """)
                .user(year)
                .call()
                .entity(Team.class);
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

    @GetMapping(value = "ocr", produces = MediaType.APPLICATION_JSON_VALUE)
    public Invoice ocr()  {

        var userMessageText = """
                Analyse cette facture et donne-moi la liste des articles avec leurs prix et quantités.              
                """;
        var userMessage = new UserMessage(userMessageText, List.of(
                new Media(MimeTypeUtils.IMAGE_JPEG, imageResource)
        ));
        return chatClient.prompt()
                .messages(userMessage)
                .call()
                .entity(Invoice.class);

    }
}
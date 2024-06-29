package net.amch.labs.promptengineeringai.usecases.controller;


import net.amch.labs.promptengineeringai.usecases.model.Histo;
import net.amch.labs.promptengineeringai.usecases.model.Invoice;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ocr")
public class OcrController {

    @Value("classpath:images/facture.jpeg")
    private Resource invoice;
    private final ChatClient chatClient;

    @Value("classpath:images/conso.png")
    private Resource consoImg;

    public OcrController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping(value = "invoice", produces = MediaType.APPLICATION_JSON_VALUE)
    public Invoice ocr() {

        var userMessageText = """
                Analyse cette facture et donne-moi la liste des articles avec leurs prix et quantités.
                """;
        var userMessage = new UserMessage(userMessageText, List.of(
                new Media(MimeTypeUtils.IMAGE_JPEG, invoice)
        ));
        return chatClient.prompt()
                .messages(userMessage)
                .call()
                .entity(Invoice.class);

    }

    @GetMapping(value = "conso", produces = MediaType.APPLICATION_JSON_VALUE)
    public Histo conso() {

        var userMessageText = """
                Analyse la courbe, extrait les valeurs de la consommation et calcule la prévision pour les 5 jours suivants .
                """;
        var userMessage = new UserMessage(userMessageText, List.of(
                new Media(MimeTypeUtils.IMAGE_PNG, consoImg)
        ));
        return chatClient.prompt()
                .system("You are a Data scientist")
                .messages(userMessage)
                .call()
                .entity(Histo.class);

    }

}

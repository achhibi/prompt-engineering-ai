package net.amch.labs.promptengineeringai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.List;

public class OpenApiTest {

    @Test
    void testOpenAiApi(){
        var openAiApi = new OpenAiApi(System.getenv("OPENAI_API_KEY"));
        var chatModel= new OpenAiChatModel(openAiApi, OpenAiChatOptions
                .builder()
                .withMaxTokens(1000)
                .withTemperature(0F)
                .withModel("gpt-4o")
                .build());
        Prompt zeroShotPrompt = getPrompt();
        var chatResponse = chatModel.call(zeroShotPrompt);
        System.out.println( chatResponse.getResult().getOutput().getContent());
    }

    private static Prompt getPrompt() {
        SystemMessage systemMessage = new SystemMessage("""
               Donne moi l'équipe qui a gagner la coupe du monde du foot de l'année qui sera fournie
               Je veux le résultat au format json sous la forme suivante:
               - Nom de l'équipe 
               - Entraîneur
               - La liste des joueurs
               - le pays organisateur
                """);
        UserMessage userMessage = new UserMessage("Je veux le résultat pour l'année 2022");
        Prompt zeroShotPrompt = new Prompt(List.of(systemMessage, userMessage));
        return zeroShotPrompt;
    }
}

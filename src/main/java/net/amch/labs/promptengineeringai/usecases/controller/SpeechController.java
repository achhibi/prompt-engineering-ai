package net.amch.labs.promptengineeringai.usecases.controller;

import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/speech")
public class SpeechController {
    private final SpeechModel speechModel;

    SpeechController(SpeechModel speechModel) {
        this.speechModel = speechModel;
    }

    @GetMapping(produces = "audio/mp3")
    byte[] speech(@RequestParam String message) {
        return speechModel.call(new SpeechPrompt(message)).getResult().getOutput();

    }

    @GetMapping("/options")
    ResponseEntity<byte[]> speechWithOpenAiOptions(@RequestParam String message) {
        var speechResponse = speechModel.call(new SpeechPrompt(message, OpenAiAudioSpeechOptions.builder()
                .withModel("tts-1")
                .withVoice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .withSpeed(1.0f)
                .build()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "audio/mp3");
        return ResponseEntity.ok().headers(headers)
                .body(speechResponse.getResult().getOutput());
    }
}

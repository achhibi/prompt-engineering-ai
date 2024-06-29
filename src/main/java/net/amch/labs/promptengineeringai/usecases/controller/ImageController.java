package net.amch.labs.promptengineeringai.usecases.controller;


import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {
    final ImageModel imageModel;

    public ImageController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping
    public String generate(@RequestBody Prompt message){
        var imageResponse = imageModel.call(new ImagePrompt(message.description(), OpenAiImageOptions.builder()
                .withQuality("hd")
                .withN(1)
                .withHeight(1024)
                .withWidth(1024)
                .withModel(OpenAiImageApi.ImageModel.DALL_E_3.getValue())
                .build()));
        return imageResponse.getResult().getOutput().getUrl();
    }
    record Prompt(String description) {}

}

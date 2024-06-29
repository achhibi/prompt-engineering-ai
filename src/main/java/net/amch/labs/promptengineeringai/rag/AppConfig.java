package net.amch.labs.promptengineeringai.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Configuration
class AppConfig {

    @Value("classpath:rag/rag.txt")
    private Resource resource;
    @Value("store.json")
    private String storeName;

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) throws IOException {
        var vectorStore = new SimpleVectorStore(embeddingModel);


        String path = Path.of("src", "main", "resources", "store")
                .toFile().getAbsolutePath() + File.separator + storeName;
        File storeFile = new File(path);

        if (storeFile.exists()) {
            vectorStore.load(storeFile);
        } else {
            var textReader = new TextReader(resource);
            textReader.setCharset(Charset.defaultCharset());
            List<Document> documents = textReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> chunks = textSplitter.split(documents);
            vectorStore.add(chunks);
            vectorStore.save(storeFile);
        }

        return vectorStore;
    }
}
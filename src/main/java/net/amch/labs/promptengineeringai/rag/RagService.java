package net.amch.labs.promptengineeringai.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.List;

@Service
class RagService {
    private final VectorStore vectorStore;
    
    @Value("classpath:rag/rag.txt")
    private Resource resource;
    
    public RagService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    
    public void storeEmbeddingsFromTextFile() {
        var textReader = new TextReader(resource);
        textReader.setCharset(Charset.defaultCharset());
        List<Document> documents = textReader.get();

        vectorStore.add(documents);
    }
}
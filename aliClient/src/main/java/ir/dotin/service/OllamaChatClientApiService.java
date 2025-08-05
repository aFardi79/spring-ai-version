package ir.dotin.service;


import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OllamaChatClientApiService {

    @Autowired
    private OllamaChatClient chatClient;
    private ChatClient chatClient2;


    public String sendMessageWithChat(String message) {
        ChatResponse chatResponse = chatClient2.call(
                new Prompt(message)
        );
        return chatResponse.getResult().toString();
    }



}

package ir.dotin.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.dotin.model.bitbucket.DiffModel;
import ir.dotin.model.domain.DeepSeekMessageRequest;
import ir.dotin.model.domain.DeepSeekRequest;
import ir.dotin.model.domain.DeepSeekResponse;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@Service
@Slf4j
public class DeepSeekAiClientService {

    private RestClient restClient;

    String promptTemplate = """
            ROLE: Senior Software Engineer (Java 8, Hibernate Expert)  
            
            TASK: Review the given Java source code for improper Hibernate session management.  
            
            TARGET PATTERN:  
            Look for any occurrence where a Hibernate session is opened using:  
            HibernateSession session = HibernateManager.createSession();
            
            but the corresponding code block does not contain a session.close() call.  
            
            RESPONSE RULES:  
            - Output ONLY the raw code block(s) that match this condition.  
            - Do NOT add explanations, analysis, or extra text.  
            - If no matching code exists, output : file has no session that are not Closed.  
            
            INPUT_CODE:  
            \"\"\"%s\"\"\"
            """;


    public String getDeepSeekApiClientMessage(String deepSeekApiClientMessage) {
        List<DeepSeekMessageRequest> deepSeekMessageRequests = new ArrayList<>();
        deepSeekMessageRequests.add(new DeepSeekMessageRequest("system", "You are a helpful assistant."));
        deepSeekMessageRequests.add(new DeepSeekMessageRequest("user", deepSeekApiClientMessage));
        DeepSeekRequest deepSeekRequest = new DeepSeekRequest("deepseek-chat", deepSeekMessageRequests, false);
        restClient = RestClient.builder().baseUrl("https://api.deepseek.com").build();
        DeepSeekResponse response = restClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + "sk-6c97f1e767804952a92d5f2344c9e22f")
                .body(deepSeekRequest)
                .retrieve()
                .body(DeepSeekResponse.class);
        return response.getChoices().get(0).getMessage().getContent();
    }


    public String getDeepSeekApiChatBitbucketMessage(String deepSeekApiChatBitbucketMessage) {
        JSONObject root = new JSONObject(deepSeekApiChatBitbucketMessage);
        JSONObject pullRequest = root.getJSONObject("pullRequest");
        int prId = pullRequest.getInt("id");
        JSONObject fromRef = pullRequest.getJSONObject("fromRef");
        JSONObject repository = fromRef.getJSONObject("repository");
        String repoSlug = repository.getString("slug");
        String projectKey = repository.getJSONObject("project").getString("key");
        String apiUrl = String.format(
                "http://localhost:7990/rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/diff",
                projectKey, repoSlug, prId
        );
        String deepValue = "";
        restClient = RestClient.builder().baseUrl(apiUrl).build();
        DiffModel diffModel = restClient.get()
                .header("Authorization", "Bearer ".concat("BBDC-MjcwMjc1NjgyNzI1Ouz/VnT2LW9PxIElA+jmw5j6yGZX"))
                .retrieve()
                .toEntity(DiffModel.class)
                .getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String diffValue = objectMapper.writeValueAsString(diffModel);
            deepValue = getDeepSeekApiClientMessage(diffValue);
            String commentApiUrl = String.format(
                    "http://localhost:7990/rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/comments",
                    projectKey, repoSlug, prId
            );
            JSONObject commentPayLoad = new JSONObject();
            commentPayLoad.put("text", deepValue);
            restClient = RestClient.builder().baseUrl(commentApiUrl).build();
            String response = restClient.post()
                    .header("Authorization", "Bearer ".concat("BBDC-MjcwMjc1NjgyNzI1Ouz/VnT2LW9PxIElA+jmw5j6yGZX"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(commentPayLoad.toString())
                    .retrieve()
                    .body(String.class);
            String approveApiUrl = String.format(
                    "http://localhost:7990/rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/approve",
                    projectKey, repoSlug, prId
            );
            restClient = RestClient.builder().baseUrl(approveApiUrl).build();
            String approveResponse = restClient.post()
                    .header("Authorization", "Bearer ".concat("BBDC-MjcwMjc1NjgyNzI1Ouz/VnT2LW9PxIElA+jmw5j6yGZX"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(commentPayLoad.toString())
                    .retrieve()
                    .body(String.class);
            return response;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return deepValue;
    }



    public String getOpenSessionSecarioFromfile(){
        try {
            String javaFilePath = "/home/ftamir79/IdeaProjects/spring-ai-version/aliClient/src/main/java/ir/dotin/tempo/Test.java";
            String javaFileCode = Files.readString(Paths.get(javaFilePath));
            List<DeepSeekMessageRequest> deepSeekMessageRequests = new ArrayList<>();
            String finalPromt= String.format(promptTemplate,javaFileCode);
            deepSeekMessageRequests.add(new DeepSeekMessageRequest("system", finalPromt));
            DeepSeekRequest deepSeekRequest = new DeepSeekRequest("deepseek-chat", deepSeekMessageRequests, false);
            restClient = RestClient.builder().baseUrl("https://api.deepseek.com").build();
            DeepSeekResponse response = restClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + "sk-6c97f1e767804952a92d5f2344c9e22f")
                    .body(deepSeekRequest)
                    .retrieve()
                    .body(DeepSeekResponse.class);
            return response.getChoices().get(0).getMessage().getContent();
        }catch (IOException e){
            log.error(e.getMessage());
        }
        return null;
    }


}

package ir.dotin.controller;


import ir.dotin.service.ChatClientApi;
import ir.dotin.service.DeepSeekAiClientService;
import ir.dotin.service.OllamaChatClientApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1/ai")
public class OllamaController {




    @Autowired
    private ChatClientApi chatClientApi;


    @Autowired
    private OllamaChatClientApiService ollamaChatClientApiService;

    @Autowired
    private DeepSeekAiClientService  deepSeekAiClientService;



    @PostMapping("/sendPureMessage/{message}")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
       return new ResponseEntity<>(chatClientApi.sendMessage(message),new HttpHeaders(), HttpStatus.OK);
    }



    @PostMapping("/sendOllamaMessage/{message}")
    public ResponseEntity<String> sendMessageOllama(@RequestBody String message) {
        return new ResponseEntity<>(ollamaChatClientApiService.sendMessageWithChat(message),new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/request/{message}")
    public ResponseEntity<String> request(@PathVariable("message") String message) {
        return new ResponseEntity<>(message,new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/sendPureMessage/JavaApiStram")
    public ResponseEntity<String> sendMessageJavaApiStram(@RequestBody String message) {
        return new ResponseEntity<>(chatClientApi.sendJavaApiStreamMessage(message),new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/reDirect")
    public RedirectView setRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:3000/redirect");
        return redirectView;
    }


    @PostMapping("/sendPureMessage/DeepSeekApiClient")
    public String getDeepSeekApiClientMessage(@RequestBody String message) {
       return deepSeekAiClientService.getDeepSeekApiClientMessage(message);
    }

    @PostMapping("/sendPureMessage/callBitBucket")
    public String getCallBitBucketMessage(@RequestBody String message) {
        return deepSeekAiClientService.getDeepSeekApiChatBitbucketMessage(message);
    }


    @GetMapping("/sendPureMessage/checkOpenSession")
    public String checkOpenSession() {
        return deepSeekAiClientService.getOpenSessionSecarioFromfile();
    }


}

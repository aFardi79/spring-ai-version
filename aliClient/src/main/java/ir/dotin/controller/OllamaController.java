package ir.dotin.controller;


import ir.dotin.service.ChatClientApi;
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



    @PostMapping("/sendMessage/{message}")
    public ResponseEntity<String> sendMessage(@PathVariable("message") String message) {
       return new ResponseEntity<>(chatClientApi.sendMessage(message),new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/request/{message}")
    public ResponseEntity<String> request(@PathVariable("message") String message) {
        return new ResponseEntity<>(message,new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/reDirect")
    public RedirectView setRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:3000/redirect");
        return redirectView;
    }
}

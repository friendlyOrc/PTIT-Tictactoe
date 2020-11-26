package tictactoe.web;

import tictactoe.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import tictactoe.Account;

@Controller
public class GreetingController {


  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public Message greeting(Account acc) throws Exception {
    Thread.sleep(1000); // simulated delay
    return new Message("Hello, " + HtmlUtils.htmlEscape(acc.getName()) + "!");
  }

}
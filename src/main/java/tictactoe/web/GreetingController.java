package tictactoe.web;

import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import tictactoe.Account;
import tictactoe.data.AccountRepository;

@Controller
public class GreetingController {
  private final AccountRepository accRepo;

  @Autowired
  GreetingController(AccountRepository accRepo) {
    this.accRepo = accRepo;
  }

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/hello")
  @SendTo("/login/new")
  public ArrayList<Account> newLogin(String id) throws Exception {
    Thread.sleep(1000); // simulated delay
    ArrayList<Account> accList = accRepo.findActiveAccount();
    System.out.println(accList);
    return accList;
  }

  @MessageMapping("/invite")
  public void sendSpecific(String id) throws Exception {
    System.out.println(id);
    String[] temp = id.split(" ");
    simpMessagingTemplate.convertAndSend("/login/" + temp[1], temp[0] + " " + temp[2]);
  }
}
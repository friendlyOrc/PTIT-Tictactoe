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
import tictactoe.data.GameRepository;

@Controller
public class HomeController {
  private final AccountRepository accRepo;
  private final GameRepository gameRepo;

  @Autowired
  HomeController(AccountRepository accRepo, GameRepository gameRepo) {
    this.accRepo = accRepo;
    this.gameRepo = gameRepo;
  }

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/newlogin")
  @SendTo("/client/new")
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
    simpMessagingTemplate.convertAndSend("/client/invite/" + temp[1], temp[0] + " " + temp[2]);
  }

  @MessageMapping("/invite/decline")
  public void decline(String msg) throws Exception {
    System.out.println(msg);
    String[] temp = msg.split(" ");
    simpMessagingTemplate.convertAndSend("/client/decline/" + temp[0], temp[1]);
  }

  @MessageMapping("/invite/accept")
  public void accept(String msg) throws Exception {
    System.out.println(msg);
    String[] temp = msg.split(" ");

    int gameID = (int) gameRepo.count() + 1;
    gameRepo.addNew(gameID, Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));

    simpMessagingTemplate.convertAndSend("/client/accept/" + temp[0], gameID);
    simpMessagingTemplate.convertAndSend("/client/accept/" + temp[1], gameID);
  }
}
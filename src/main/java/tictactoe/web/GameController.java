package tictactoe.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import tictactoe.Account;
import tictactoe.Game;
import tictactoe.data.AccountRepository;
import tictactoe.data.GameRepository;

@Controller
public class GameController {
    private final AccountRepository accRepo;
    private final GameRepository gameRepo;
    private static Map<Integer, Integer> gameList = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> reList = new HashMap<Integer, Integer>();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // @Value("${JAVA_PATH}")
    // private String myVariable;
    @Autowired
    GameController(AccountRepository accRepo, GameRepository gameRepo) {
        this.accRepo = accRepo;
        this.gameRepo = gameRepo;
    }

    @GetMapping("/game")
    public String start(Model model, HttpSession session, @RequestParam int matchID) {

        if (session.getAttribute("account") == null) {
            return "redirect:/login";
        }
        Game game = gameRepo.findGame(matchID).get(0);
        System.out.print("=====================" + game.getAcc1().getId() + "\n");

        session.setAttribute("game", game);
        model.addAttribute("account", (Account) session.getAttribute("account"));
        model.addAttribute("page", "Game");
        return "game";
    }

    @MessageMapping("/ready")
    public void ready(String msg) throws Exception {

        String[] temp = msg.split(" ");
        if (!gameList.containsKey(Integer.parseInt(temp[0]))) {
            gameList.put(Integer.parseInt(temp[0]), 0);
        }
        int count = gameList.get(Integer.parseInt(temp[0])) + 1;
        if (count > 2)
            count = 2;
        System.out.print("================" + count);
        gameList.replace(Integer.parseInt(temp[0]), count);
        if (count == 2) {
            simpMessagingTemplate.convertAndSend("/client/game/" + temp[0], temp[1] + " ---------");
        }
    }

    @MessageMapping("/move")
    public void move(String msg) throws Exception {
        String[] temp = msg.split(" ");
        int win = -1;
        String[] board = new String[9];
        for (int i = 0; i < 9; i++) {
            board[i] = String.valueOf(temp[3].charAt(i));

            System.out.print(board[i]);
        }
        System.out.print("\n");
        for (int a = 0; a < 8; a++) {
            String line = "";

            switch (a) {
                case 0:
                    line = board[0] + board[1] + board[2];
                    break;
                case 1:
                    line = board[3] + board[4] + board[5];
                    break;
                case 2:
                    line = board[6] + board[7] + board[8];
                    break;
                case 3:
                    line = board[0] + board[3] + board[6];
                    break;
                case 4:
                    line = board[1] + board[4] + board[7];
                    break;
                case 5:
                    line = board[2] + board[5] + board[8];
                    break;
                case 6:
                    line = board[0] + board[4] + board[8];
                    break;
                case 7:
                    line = board[2] + board[4] + board[6];
                    break;
            }
            // For X winner
            if (line.equals("XXX")) {
                win = 1;
            }

            // For O winner
            else if (line.equals("OOO")) {
                win = 2;
            }
        }
        if (win == -1) {
            for (int a = 0; a < 9; a++) {
                if (board[a].equals("-")) {
                    break;
                } else if (a == 8) {
                    win = 0;
                }
            }
        }

        System.out.print("===========WINNER==========" + win);
        simpMessagingTemplate.convertAndSend("/client/move/" + temp[0], temp[2] + " " + temp[3]);
        if (win == 1) {
            simpMessagingTemplate.convertAndSend("/client/result/" + temp[0], temp[1]);
            accRepo.win(Integer.parseInt(temp[1]));
        } else if (win == 2) {
            simpMessagingTemplate.convertAndSend("/client/result/" + temp[0], temp[2]);
            accRepo.win(Integer.parseInt(temp[2]));
        } else if (win == 0) {
            simpMessagingTemplate.convertAndSend("/client/result/" + temp[0], -1);
            accRepo.draw(Integer.parseInt(temp[1]));
            accRepo.draw(Integer.parseInt(temp[2]));
        }
    }

    @MessageMapping("/rematch")
    public void rematch(String msg) throws Exception {
        String[] temp = msg.split(" ");
        int id = (int) (gameRepo.count() + 1);
        if (!reList.containsKey(id)) {
            reList.put(id, 0);
        }
        int count = reList.get(id) + 1;
        if (count > 2)
            count = 2;
        System.out.print("================" + count);
        reList.replace(id, count);
        if (count == 2) {
            gameRepo.addNew(id, Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            simpMessagingTemplate.convertAndSend("/client/rematch/" + temp[0], id);
            simpMessagingTemplate.convertAndSend("/client/rematch/" + temp[0], id);
        }
    }
}
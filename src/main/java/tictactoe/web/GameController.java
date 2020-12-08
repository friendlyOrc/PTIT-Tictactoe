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
import tictactoe.Board;
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

        accRepo.busy(game.getAcc1().getId());
        accRepo.busy(game.getAcc2().getId());

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
            Game game = gameRepo.findGame(Integer.parseInt(temp[0])).get(0);
            simpMessagingTemplate.convertAndSend("/client/game/" + temp[0], game.getAcc2().getId() + " ---------");
        }
    }

    @MessageMapping("/move")
    public void move(String msg) throws Exception {
        String[] temp = msg.split(" ");
        Board board = new Board(temp[3]);
        int win = board.checkWin();

        System.out.print("===========WINNER==========" + win);
        simpMessagingTemplate.convertAndSend("/client/move/" + temp[0], temp[2] + " " + temp[3]);
        if (win != -1) {
            Game game = gameRepo.findGame(Integer.parseInt(temp[0])).get(0);
            gameRepo.result(win, game.getId());
            if (win == 1) {
                simpMessagingTemplate.convertAndSend("/client/result/" + temp[0], 2);
                accRepo.win(game.getAcc2().getId());
                gameRepo.result(2, Integer.parseInt(temp[0]));
            } else if (win == 2) {
                simpMessagingTemplate.convertAndSend("/client/result/" + temp[0], 1);
                accRepo.win(game.getAcc1().getId());
                gameRepo.result(1, Integer.parseInt(temp[0]));
            } else if (win == 0) {
                simpMessagingTemplate.convertAndSend("/client/result/" + temp[0], -1);
                accRepo.draw(game.getAcc1().getId());
                accRepo.draw(game.getAcc2().getId());
                gameRepo.result(0, Integer.parseInt(temp[0]));
            }
        }
    }

    @MessageMapping("/surr")
    public void surr(String msg) throws Exception {
        String[] temp = msg.split(" ");

        Game game = gameRepo.findGame(Integer.parseInt(temp[0])).get(0);
        int id = Integer.parseInt(temp[1]);
        if (id == game.getAcc1().getId()) {
            simpMessagingTemplate.convertAndSend("/client/result/" + temp[0], 2);
            accRepo.win(game.getAcc2().getId());
            gameRepo.result(2, Integer.parseInt(temp[0]));
        } else {
            simpMessagingTemplate.convertAndSend("/client/result/" + temp[0], 1);
            accRepo.win(game.getAcc1().getId());
            gameRepo.result(1, Integer.parseInt(temp[0]));
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

    @MessageMapping("/quit")
    public void quit(String gameId) throws Exception {

        simpMessagingTemplate.convertAndSend("/client/quit/" + gameId, "end");

    }
}
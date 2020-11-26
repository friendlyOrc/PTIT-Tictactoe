package tictactoe.web;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import tictactoe.Account;
import tictactoe.data.AccountRepository;

@Controller
public class HomeController {
    private final AccountRepository accRepo;

    // @Value("${JAVA_PATH}")
    // private String myVariable;
    @Autowired
    HomeController(AccountRepository accRepo) {
        this.accRepo = accRepo;
    }

    @GetMapping("/")
    public String home(Model model) {
        // System.out.println("==========> " + myVariable);
        model.addAttribute("account", new Account());
        model.addAttribute("page", "Login");
        return "login";
    }

    @PostMapping
    public String checkLogin(Account acc, Model model, HttpSession session) {

        ArrayList<Account> accList = accRepo.findAccount(acc.getUsername(), acc.getPassword());
        if (accList.size() == 1) {
            model.addAttribute("page", "Home");
            session.setAttribute("account", accList.get(0));
            return "home";
        } else {
            model.addAttribute("page", "Login");
            model.addAttribute("msg", "Wrong username or password!");
            return "login";
        }
    }
}
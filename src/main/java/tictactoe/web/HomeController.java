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
    public String login(Model model, HttpSession session) {

        if(session.getAttribute("account") != null){
            return "redirect:/home";
        }
        model.addAttribute("account", new Account());
        model.addAttribute("page", "Login");
        return "login";
    }
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        if(session.getAttribute("account") == null){
            return "redirect:/";
        }
        Account temp = (Account) session.getAttribute("account");
        accRepo.online(temp.getId());
        ArrayList<Account> online = accRepo.findActiveAccount();
        session.setAttribute("online", online);
        model.addAttribute("page", "Home");
        return "home";
    }
    @GetMapping("logout")
    public String logOut(Model model, HttpSession session) {
        
        session.removeAttribute("account");
        return "redirect:/";
    }

    @PostMapping
    public String checkLogin(Account acc, Model model, HttpSession session) {

        ArrayList<Account> accList = accRepo.findAccount(acc.getUsername(), acc.getPassword());
        if (accList.size() == 1) {
            session.setAttribute("account", accList.get(0));
            return "redirect:/home";
        } else {
            model.addAttribute("page", "Login");
            model.addAttribute("msg", "Wrong username or password!");
            return "login";
        }
    }
}
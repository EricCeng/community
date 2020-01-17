package life.drift.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author 51514
 */
@Controller
public class IndexController {

    @GetMapping("/index")
    public String hello() {
        return "index";
    }

}

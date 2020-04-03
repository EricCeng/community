package life.drift.community.controller;

import life.drift.community.dto.PaginationDTO;
import life.drift.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 51514
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping({"/", "/index"})
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search
    ) {
        //显示条目信息
        PaginationDTO pagination = questionService.list(search, page, size);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "index";
    }

}

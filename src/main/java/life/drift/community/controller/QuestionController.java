package life.drift.community.controller;

import life.drift.community.dto.CommentDTO;
import life.drift.community.dto.QuestionDTO;
import life.drift.community.enums.CommentTypeEnum;
import life.drift.community.service.CommentService;
import life.drift.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model) {

        QuestionDTO questionDTO = questionService.getById(id);

        //搜索相关问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);

        List<CommentDTO> commentDTOList = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOList);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}

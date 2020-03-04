package life.drift.community.controller;

import life.drift.community.dto.NotificationDTO;
import life.drift.community.dto.PaginationDTO;
import life.drift.community.enums.NotificationEnum;
import life.drift.community.mapper.NotificationMapper;
import life.drift.community.model.User;
import life.drift.community.service.NotificationService;
import life.drift.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}") //动态切换路径
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        // 如果用户未登陆，将跳转到首页进行登陆

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterid();
        } else {
            return "redirect:";
        }
    }
}

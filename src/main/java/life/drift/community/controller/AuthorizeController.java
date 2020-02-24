package life.drift.community.controller;

import life.drift.community.dto.AccessTokenDTO;
import life.drift.community.dto.GithubUser;
import life.drift.community.mapper.UserMapper;
import life.drift.community.model.User;
import life.drift.community.provider.GithubProvider;
import life.drift.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author 51514
 */
@Controller
public class AuthorizeController {

    @Autowired
    //该注解，自动将容器中实例化好的实例加载到当前使用的上下文

    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    //读取配置文件中的值，并赋值
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if (githubUser != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId())); // 强转
            user.setAvatarUrl(githubUser.getAvatar_url());

            userService.createOrUpdate(user);

            //登陆成功，自动写入 cookie，判断 token
            response.addCookie(new Cookie("token", token));

            return "redirect:";
        } else {
            //登陆失败，重新登陆
            return "redirect:";
        }
    }

    //退出登陆
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:";
    }
}

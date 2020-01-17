package life.drift.community.provider;

import com.alibaba.fastjson.JSON;
import life.drift.community.dto.AccessTokenDTO;
import life.drift.community.dto.GithubUser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

/**
 * @author 51514
 */
@Component
//这个注解，就不需要实例化对象了 ------- @AutoWired --- IoC，控制反转，由容器来管理对象

public class GithubProvider {
    //习惯：参数超过两个对象，则最好是进行封装 ---- AccessTokenDTO

    //POST
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post((okhttp3.RequestBody) body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

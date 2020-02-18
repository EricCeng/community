package life.drift.community.dto;

import lombok.Data;

/**
 * @author 51514
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;

}

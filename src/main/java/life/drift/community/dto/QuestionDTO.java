package life.drift.community.dto;

import life.drift.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private String tag;
    private Long viewCount;
    private Long commentCount;
    private Long likeCount;
    private User user;
}

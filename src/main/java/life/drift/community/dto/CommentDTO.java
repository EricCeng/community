package life.drift.community.dto;

import life.drift.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long commentCount;
    private Long likeCount;
    private String content;
    private User user;
}

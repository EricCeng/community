package life.drift.community.mapper;

import life.drift.community.model.Comment;
import life.drift.community.model.CommentExample;
import life.drift.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}
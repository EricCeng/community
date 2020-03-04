package life.drift.community.mapper;

import life.drift.community.model.Question;
import life.drift.community.model.QuestionExample;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);
}
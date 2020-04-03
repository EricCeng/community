package life.drift.community.mapper;

import life.drift.community.dto.QuestionQueryDTO;
import life.drift.community.model.Question;
import life.drift.community.model.QuestionExample;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
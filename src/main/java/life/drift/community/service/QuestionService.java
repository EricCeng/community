package life.drift.community.service;

import life.drift.community.dto.QuestionDTO;
import life.drift.community.mapper.QuestionMapper;
import life.drift.community.mapper.UserMapper;
import life.drift.community.model.Question;
import life.drift.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Service 的目的是，作为中间层去组装 User 和 Question
@Service
public class QuestionService {
    @Autowired(required=false)
    private QuestionMapper questionMapper;

    @Autowired(required=false)
    private UserMapper userMapper;

    public List<QuestionDTO> list() {
        List<Question> questions = questionMapper.list();
        List<QuestionDTO> questionDTOList =new ArrayList<>();
        for(Question question:questions){
            //将question转化成DTO
            QuestionDTO questionDTO = new QuestionDTO();
            User user = userMapper.findById(question.getCreator());
            BeanUtils.copyProperties(question, questionDTO);//这个工具类的目的是，快速将对象属性拷贝到对象上（反射）
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}

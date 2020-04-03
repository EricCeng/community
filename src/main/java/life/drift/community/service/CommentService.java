package life.drift.community.service;

import life.drift.community.dto.CommentDTO;
import life.drift.community.enums.CommentTypeEnum;
import life.drift.community.enums.NotificationEnum;
import life.drift.community.enums.NotificationStatusEnum;
import life.drift.community.exception.CustomizeErrorCode;
import life.drift.community.exception.CustomizeException;
import life.drift.community.mapper.*;
import life.drift.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    // @Transactional 可给整个方法体添加事务，自动管理事务 当一条语句执行成功另一条失败时，事务将会发生回滚
    // 简单来说：评论数若因错误增加失败，评论插入也不会执行

    //    从业务上来说，一次交互产生的数据保持同一性，否则从业务层面认为这就是脏数据，但是数据库认为这是合理的。
//    为了保证数据的有效，需要功能入口统一管理事务，不仅仅限于是几个表的操作，可能是多个SQL的操作。
//    从设计上来说，为了规范代码写法，可能要去入口service全部加入事务控制，便于后续扩展。
    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            // 回复评论
            // dbComment 是评论者
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);

            //二级评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1L);
            commentExtMapper.incCommentCount(parentComment);

            //创建通知 评论回复
            createNotify(comment, dbComment.getCommentator(), dbComment.getContent(), commentator.getName(), NotificationEnum.REPLY_COMMENT, question.getId());


        } else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            comment.setCommentCount(0L);
            commentMapper.insert(comment);

            //评论数
            question.setCommentCount(1L);
            questionExtMapper.incCommentCount(question);
            // question 表的评论数无法实时更新，可尝试触发器调用存储过程？？？

            //创建通知 问题回复
            createNotify(comment, question.getCreator(), question.getTitle(), commentator.getName(), NotificationEnum.REPLY_QUESTION, question.getId());
        }
    }

    //创建通知 封装方法
    private void createNotify(Comment comment, Long receiver, String outerTitle, String notifierName, NotificationEnum notificationType, Long outerId) {
        if (receiver.equals(comment.getCommentator())) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        if (commentList.size() == 0) {
            return new ArrayList<>();
        }

        // java8 语法
        // 获取去重的评论人
        Set<Long> commentators = commentList.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        // 获取评论人，并转换为 Map ---> 降低时间复杂度
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 将 comment 转换为 commentDTO
        List<CommentDTO> commentDTOS = commentList.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
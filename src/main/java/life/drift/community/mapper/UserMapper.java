package life.drift.community.mapper;

import life.drift.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Insert("insert into USER (account_id,name,gmt_create,gmt_modified) values (#{accountId},#{name},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}

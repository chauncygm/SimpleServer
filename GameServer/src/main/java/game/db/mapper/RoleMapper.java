package game.db.mapper;

import game.db.beans.Role;
import game.db.beans.RoleExample;
import game.db.beans.RoleKey;
import game.db.beans.RoleWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    long countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(RoleKey key);

    int insert(RoleWithBLOBs record);

    int insertSelective(RoleWithBLOBs record);

    List<RoleWithBLOBs> selectByExampleWithBLOBs(RoleExample example);

    List<Role> selectByExample(RoleExample example);

    RoleWithBLOBs selectByPrimaryKey(RoleKey key);

    int updateByExampleSelective(@Param("record") RoleWithBLOBs record, @Param("example") RoleExample example);

    int updateByExampleWithBLOBs(@Param("record") RoleWithBLOBs record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(RoleWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(RoleWithBLOBs record);

    int updateByPrimaryKey(Role record);
}
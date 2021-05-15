package game.db.dao;

import game.db.DBFactory;
import game.db.beans.RoleWithBLOBs;
import game.db.mapper.RoleMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gongshengjun
 * @date 2021/5/14 17:39
 */
public class RoleDao {

    private static final Logger logger = LoggerFactory.getLogger(RoleDao.class);

    public void insert(RoleWithBLOBs role) {
        try (SqlSession session = DBFactory.DB_GAME.getFactory().openSession()){
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            mapper.insert(role);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public void update(RoleWithBLOBs role) {
        try (SqlSession session = DBFactory.DB_GAME.getFactory().openSession()){
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            int result = mapper.updateByPrimaryKeyWithBLOBs(role);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }
}

package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 通过rid和uid查询favorite表格，如果有记录，说明用户已经收藏
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        String sql = "select * from tab_favorite where rid = ? and uid = ?";
        Favorite favorite = null;
        //将来没有查询到，这边会抛出异常
        try {
            favorite = template.queryForObject(sql,new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return favorite;
    }

    /**
     * 在 favorite表中增加一条记录
     * @param rid
     * @param uid
     */
    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql,rid,new Date(),uid);
    }

    /**
     * 在route表格中更新count
     * @param rid
     */
    @Override
    public void updateCount(int rid) {
        String sql = "update tab_route set count = count+1 where rid = ?";
        template.update(sql,rid);
    }

}

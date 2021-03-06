package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 根据cid 查询总记录数
     * @param cid
     * @return
     */
    @Override
    public int findTotalCount(int cid,String rname) {
        //两个参数可以存在可以不存在！多条件的组合查询->拼接sql
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List<Object> params = new ArrayList<>();
        //判断参数是否有值
        if (cid!=0) {
            sb.append("and cid=? ");
            params.add(cid);
        }
        if (rname != null && rname.length() != 0) {
            sb.append("and rname like ?");
            params.add("%"+rname+"%");
        }
        sql = sb.toString();
        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    /**
     * 查询当前页的所有旅游线路数据
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List<Object> params = new ArrayList<>();
        if (cid!=0) {
            sb.append("and cid=? ");
            params.add(cid);
        }
        if (rname != null && rname.length() != 0) {
            sb.append("and rname like ? ");
            params.add("%"+rname+"%");
        }
        sb.append("limit ?,? ");
        sql=sb.toString();
        params.add(start);
        params.add(pageSize);
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }

    /**
     * 根据id查询tab_route中的信息并封装到route类
     * @param rid
     * @return
     */
    @Override
    public Route findById(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }
}

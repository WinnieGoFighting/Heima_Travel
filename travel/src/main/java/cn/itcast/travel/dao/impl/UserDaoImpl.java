package cn.itcast.travel.dao.impl;
import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据用户查询用户信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            //1 定义sql
            String sql = "select * from tab_user where username = ?";
            //2 执行sql
            //如果找不到结果，封装不上，会报异常，所以需要 try..catch来处理异常
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 保存用户
     * @param user
     */
    @Override
    public void save(User user) {
        String sql = "insert into tab_user values (null,?,?,?,?,?,?,?,?,?)";
//        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        template.update(sql,user.getUsername(),
                            user.getPassword(),
                            user.getName(),
                            user.getBirthday(),
                            user.getSex(),
                            user.getTelephone(),
                            user.getEmail(),
                            user.getStatus(),
                            user.getCode());
    }

    /**
     *根据用户的激活码查询用户
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        User user = null;
        String sql = "select * from tab_user where code=?";
        try {
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 更新用户的激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        String sql = "update tab_user set status = 'Y' where uid=? ";
        template.update(sql,user.getUid());
    }

    /**
     * 根据用户名和密码查询的方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null;
        try {
            //1 定义sql
            String sql = "select * from tab_user where username = ? and password=?";
            //2 执行sql
            //如果找不到结果，封装不上，会报异常，所以需要 try..catch来处理异常
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}

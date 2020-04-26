package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        //1 根据用户名查询用户对象 （对应dao的findByUsername方法）
        //2 保存对象信息 （对应dao的save()方法）
        User u = userDao.findByUsername(user.getUsername());
        if (u!=null) {
            //用户名存在，注册失败
            return false;
        }
        //设置激活码和激活状态
        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        userDao.save(user);

        //发送激活邮件
        //这边的href要写绝对路径
        String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活[黑马旅游网]</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if (user!=null) {
            //2. 调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        }
        else
            return false;
    }

    /**
     * 登录方法
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }


}

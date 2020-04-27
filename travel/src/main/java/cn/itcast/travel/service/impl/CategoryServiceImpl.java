package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();
    /**
     *
     * @return
     */
    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        //1.1 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2 使用sortedset排序查询
        Set<Tuple> categories = jedis.zrangeWithScores("category", 0, -1);
        List<Category> list = new ArrayList<>();
        if (categories==null||categories.size()==0) {
            System.out.println("从数据库查询。。");
            //从数据库查询，再将数据存入redis
            list = categoryDao.findAll();
            for (int i = 0; i < list.size(); i++) {
                jedis.zadd("category",list.get(i).getCid(),list.get(i).getCname());
            }
        }
        else {
            System.out.println("从redis中取出。。。");
            //从redis中拿到数据封装到list
            for(Tuple elem:categories) {
                Category c = new Category();
                c.setCid((int)elem.getScore());
                c.setCname(elem.getElement());
                list.add(c);
            }
        }
        return list;
    }
}

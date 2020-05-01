package cn.itcast.travel.service.impl;
import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 查询用户是否收藏该线路
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(int rid, int uid) {
        Favorite favorite = favoriteDao.findByRidAndUid(rid,uid);
        return favorite!=null;
    }

    /**
     * 更新收藏数
     * @param rid
     * @param uid
     */
    @Override
    public void add(int rid, int uid) {
        //跟新两个表格 favorite 和 route, 需要两个dao
        favoriteDao.add(rid,uid);
        favoriteDao.updateCount(rid);
    }
}

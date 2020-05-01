package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao  = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 通过查询数据库组装PageBean对象并返回
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        //pageBean 中的五个数据
        //    private int totalCount;
        //    private int totalPage;
        //    private int currentPage;
        //    private int pageSize;
        //    private List<T> list;
        PageBean<Route> pb = new PageBean<>();
        //1.当前页数
        pb.setCurrentPage(currentPage);
        //2.每页显示条数
        pb.setPageSize(pageSize);
        //3.总条目数
        int totalCount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);

        //4.当前页的数据集合
        int start = (currentPage-1)*pageSize;
        List<Route> list = routeDao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);

        //4.总页数
        int totalPage = totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
        pb.setTotalPage(totalPage);

        return pb;
    }

    /**
     * 根据rid查询route的详细信息(结合三张表route,img, seller，favorite得到)
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        Route route = routeDao.findById(rid);

        //根据rid查询图片信息并设置回route类
        List<RouteImg> routeImgList = routeImgDao.findById(rid);
        route.setRouteImgList(routeImgList);

        //根据rid查询卖家信息并设置回route类
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        return route;
    }
}

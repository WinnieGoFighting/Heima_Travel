package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    RouteDao routeDao = new RouteDaoImpl();
    /**
     * 通过查询数据库组装PageBean对象并返回
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize) {
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
        int totalCount = routeDao.findTotalCount(cid);
        pb.setTotalCount(totalCount);

        //4.当前页的数据集合
        int start = (currentPage-1)*pageSize;
        List<Route> list = routeDao.findByPage(cid,start,pageSize);
        pb.setList(list);

        //4.总页数
        int totalPage = totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
        pb.setTotalPage(totalPage);

        return pb;
    }
}

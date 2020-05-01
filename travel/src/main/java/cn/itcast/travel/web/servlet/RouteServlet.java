package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String currentPage_str = request.getParameter("currentPage");
        String pageSize_str = request.getParameter("pageSize");
        String cid_str = request.getParameter("cid");

        //增加一个参数，rname
        //tomcat7 get 请求中文乱码问题，需要解决
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        int cid = 0;
        if (cid_str != null && cid_str.length()>0&&!"null".equals(cid_str)) {
            cid = Integer.parseInt(cid_str);
        }


        int currentPage = 1; //当前页码，默认第一页
        if (currentPage_str != null && currentPage_str.length()>0) {
            currentPage = Integer.parseInt(currentPage_str);
        }

        int pageSize = 5; //默认一页显示五条记录
        if (pageSize_str != null && pageSize_str.length()>0) {
            pageSize = Integer.parseInt(pageSize_str);
        }

        //调用service返回PageBean
        PageBean<Route> pb = service.pageQuery(cid,currentPage,pageSize,rname);

        //序列化PageBean返回给前端
        writeValue(pb,response);
    }

    /**
     * 根据id查询旅游线路的详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受rid
        String rid_str = request.getParameter("rid");
        int rid = 0;
        if (rid_str!=null&&rid_str.length()!=0)
            rid = Integer.parseInt(rid_str);
        //2.调用service查询route对象
        Route route = service.findOne(rid);
        //3.转为json写回客户端
        writeValue(route,response);
    }

    /**
     * 判断当前用户是否收藏过该线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取rid
        String rid_str = request.getParameter("rid");
        int rid = Integer.parseInt(rid_str);


        //获取用户
        User user = (User)request.getSession().getAttribute("user");
        int uid = 0;
        if (user != null)
            uid = user.getUid();

        //调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid,uid);

        //把flag写回前端
        writeValue(flag,response);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取线路rid
        String rid_str = request.getParameter("rid");
        int rid = Integer.parseInt(rid_str);

        //获取当前用户
        User user = (User)request.getSession().getAttribute("user");
        int uid;
        if (user == null)
            return;
        else
            uid = user.getUid();

        //调用service添加
        favoriteService.add(rid,uid);

    }

}

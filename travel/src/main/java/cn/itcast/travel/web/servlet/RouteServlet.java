package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;
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

        int cid = 0;
        if (cid_str != null && cid_str.length()>0) {
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
        PageBean<Route> pb = service.pageQuery(cid,currentPage,pageSize);

        //序列化PageBean返回给前端
        writeValue(pb,response);
    }

}

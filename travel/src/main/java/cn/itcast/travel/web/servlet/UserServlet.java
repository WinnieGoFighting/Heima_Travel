package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    private UserService service = new UserServiceImpl();
    /**
     * 用户注册
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //0. 校验验证码
        String code = request.getParameter("check");
        //0.1 从session中获取验证码
        HttpSession session = request.getSession();
        String realCode = (String)session.getAttribute("CHECKCODE_SERVER");
        //防止验证码复用，需要从session中移除验证码
        session.removeAttribute("CHECKCODE_SERVER");

        ResultInfo info = new ResultInfo();
        if (realCode==null||!realCode.equalsIgnoreCase(code)) {
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //info转换为json响应回去
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(info);
//            response.setContentType("application/json;charset=utf-8");
//            response.getWriter().write(json);
            writeValue(info,response);
            return;
        }
        //1. 获取数据
        Map<String,String[]> map = request.getParameterMap();
        //2. 封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3. 调用service完成注册
        //UserService service = new UserServiceImpl();
        boolean flag = service.register(user);
        //ResultInfo info = new ResultInfo();
        if(flag) {
            //注册成功
            info.setFlag(true);
        }
        else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
        }
        //4. 响应结果：将info对象序列化为json
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(info);
//        response.setContentType("application/json;charset=utf-8");
//        response.getWriter().write(json);
        writeValue(info,response);
    }

    /**
     * 用户激活
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            //调用service完成激活
            //UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            //判断标记
            String msg;
            if(flag) {
                //激活成功
                msg = "激活成功，请<a href='"+ request.getContextPath()+"/login.html'>登录</a>";
            }
            else {
                //激活失败
                msg = "激活失败，请联系管理员";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    /**
     * 用户登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        //2. 封装user对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3. 调用service方法
        //UserService service = new UserServiceImpl();
        User u = service.login(user);

        //4.判断用户对象是否为null
        ResultInfo info = new ResultInfo();
        if (u==null) {
            //用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        else {
            //再判断用户是否激活
            if ("N".equals(u.getStatus())) {
                info.setFlag(false);
                info.setErrorMsg("您的账户尚未激活");
            }
            else {
                //把user信息存入session
                HttpSession session = request.getSession();
                session.setAttribute("user",u);
                info.setFlag(true);
            }
        }
        //响应数据
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(),info);
        writeValue(info,response);
    }

    /**
     * 从session中取出用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User u = (User)request.getSession().getAttribute("user");
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(),u);
        writeValue(u,response);

    }

    /**
     * 用户退出登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.销毁session
        request.getSession().invalidate();
        //2.跳转登陆页面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

}

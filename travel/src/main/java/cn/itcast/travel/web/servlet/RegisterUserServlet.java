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

@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
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
        UserService service = new UserServiceImpl();
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
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

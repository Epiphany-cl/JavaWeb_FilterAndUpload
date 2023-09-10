package servlet;

import com.google.gson.Gson;
import utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/UserServlet")
public class UserServlet extends BaseServlet {
    protected void userLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("user", "admin");

        resp.setContentType("application/json;charset=utf-8");
        Gson gson = new Gson();
        String json = gson.toJson("登陆成功！");
        resp.getWriter().write(json);
    }

    protected void userLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();

        resp.setContentType("application/json;charset=utf-8");
        Gson gson = new Gson();
        String json = gson.toJson("已退出~");
        resp.getWriter().write(json);
    }
}

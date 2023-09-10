package filter;


import com.google.gson.Gson;
import utils.BaseServlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;

@WebFilter(filterName = "AdminFilter", urlPatterns = "/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String user = (String) req.getSession().getAttribute("user");
        if (user != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            resp.setContentType("application/json;charset=utf-8");
            Gson gson = new Gson();
            String json = gson.toJson("请登录~");
            resp.getWriter().write(json);
        }
    }

    @Override
    public void destroy() {

    }
}

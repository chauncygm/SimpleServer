package com.interceptor;

import com.constants.Constant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute(Constant.SESSION_KEY) != null) {
            return true;
        }
        // 跳转到登录页
        String url = "/login?redirect=true";
        response.sendRedirect(request.getContextPath() + url);
        return false;
    }
}

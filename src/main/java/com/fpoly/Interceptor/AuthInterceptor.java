package com.fpoly.Interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws IOException {
		
		HttpSession session = request.getSession();
		
		
		if(session.getAttribute("user") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			session.setAttribute("message", "Please Login");
			return false;
		} 
		return true;
	}
}

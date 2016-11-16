package bbs.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bbs.beans.User;

@WebFilter("/*")
public class AdminFilter implements Filter {

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		String target = ((HttpServletRequest)request).getRequestURI();
		String[] login = target.split("/");

			if(login[login.length -1].matches("userControl") ||login[login.length -1].matches("signup") ||
					login[login.length -1].matches("settings")) {
				HttpSession session = ((HttpServletRequest) request).getSession();
				User user = (User) session.getAttribute("loginUser");

				if(user == null) {
					((HttpServletResponse) response).sendRedirect("./login");
					return;
				}

				if(user.getDepartmentId() != 1) {
					((HttpServletResponse) response).sendRedirect("./");
					return;
				}
			}
			chain.doFilter(request, response);
	}


	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void destroy() {
	}

}
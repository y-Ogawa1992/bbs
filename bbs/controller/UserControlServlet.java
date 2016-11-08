package bbs.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bbs.beans.User;
import bbs.service.UserService;

@WebServlet(urlPatterns = { "/userControl"})
public class UserControlServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {


		List<User> Users = new UserService().getUsers();

		request.setAttribute("Users", Users);
		request.getRequestDispatcher("/userControl.jsp").forward(request,  response);

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		User user = new User();
		user.setId(Integer.valueOf(request.getParameter("id")));
		user.setStop(Boolean.valueOf(request.getParameter("stop")));

		new UserService().stop(user, user.getId(), user.getStop());

		List<User> Users = new UserService().getUsers();
		request.setAttribute("Users", Users);
		request.getRequestDispatcher("/userControl.jsp").forward(request,  response);
	}

}

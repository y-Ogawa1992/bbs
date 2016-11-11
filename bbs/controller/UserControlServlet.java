package bbs.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bbs.beans.Branch;
import bbs.beans.Department;
import bbs.beans.User;
import bbs.service.BranchService;
import bbs.service.DepartmentService;
import bbs.service.UserService;

@WebServlet(urlPatterns = { "/userControl"})
public class UserControlServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("loginUser");

		List<Department> department = new DepartmentService().getDepartment();
		List<Branch> branch = new BranchService().getBranch();
		List<User> Users = new UserService().getUsers();


		request.setAttribute("user", user);
		request.setAttribute("department", department);
		request.setAttribute("branch", branch);
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

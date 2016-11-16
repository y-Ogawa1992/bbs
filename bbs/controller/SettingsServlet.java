package bbs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import bbs.beans.Branch;
import bbs.beans.Department;
import bbs.beans.User;
import bbs.service.BranchService;
import bbs.service.DepartmentService;
import bbs.service.UserService;

@WebServlet(urlPatterns = { "/settings"})
public class SettingsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
IOException {

		User user = new User();
		user.setId(Integer.valueOf(request.getParameter("id")));

		List<Branch> branch = new BranchService().getBranch();
		List<Department> department = new DepartmentService().getDepartment();

		request.setAttribute("department", department);
		request.setAttribute("branch", branch);

		//getIdで探したユーザー情報を編集画面表示時にセット
		HttpSession session = request.getSession();
		User editUser = new UserService().getUser(user.getId());
		session.setAttribute("editUser", editUser);


		request.getRequestDispatcher("settings.jsp").forward(request, response);
	}


	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		List<String> messages = new ArrayList<String>();

		HttpSession session = request.getSession();

		User editUser = getEditUser(request);
		session.setAttribute("editUser", editUser);

		if (isValid(request, messages) == true) {
			new UserService().update(editUser);

			request.setAttribute("loginUser", editUser);
			request.setAttribute("editUser", editUser);

			response.sendRedirect("userControl");
		} else {
			session.setAttribute("errorMessages", messages);
			request.setAttribute("editUser", editUser);
			response.sendRedirect("settings?id=" + editUser.getId());//ここ
		}
	}


	private User getEditUser(HttpServletRequest request) throws IOException, ServletException {

		HttpSession session = request.getSession();
		User editUser = (User) session.getAttribute("editUser");

		editUser.setLoginId(request.getParameter("loginId"));
		editUser.setPassword(request.getParameter("password"));
		editUser.setName(request.getParameter("name"));
		editUser.setBranchId(Integer.valueOf(request.getParameter("branch")));
		editUser.setDepartmentId(Integer.valueOf(request.getParameter("department")));
		return editUser;
	}


	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String name = request.getParameter("name");
		String loginId = request.getParameter("loginId");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		User user = new UserService().getUserId(loginId);



		if(StringUtils.isEmpty(name) == true) {
			messages.add("名前を入力してください");
		}

		if(StringUtils.isEmpty(loginId) == true) {
			messages.add("ログインIDを入力してください");
		}else if(loginId == user.getLoginId()) {
			messages.add("このIDは既に使用されています");//適応されていない
		}

		if(!password.equals(password2)){
			messages.add("変更用、確認用パスワードに相違があります");
		}

		if(messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}


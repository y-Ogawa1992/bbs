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
import bbs.exception.NoRowsUpdatedRuntimeException;
import bbs.service.BranchService;
import bbs.service.DepartmentService;
import bbs.service.UserService;

@WebServlet(urlPatterns = { "/settings"})
public class SettingsServlet extends HttpServlet {

	//【パスワードが空白の場合は変更無し】の処理をしていない
	//【所属と役職が初期値で渡っていない】

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
IOException {

		User user = new User();
		user.setId(Integer.valueOf(request.getParameter("id")));

		List<Branch> branch = new BranchService().getBranch();
		List<Department> department = new DepartmentService().getDepartment();

		//getIdで探したユーザー情報を編集画面表示時にセット
		HttpSession session = request.getSession();
		User editUser = new UserService().getUser(user.getId());
		session.setAttribute("editUser", editUser);
		session.setAttribute("department", department);
		session.setAttribute("branch", branch);

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

			try {
				new UserService().update(editUser);
			} catch (NoRowsUpdatedRuntimeException e) {
				session.removeAttribute("editUser");
				messages.add("他の人によって更新されています。最新のデータを表示しました。データを確認してください。");
				session.setAttribute("errorMessages", messages);
				response.sendRedirect("settings");
			}

			session.setAttribute("loginUser", editUser);
			session.removeAttribute("editUser");

			response.sendRedirect("settings.jsp");
		} else {
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("settings.jsp");
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

		String loginId = request.getParameter("loginId");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		if(StringUtils.isEmpty(loginId) == true) {
			messages.add("ログインIDを入力してください");
		}
		if(StringUtils.isEmpty(password) == true) {
			messages.add("パスワードを入力してください");
		}
		if(password != password2){
			messages.add("パスワードが違います");
		}
		//TODO アカウントがすでに利用されていないか、メールアドレスがすでに登録されていないかどうかなどの確認も必要
		if(messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}


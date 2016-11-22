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

		List<String> messages = new ArrayList<String>();
		HttpSession session = request.getSession();

		//getParameterで取った値が数字であるか
		//～～～～～～で取った値がDBに存在するか否か
		String userId = request.getParameter("id");

		if(StringUtils.isEmpty(userId) == true){
			messages.add("無効な操作が行われました");
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("./userControl");
			return;
		}
		if(!userId.matches("^[0-9]*$")) {
			messages.add("無効な操作が行われました");
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("./userControl");
			return;
		}
		int userid = Integer.parseInt(userId);

		User foundUser = new UserService().getUser(userid);
		if(foundUser == null) {
			messages.add("存在しないユーザーです");
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("./userControl");
			return;
		}


		List<Branch> branch = new BranchService().getBranch();
		List<Department> department = new DepartmentService().getDepartment();

		request.setAttribute("department", department);
		request.setAttribute("branch", branch);
		//getIdで探したユーザー情報を編集画面表示時にセット

		User editUser = new UserService().getUser(userid);
		session.setAttribute("editUser", editUser);
		request.setAttribute("editUser", editUser);

		request.getRequestDispatcher("settings.jsp").forward(request, response);
	}


	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		List<String> messages = new ArrayList<String>();

		HttpSession session = request.getSession();
		User editUser = getEditUser(request, response);
		User user = (User) session.getAttribute("loginUser");

		if (isValid(request, messages) == true) {
			new UserService().update(editUser);


			if(user.getId() == editUser.getId()){
				session.setAttribute("loginUser", editUser);
				response.sendRedirect("userControl");
			}

			if(editUser.getId() != user.getId()){
				request.setAttribute("loginUser", editUser);
				response.sendRedirect("userControl");
			}
		} else {

			List<Branch> branch = new BranchService().getBranch();
			List<Department> department = new DepartmentService().getDepartment();

			session.setAttribute("errorMessages", messages);
			request.setAttribute("editUser", editUser);
			request.setAttribute("department", department);
			request.setAttribute("branch", branch);

			request.getRequestDispatcher("settings.jsp").forward(request, response);

		//	response.sendRedirect("settings?id=" + editUser.getId());

		}
	}


	private User getEditUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		User editUser = (User) session.getAttribute("editUser");

		editUser.setLoginId(request.getParameter("loginId"));
		editUser.setPassword(request.getParameter("password"));
		editUser.setName(request.getParameter("name"));
		editUser.setBranchId(Integer.valueOf(request.getParameter("branch")));
		editUser.setDepartmentId(Integer.valueOf(request.getParameter("department")));
		System.out.println(editUser.getBranchId());
		System.out.println(editUser.getDepartmentId());
		return editUser;
	}

	//ここから選択できるけど変更できない＆エラーメッセージ出す処理
//		User user = (User) session.getAttribute("loginUser");
//		int bId = Integer.valueOf(request.getParameter("branch"));
//		int dId = Integer.valueOf(request.getParameter("department"));
//		List<Branch> branch = new BranchService().getBranch();
//		List<Department> department = new DepartmentService().getDepartment();
//
//		List<String> messages = new ArrayList<String>();

//		if(user.getId() != editUser.getId()) {
//			editUser.setBranchId(Integer.valueOf(request.getParameter("branch")));
//			editUser.setDepartmentId(Integer.valueOf(request.getParameter("department")));
//		}else if(user.getId() == editUser.getId() && bId != 1 && dId != 1){
//			messages.add("人事総務担当者は自身の所属、役職の変更は出来ません。");
//			session.setAttribute("errorMessages", messages);
//			request.setAttribute("editUser", editUser);
//			request.setAttribute("department", department);
//			request.setAttribute("branch", branch);
//			request.getRequestDispatcher("settings.jsp").forward(request, response);
//		}



	private boolean isValid(HttpServletRequest request, List<String> messages) throws IOException, ServletException {

		String name = request.getParameter("name");
		String loginId = request.getParameter("loginId");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		HttpSession session = request.getSession();
		User editUser = (User) session.getAttribute("editUser");
		User user = new UserService().getUserId(loginId);


		if(StringUtils.isEmpty(name) == true) {
			messages.add("名前を入力してください");
		}else if(10 < name.length()) {
			messages.add("名前は10文字以下で入力してください");
		}

		//ログインID入力チェック、ID重複チェック時に変更無し処理
		if(StringUtils.isEmpty(loginId) == true) {
			messages.add("ログインIDを入力してください");
		}else if(!loginId.matches("^[a-zA-Z0-9]{6,20}$")) {
			messages.add("ログインIDは半角英数字6文字から20文字で入力してください");
		}else if(user != null && user.getId() != editUser.getId()) {
			messages.add("このログインIDは既に使用されています");
		}

		if(StringUtils.isEmpty(password) == false && !password.matches("^[a-zA-Z0-9!-/:-@¥[-`{-~]]{6,255}$")){
			messages.add("パスワードは6文字以上で入力してください");
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


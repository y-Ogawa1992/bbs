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


@WebServlet(urlPatterns = { "/signup" })
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		//①最初に表示するため
		List<Branch> branch = new BranchService().getBranch();
		List<Department> department = new DepartmentService().getDepartment();

		User user = new User();
		request.setAttribute("user", user);


		//①最初に表示するため
		request.setAttribute("branch", branch);
		request.setAttribute("department", department);
		request.getRequestDispatcher("/signup.jsp").forward(request,  response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		List<String> messages = new ArrayList<String>();
		HttpSession session = request.getSession();

		User user = new User();
		user.setLoginId(request.getParameter("login_id"));
		user.setPassword(request.getParameter("password"));
		user.setName(request.getParameter("name"));
		user.setBranchId(Integer.valueOf(request.getParameter("branch")));
		user.setDepartmentId(Integer.valueOf(request.getParameter("department")));

		if(isValid(request, messages) == true) {

			new UserService().register(user);
			response.sendRedirect("./userControl");
		} else {
			List<Branch> branch = new BranchService().getBranch();
			List<Department> department = new DepartmentService().getDepartment();


			session.setAttribute("errorMessages", messages);
			request.setAttribute("department", department);
			request.setAttribute("branch", branch);
			request.setAttribute("user", user);
			request.getRequestDispatcher("signup.jsp").forward(request, response);
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {
		String loginId = request.getParameter("login_id");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String password2 = request.getParameter("password2");
		Integer branchId = Integer.valueOf(request.getParameter("branch"));
		Integer departmentId = Integer.valueOf(request.getParameter("department"));

		User user = new UserService().getUserId(loginId);

		if(StringUtils.isEmpty(name) == true) {
			messages.add("名前を入力してください");
		}else if(10 < name.length()) {
			messages.add("名前は10文字以下で入力してください");
		}

		if(StringUtils.isEmpty(loginId) == true) {
			messages.add("ログインIDを入力してください");
		}else if(!loginId.matches("^[a-zA-Z0-9]{6,20}$")) {
			messages.add("ログインIDは半角英数字6文字から20文字で入力してください");
		}else if(user != null) {
			messages.add("このログインIDは既に使用されています");//適応されていない
		}

		if(StringUtils.isEmpty(password) == true) {
			messages.add("パスワードを入力してください");
		}else if(!password.matches("^[a-zA-Z0-9!-/:-@¥[-`{-~]]{6,255}$")) {
			messages.add("パスワードは6文字以上で入力してください");
		}else if(!password.equals(password2)){
			messages.add("確認用パスワードに相違があります");
		}

		if(branchId == 0) {
			messages.add("所属を選択してください");
		}
		if(departmentId == 0) {
			messages.add("役職を選択してください");
		}

		if(messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}


}


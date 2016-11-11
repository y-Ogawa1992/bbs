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

		List<Branch> branch = new BranchService().getBranch();
		List<Department> department = new DepartmentService().getDepartment();

		request.setAttribute("department", department);
		request.setAttribute("branch", branch);
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

			response.sendRedirect("./");
		} else {
			session.setAttribute("errorMessages", messages);
			request.setAttribute("user", user);
			request.getRequestDispatcher("signup.jsp").forward(request, response);;
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {
		String loginId = request.getParameter("login_id");
		String password = request.getParameter("password");
		String name = request.getParameter("name");

		User user = new UserService().getUserId(loginId);

		if(loginId == user.getLoginId()) {
			messages.add("このIDは既に使用されています");
		}
		if(StringUtils.isEmpty(loginId) == true) {
			messages.add("ログインIDを入力してください");
		}
		if(StringUtils.isEmpty(password) == true) {
			messages.add("パスワードを入力してください");
		}
		if (!loginId.matches("^[0-9a-zA-Z]{6,20}$")) {
			messages.add("ログインIDは半角英数字6文字から20文字で入力してください");
		}
		//パスワードに記号も含めたい[ -/:-@\[-\`\{-\~]もしくは^[a-zA-Z0-9 -/:-@\[-\`\{-\~]+$
		if(password.matches("^[0-9a-zA-Z_]{6,255}$")) {
			messages.add("パスワードは6文字以上で入力してください");
		}
		if(10 < name.length()) {
			messages.add("名前は10文字以下で入力してください");
		}


		//TODO アカウントがすでに利用されていないか、メールアドレスがすでに登録されていないかなどの確認も必要
		if(messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}


}


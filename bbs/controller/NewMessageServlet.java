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

import bbs.beans.Message;
import bbs.beans.User;
import bbs.service.MessageService;

@WebServlet(urlPatterns = { "/message"})
public class NewMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Message message = new Message();
		String text = request.getParameter("text");

		request.setAttribute("text", text);
		request.setAttribute("message", message);
		request.getRequestDispatcher("message.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		List<String> messages = new ArrayList<String>();

		User user = (User) session.getAttribute("loginUser");

		Message message = new Message();
		message.setTitle(request.getParameter("title"));
		message.setCategory(request.getParameter("category"));
		message.setText(request.getParameter("text"));
		message.setUserId(user.getId());


		if(isValid(request, messages) == true) {

			new MessageService().register(message);
			response.sendRedirect("./");
		} else {
			session.setAttribute("errorMessages", messages);

			request.setAttribute("message", message);
			request.getRequestDispatcher("message.jsp").forward(request, response);
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String title = request.getParameter("title");
		String message = request.getParameter("text");
		String category = request.getParameter("category");

		if(StringUtils.isEmpty(title) == true || StringUtils.isBlank(title) == true) {
			messages.add("タイトルを入力してください");
		}else if(50 < title.length()) {
			messages.add("タイトルは50文字以下で入力してください");
		}
		if(StringUtils.isEmpty(category) == true || StringUtils.isBlank(category) == true) {
			messages.add("カテゴリーを入力してください");
		}else if(10 < category.length()) {
			messages.add("カテゴリーは10文字以下で入力してください");
		}
		if(StringUtils.isEmpty(message) == true || StringUtils.isBlank(message) == true) {
			messages.add("本文を入力してください");
		}else if(1000 < message.length()) {
			messages.add("本文1000文字以下で入力してください");
		}
		if(messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}

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

import bbs.beans.Comment;
import bbs.beans.User;
import bbs.service.CommentService;

@WebServlet(urlPatterns = { "/newComment"})
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		List<String> messages = new ArrayList<String>();

		if(isValid(request, messages) == true) {

			User user = (User) session.getAttribute("loginUser");


			Comment comment = new Comment();
			comment.setMessageId(Integer.valueOf(request.getParameter("messageId")));
			comment.setUserId(user.getId());
			comment.setText(request.getParameter("text"));

			new CommentService().register(comment);
			response.sendRedirect("./");
		} else {
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("./");
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String comment = request.getParameter("text");

		if(StringUtils.isEmpty(comment) == true) {
			messages.add("コメントを入力してください");
		}
		if(500 < comment.length()) {
			messages.add("コメントは500文字以下で入力してください");
		}
		if(messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}

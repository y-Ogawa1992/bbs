package bbs.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bbs.beans.Comment;
import bbs.beans.Message;
import bbs.beans.User;
import bbs.beans.UserComment;
import bbs.beans.UserMessage;
import bbs.service.CommentService;
import bbs.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		//selectBoxで選択された値を拾ってくる
		String category = request.getParameter("category");
		String minInsertDate = request.getParameter("minInsertDate") + " 00:00:00";
		String maxInsertDate = request.getParameter("maxInsertDate") + " 23:59:59";

		System.out.println(minInsertDate);
		System.out.println(maxInsertDate);

		List<UserMessage> messages = new MessageService().getMessage(category, minInsertDate, maxInsertDate);//
		List<UserComment> comments = new CommentService().getComment();
		List<Message> categories = new MessageService().getCategory();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("loginUser");


		request.setAttribute("user", user);
		request.setAttribute("messages", messages);
		request.setAttribute("comments", comments);
		request.setAttribute("categories", categories);
		request.getRequestDispatcher("/top.jsp").forward(request,  response);

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//投稿と付随するコメントの削除post

		Message message = new Message();
		message.setId(Integer.valueOf(request.getParameter("messageId")));

		Comment comment = new Comment();
		comment.setMessageId(Integer.valueOf(request.getParameter("messageId")));

		new MessageService().delete(message);
		new CommentService().delete(comment);
		response.sendRedirect("./");

	}

}

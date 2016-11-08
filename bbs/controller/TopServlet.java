package bbs.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

		List<UserMessage> messages = new MessageService().getMessage();
		List<UserComment> comments = new CommentService().getComment();

		request.setAttribute("messages", messages);
		request.setAttribute("comments", comments);
		request.getRequestDispatcher("/top.jsp").forward(request,  response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

			User user = (User) session.getAttribute("loginUser");

			Message message = new Message();
			message.setId(Integer.valueOf(request.getParameter("messageId")));

			new MessageService().delete(message);
			response.sendRedirect("./");
	}

}

package bbs.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

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
		//ログイン時はnullが拾える
		String category = request.getParameter("category");
		String minInsertDate = request.getParameter("minInsertDate");
		String maxInsertDate = request.getParameter("maxInsertDate");

		System.out.println(minInsertDate);

		//nullならDBから最古最新を探してsetするif文
		List<UserMessage> oldMessage = new MessageService().getInsertOld();
		List<UserMessage> newMessage = new MessageService().getInsertNew();

		if(StringUtils.isEmpty(minInsertDate) == true && StringUtils.isEmpty(maxInsertDate) == true) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			minInsertDate = sdf.format(oldMessage.get(0).getInsertDate()) + ".0";
			maxInsertDate = sdf.format(newMessage.get(0).getInsertDate()) + ".0";
			System.out.println(minInsertDate);
		}else {
			minInsertDate = minInsertDate + " 00:00:00.0";
			maxInsertDate = maxInsertDate + " 23:59:59.9";
		}

		List<UserMessage> messages = new MessageService().getMessage(category, minInsertDate, maxInsertDate);
		List<UserComment> comments = new CommentService().getComment();
		List<Message> categories = new MessageService().getCategory();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("loginUser");

		String[] minInDa = minInsertDate.split(" ", 0);
		String[] maxInDa = maxInsertDate.split(" ", 0);

		if(ユーザーが選択しないもしくは選択する) {
			request.setAttribute("minInDa", minInDa);
			request.setAttribute("maxInDa", maxInDa);
		}

		request.setAttribute("category", category);
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

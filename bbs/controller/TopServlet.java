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

		//nullならDBから最古最新を探してsetするif文
		List<UserMessage> oldMessage = new MessageService().getInsertOld();
		List<UserMessage> newMessage = new MessageService().getInsertNew();
		String oldInsertDate;
		String newInsertDate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if(oldMessage.size() == 0 && newMessage.size() == 0) {
			oldInsertDate = "0000-00-00 00:00:00.0";
			newInsertDate = "0000-00-00 00:00:00.0";

			String[] minInDa = oldInsertDate.split(" ", 0);
			String[] maxInDa = newInsertDate.split(" ", 0);

			request.setAttribute("minInDa", minInDa);
			request.setAttribute("maxInDa", maxInDa);
		}else if(StringUtils.isEmpty(minInsertDate) == true && StringUtils.isEmpty(maxInsertDate) == true) {
			//nullであればDBから拾う、DBに無ければtop表示
			oldInsertDate = sdf.format(oldMessage.get(0).getInsertDate()) + ".0";
			newInsertDate = sdf.format(newMessage.get(0).getInsertDate()) + ".0";
		}else if(StringUtils.isEmpty(minInsertDate) == false && StringUtils.isEmpty(maxInsertDate) == true) {
			oldInsertDate = minInsertDate + " 00:00:00.0";
			newInsertDate = sdf.format(newMessage.get(0).getInsertDate()) + ".0";

			String[] minInDa = oldInsertDate.split(" ", 0);
			request.setAttribute("minInDa", minInDa);

		}else if(StringUtils.isEmpty(minInsertDate) == true && StringUtils.isEmpty(maxInsertDate) == false) {
			oldInsertDate = sdf.format(oldMessage.get(0).getInsertDate()) + ".0";
			newInsertDate = maxInsertDate + " 23:59:59.9";

			String[] maxInDa = newInsertDate.split(" ", 0);
			request.setAttribute("maxInDa", maxInDa);

		}else {
			//nullで無ければ日付の入力値を判定して、大小逆であれば入れ替えて時間を足す
			int yyyymmdd = minInsertDate.compareTo(maxInsertDate);
			if(yyyymmdd > 0) {
				String insert = minInsertDate;
				minInsertDate = maxInsertDate;
				maxInsertDate = insert;
			}
			oldInsertDate = minInsertDate + " 00:00:00.0";
			newInsertDate = maxInsertDate + " 23:59:59.9";
		}


		List<UserMessage> messages = new MessageService().getMessage(category, oldInsertDate, newInsertDate);
		List<UserComment> comments = new CommentService().getComment();
		List<Message> categories = new MessageService().getCategory();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("loginUser");

		String[] minInDa = oldInsertDate.split(" ", 0);
		String[] maxInDa = newInsertDate.split(" ", 0);

		if(StringUtils.isEmpty(minInsertDate) != true && StringUtils.isEmpty(maxInsertDate) != true) {
			request.setAttribute("minInDa", minInDa);
			request.setAttribute("maxInDa", maxInDa);
		}

		Comment comment = new Comment();

		request.setAttribute("category", category);
		request.setAttribute("categories", categories);
		request.setAttribute("user", user);
		request.setAttribute("messages", messages);
		request.setAttribute("comments", comments);
		request.setAttribute("comment", comment);
		request.getRequestDispatcher("/top.jsp").forward(request,  response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//投稿と付随するコメントの削除post

		//コメントのみ削除しようとしたら
		//投稿のIdがgetParameter出来ないからエラーになる
		String mId = request.getParameter("messageId");
		String cId = request.getParameter("commentId");
		Message message = new Message();
		Comment comment = new Comment();

		if(mId == null && cId != null){
			comment.setId(Integer.valueOf(request.getParameter("commentId")));
			new CommentService().delete(comment);
		}

		if(mId != null && cId == null){
		message.setId(Integer.valueOf(request.getParameter("messageId")));
		new MessageService().delete(message);
		}

		response.sendRedirect("./");
	}

}

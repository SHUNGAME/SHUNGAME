package org.shun.game.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

@WebServlet("/index.html")
public class IndexServlet extends HttpServlet {
	/**
	 * serial id.
	 */
	private static final long serialVersionUID = -7969340550079447685L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/javascript;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		//scoreType=100：周；200：月；300：年
		String scoreType = req.getParameter("scoreType");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userid", "1");
		map.put("username", "测试1");
		map.put("ranking", "1");
		map.put("scoreType", scoreType);
		map.put("score", "100");
		list.add(map);
		map = new HashMap<String,Object>();
		map.put("userid", "2");
		map.put("username", "测试2");
		map.put("ranking", "2");
		map.put("scoreType", scoreType);
		map.put("score", "90");
		list.add(map);
		out.print(new JSONArray(list).toString());
		//super.doPost(req, resp);
	}
}

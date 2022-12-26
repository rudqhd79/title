package controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import DAO.BoardDAO;
import DTO.Board;

@WebServlet("/")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardDAO dao;
	private ServletContext ctx;

	// init 메소드는 한번만 실행된다 (한개의 객체로 공유 할 수 있다)
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dao = new BoardDAO();
		ctx = getServletContext(); // ServletCOntext : 웹 어플리케이션의 자원 관리
	}

	public BoardController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // request 객체에 한글 깨짐 방지
		doPro(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doPro(request, response);
	}

	protected void doPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String context = request.getContextPath();
		String command = request.getServletPath();
		String site = null;

		// 경로 라우팅(경로를 찾아줌)
		switch (command) {
		case "/list":
			site = getList(request);
			break;
		case "/view":
			site = getView(request);
			break;
		case "/write": // 단순하게 화면 보여주는 기능
			site = "write.jsp";
			break;
		case "/insert": //
			site = insertBoard(request);
			break;
		}
		/*
		 * redirect: URL의 변화 O, 객체의 재사용 X (request, response 객체)
		 * DB에 변화가 생기는 요청에 사용 (글쓰기, 회원가입 등등)
		 * 
		 * forward: URL의 변화 X(보안 등), 객체의 재사용 O (request, response 객체)
		 * 단순 조회 (리스트보기, 검색) select...
		 */
		if (site.startsWith("redirect:/")) {
			String rview = site.substring("redirect:/".length());
			System.out.println(rview);
			response.sendRedirect(rview);
		} else {
			ctx.getRequestDispatcher("/" + site).forward(request, response);
		}
	}

	public String getList(HttpServletRequest request) {

		List<Board> list;
		// exception 에러가 뜨는 이유는 getList() 메소드에 throws가 있기 때문에 여기서 exception을 받는 것이다
		try {
			list = dao.getList();
			request.setAttribute("boardList", list);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시판 목록 생성 과정에서 문제 발생");

			// 사용자 한테 에러메시지 보여주기 위해 저장
			request.setAttribute("error", "게시판 목록이 정상적으로 처리되지 않았습니다!");
		}

		return "index.jsp";
	}

	public String getView(HttpServletRequest request) {

		int board_no = Integer.parseInt(request.getParameter("board_no"));
		// exception 에러가 뜨는 이유는 getList() 메소드에 throws가 있기 때문에 여기서 exception을 받는 것이다
		try {
			// 클릭하면 조회수 1씩 증가하는 메소드 추가
			dao.updateViews(board_no);
			Board b = dao.getView(board_no);
			request.setAttribute("board", b);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시글을 가져오는 과정에서 문제 발생");

			// 사용자 한테 에러메시지 보여주기 위해 저장
			request.setAttribute("error", "게시글이 정상적으로 처리되지 않았습니다!");
		}

		return "view.jsp";
	}

	public String insertBoard(HttpServletRequest request) {
		Board b = new Board();
		// 이런 과정 생략 가능
//		b.setUser_id(request.getParameter("user_id");

		try {
			BeanUtils.populate(b, request.getParameterMap());
			dao.insertBoard(b);

		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("추가 과정에서 문제 발생");

			// 사용자 한테 에러메시지 보여주기 위해 저장
			request.setAttribute("error", "게시글이 정상적으로 등록되지 않았습니다!");
			return getList(request);
		}
		return "redirect:/list";

	}
}

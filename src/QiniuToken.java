import Util.JsonBuilder;
import Util.TokenHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class QiniuToken extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        String accessKey = req.getParameter("accessKey");
        String secretKey = req.getParameter("secretKey");
        String bucket = req.getParameter("bucket");
        if (accessKey == null || secretKey == null || bucket == null) {
            resp.getWriter().write(BAD("参数不能为空").getJson());
            return;
        }
        System.out.println(accessKey + "\n" + secretKey + "\n" + bucket);
        TokenHelper tokenHelper = TokenHelper.create(accessKey, secretKey);
        String token = tokenHelper.getToken(bucket);
        String json = buildToken(token).getJson();
        System.out.println(json);
        resp.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(400);
        resp.getWriter().write(BAD("七牛token获取器，请使用POST方法请求该页面").getJson());
    }

    private JsonBuilder buildToken(String token) {
        return getJsonBuilder(200, "success").put("token", token);
    }

    private JsonBuilder BAD(String info) {
        return getJsonBuilder(400, info);
    }

    private JsonBuilder getJsonBuilder(int status, String info) {
        return new JsonBuilder()
                .put("status", status)
                .put("info", info);
    }

}

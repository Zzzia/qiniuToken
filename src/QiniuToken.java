import util.JsonBuilder;
import util.TokenHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class QiniuToken extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        initResult(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        initResult(req, resp);
    }

    private void initResult(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "Authentication");
        resp.setContentType("application/json;charset=utf-8");
        String accessKey = req.getParameter("accessKey");
        String secretKey = req.getParameter("secretKey");
        String bucket = req.getParameter("bucket");
        if (accessKey == null || accessKey.isEmpty() || secretKey == null || secretKey.isEmpty() || bucket == null || bucket.isEmpty()) {
            resp.getWriter().write(BAD("参数不能为空，参数：accessKey,secretKey,bucket").getJson());
            return;
        }
//        System.out.println(accessKey + "\n" + secretKey + "\n" + bucket);
        TokenHelper tokenHelper = TokenHelper.create(accessKey, secretKey);
        String token = tokenHelper.getToken(bucket);
        String json = buildToken(token).getJson();
//        System.out.println(json);
        resp.getWriter().write(json);
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

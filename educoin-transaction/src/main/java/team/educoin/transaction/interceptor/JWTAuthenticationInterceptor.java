package team.educoin.transaction.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import team.educoin.transaction.util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @description:
 * @author: PandaClark
 * @create: 2019-06-08
 */
public class JWTAuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // System.out.println("method:"+request.getMethod());

        // 放行非简单请求的预检，预检会发送一个 OPTIONS 请求，该请求会携带 token 请求头，
        // 但并不会携带 token 的具体字段，即token=NULL，预检不通过导致正式请求无法发送
        if (request.getMethod().equals("OPTIONS")){
            return true;
        }

        String token = request.getHeader("token");
        String uri = request.getRequestURI();

        System.out.println("uri:"+uri);

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("无token，请重新登录");
        }

        // 如果验证失败会抛异常，后面代码不会执行
        Map<String, String> userToken = JWTUtil.verifyToken(token);
        String userId = userToken.get("user_id");
        String userType = userToken.get("user_type");

        switch (userType) {
            case "user":
                if (!uri.startsWith("/user")) {
                    throw new RuntimeException("非法访问！您没有该权限");
                }
                break;
            case "agency":
                if (!uri.startsWith("/agency")) {
                    throw new RuntimeException("非法访问！您没有该权限");
                }
                break;
            case "admin":
                if (!uri.startsWith("/admin")) {
                    throw new RuntimeException("非法访问！您没有该权限");
                }
                break;
            default:
                throw new RuntimeException("未知访问请求");
        }

        request.setAttribute("email", userId);

        return true;
    }
}

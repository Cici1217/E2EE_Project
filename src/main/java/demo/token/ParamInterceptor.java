//package demo.token;
//
////import demo.pojo.chater;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class ParamInterceptor implements HandlerInterceptor {
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("person:");
//        String token = (String) request.getSession().getAttribute("token");
//        System.out.println(token);
//        if(token != null){
//            System.out.println("token yes!");
//            return true;
//        }
//        //如果不满足登陆验证，则跳转到登陆页面
//        System.out.println("token no!");
//        response.sendRedirect("/login.html");
//        return false;
//    }
//
//
//}

//package demo.token;
//
//import io.jsonwebtoken.*;
//import org.springframework.util.StringUtils;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Date;
////JWT工具类
//public class JWTUtils {
//    //密钥
//    private static final String SALT = "kxoif%$*hdas$@_dlsd";
//    //过期时间
//    private static final Integer EXPIRE_TIME = 1000 * 60 * 30;
//    //生成token
//    public static String getToken(String id,String name){
//        String token = Jwts.builder()
//                //设置开始时间
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                //设置有效期
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
//                //设置用户声明
//                .claim("id", id)
//                .claim("name", name)
//                //签名
//                .signWith(SignatureAlgorithm.HS256, SALT)
//                .compact();
//        System.out.println(token);
//        return token;
//    }
//    //验证token
//    public static boolean isExpire(String token){
//        if(StringUtils.isEmpty(token)){
//            return true;
//        }
//        try {
//            Jwts.parser().setSigningKey(SALT).parseClaimsJws(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return true;
//        }
//        return false;
//    }
//    public static boolean isExpire(HttpServletRequest request){
//        String token = request.getHeader("token");
//        if(StringUtils.isEmpty(token)){
//            return false;
//        }
//        try {
//            Jwts.parser().setSigningKey(SALT).parseClaimsJws(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return true;
//        }
//        return false;
//    }
//
//    //获取用户id
//    public static String getId(String token){
//        Claims body = Jwts.parser().setSigningKey(SALT).parseClaimsJws(token).getBody();
//        return (String) body.get("id");
//    }
//
//    public String getId(HttpServletRequest request){
//        String token = request.getHeader("token");
//        Claims body = Jwts.parser().setSigningKey(SALT).parseClaimsJws(token).getBody();
//        return (String) body.get("id");
//    }
//
//}
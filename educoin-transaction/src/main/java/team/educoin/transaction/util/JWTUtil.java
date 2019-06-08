package team.educoin.transaction.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: PandaClark
 * @create: 2019-06-08
 */
public class JWTUtil {

    public static final String SECRET = "INCASblockchain";

    public static final Long EXPIRE = 30 * 24 * 60 * 60 * 1000L;

    public static String createToken(String userId, String userType) throws Exception {
        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create().withHeader(map) // header
                .withClaim("iss", "incas") // payload
                .withClaim("user_type", userType)
                .withClaim("user_id", userId)
                //.withIssuedAt() // sign time
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE)) // expire time
                .sign(Algorithm.HMAC256(SECRET)); // signature
        return token;
    }

    public static Map<String, String> verifyToken(String token) throws JWTVerificationException {
        // DecodedJWT jwt = null;
        // try {
        //     JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        //     jwt = verifier.verify(token);
        // } catch (JWTVerificationException e) {
        //     //token 校验失败, 抛出Token验证非法异常
        //     e.printStackTrace();
        //     throw new RuntimeException("token校验失败");
        // }

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = verifier.verify(token);

        Claim userId = jwt.getClaim("user_id");
        Claim userType = jwt.getClaim("user_type");

        if (StringUtils.isEmpty(userId.asString()) || StringUtils.isEmpty(userType.asString())){
            throw new JWTVerificationException("jwt用户名或用户类型为空！");
        }

        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId.asString());
        map.put("user_type", userType.asString());
        return map;
    }

}

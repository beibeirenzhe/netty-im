package com.fyrt.fyrtwebsocketim.webSocket;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatUser;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

//JWT 工具类 token验证
public class JwtUtil {


    private static String SECRET = "FYRT";

    /**
     * 生成token
     *
     * @param extime 过期时间
     * @return 令牌
     */
    public static String createToken(Object user,long extime) throws JsonProcessingException {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", SignatureAlgorithm.HS256);
        header.put("type", "JWT");
        //生成token 头信息 设置过期时间  签名时间  将用户信息设置json
        String token = JWT.create().withHeader(header).
//                withIssuer(""). //代表这个JWT的签发者
//                withKeyId("").  //key
//                withJWTId("").  //是JWT的唯一标识。
//                withNotBefore(). //是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的；
//                withSubject(""). //代表这个JWT的主体，即它的所有人；
//                withAudience().// 代表这个JWT的接收对象；
//                withIssuedAt(). // 是一个时间戳，代表这个JWT的签发时间；
//                withExpiresAt(new Date(System.currentTimeMillis() + extime)). //过期时间
                withClaim("user",new ObjectMapper().   //
                        writeValueAsString(user)).
                      sign(Algorithm.HMAC256(SECRET));
        return token;
    }


    /**
     * 解析令牌成为对象
     * @param token  传入令牌
     * @return // 返回对象
     * @throws Exception
     */

    public static ChatUser verifyToken(String token, String SECRET) throws  Exception{
        // 创建解析器，用来将String的令牌解析成jwt对象
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();

        // 解析
        DecodedJWT jwt = verifier.verify(token);
        // 从对象中拿出来user的键值对
        Claim userClaim = jwt.getClaim("user");
        String json = userClaim.asString();

        // 用jackson将String转换成对象
        ChatUser user = new ObjectMapper().readValue(json, ChatUser.class);

        return user;
    }

}

package hello.monitor.server.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Jwt令牌工具类
 * @author ChangxueDeng
 */
@Component
public class JwtUtils {

    /**签名密钥
     */
    @Value("${spring.security.jwt.key}")
    private String key;


    /**
     * token过期时间
     */
    @Value("${spring.security.jwt.expire}")
    private int expire;

    /**
     * redis操作工具类
     */
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 创建签名算法
     * @return {@link Algorithm}
     */
    private Algorithm createAlgorithm(){
        return Algorithm.HMAC256(key);
    }

    /**
     * 计算令牌的过期时间
     * @return {@link Date}
     */
    public Date createExpireTime(){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        //增加一小时
        calendar.add(Calendar.HOUR_OF_DAY, expire);
        return calendar.getTime();
    }

    /**
     * 创建令牌
     * @param details UserDetails
     * @param name 用户名
     * @param id 用户id
     * @return {@link String}
     */
    public String createJwtToken(UserDetails details, String name, int id) {
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", id)
                .withClaim("name", name)
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(createExpireTime())
                .withIssuedAt(new Date())
                .sign(createAlgorithm());
    }


    /**
     * 解析令牌
     * @param headerToken 整个token
     * @return {@link DecodedJWT}
     */
    public DecodedJWT decodedJwtToken(String headerToken) {
        //验证器
        JWTVerifier verifier = JWT.require(createAlgorithm()).build();
        //验证token格式
        if (!checkJwtToken(headerToken)) {
            return null;
        }
        try {
            String token = headerToken.substring(7);
            DecodedJWT decodedJwt = verifier.verify(token);
            if(decodedJwt.getExpiresAt().before(new Date())) {
                return null;
            }
            return decodedJwt;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据解码后的jwt获取用户详情
     *  从jwt中提取用户名和权限信息，构建UserDetails对象
     * @param decodedJwt 解码后的jwt
     * @return {@link UserDetails}
     */
    public UserDetails getUserDetails(DecodedJWT decodedJwt) {
        Map<String, Claim> claimMap = decodedJwt.getClaims();
        return User.withUsername(claimMap.get("name").asString())
                .password("*********")
                .authorities(claimMap.get("authorities").asArray(String.class))
                .build();
    }

    /**
     * 进行解码前，判断是否过期
     * @param headerToken 完整token
     * @return boolean
     */
    public boolean checkJwtTokenExpired(String headerToken) {
        return !checkJwtToken(headerToken) || JWT.decode(headerToken.substring(7)).getExpiresAt().before(new Date());
    }

    /**
     * 从jwt中获取id
     * @param decodedJwt 解码后的jwt
     * @return {@link String}
     */
    public String getId(DecodedJWT decodedJwt) {
        Map<String, Claim> map = decodedJwt.getClaims();
        return map.get("id").toString();
    }


    /**
     * 验证token是否满足格式
     * @param headerToken 完整token
     * @return boolean
     */
    private boolean checkJwtToken(String headerToken) {
        return headerToken.startsWith("Bearer ");
    }

    /** 令牌失效
     * @param headerToken 完整token
     * @return {@link Boolean}
     */
    public Boolean invalidateJwtToken(String headerToken) {
        JWTVerifier verifier = JWT.require(createAlgorithm()).build();
        if(!checkJwtToken(headerToken)) {
            return false;
        }
        String token = headerToken.substring(7);
        try {
            DecodedJWT decodedJwt = verifier.verify(token);
            String uuid = decodedJwt.getId();
            Date expire = decodedJwt.getExpiresAt();
            return deleteJwtToken(uuid, expire);
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 删除令牌，将令牌加入黑名单
     * @param uuid 令牌uuid
     * @param expire 过期时间
     * @return boolean
     */
    private boolean deleteJwtToken(String uuid, Date expire) {
        if(isInJwtBlack(uuid)) {
            return false;
        }
        //计算剩余时间
        long time = Math.max(expire.getTime() - System.currentTimeMillis(),1);
        //加入黑名单
        stringRedisTemplate.opsForValue().set(Const.BLACK_JWT + uuid, "", time, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 判断令牌是否已存在于黑名单
     * @param uuid 令牌uuid
     * @return boolean
     */
    private boolean isInJwtBlack(String uuid) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.BLACK_JWT + uuid));
    }
}

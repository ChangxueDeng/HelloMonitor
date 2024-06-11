package hello.monitor.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ChangxueDeng
 */
@Configuration
public class WebConfiguration {
    /**
     * 密码加密编码器
     * @return {@link BCryptPasswordEncoder }
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

package hello.monitor.server.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ消息队列配置类
 * @author ChangxueDeng
 *
 */
@Configuration
public class RabbitmqConfiguration {

    /**
     * 创建队列
     * @return {@link Queue }
     */
    @Bean(name = "q")
    public Queue emailCampus(){
        return QueueBuilder.nonDurable("email-template").build();
    }

    /**
     * 创建交换机
     * @return {@link Exchange }
     */
    @Bean(name = "e")
    public Exchange exchange(){
        return ExchangeBuilder.directExchange("amq.direct").build();
    }

    /**
     * 绑定队列和交换机
     * @param queue 队列
     * @param exchange 交换机
     * @return {@link Binding }
     */
    @Bean
    public Binding binding(@Qualifier("q") Queue queue, @Qualifier("e") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("send-email").noargs();
    }

    /**
     * 消息转换器
     * @return {@link MessageConverter }
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}

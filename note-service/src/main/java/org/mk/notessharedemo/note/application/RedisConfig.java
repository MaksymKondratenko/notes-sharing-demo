package org.mk.notessharedemo.note.application;

import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository;
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;

@Configuration
@EnableConfigurationProperties({RedisProps.class, NotificationProps.class})
@EnableRedisRepositories(basePackageClasses = {NoteRepository.class, SharedNoteRepository.class},
        enableKeyspaceEvents = ON_STARTUP, keyspaceNotificationsConfigParameter = "Kh")
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProps redisProps;

    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisServerConfig = new RedisStandaloneConfiguration(redisProps.getServer().getHostname(), redisProps.getServer().getPort());
        return new JedisConnectionFactory(redisServerConfig);
    }

    @Bean
    public Topic topic() {
        return new PatternTopic("__keyspace@*__:SharedNote:*");
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListener messageListener,
                                                        RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListener, topic());
        return container;
    }
}

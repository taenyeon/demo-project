package com.example.demoproject.common.webSocket.handler;

import com.example.demoproject.common.security.CustomUserDetailService;
import com.example.demoproject.common.security.JwtTokenProvider;
import com.example.demoproject.common.security.entity.UserAuthentication;
import com.example.demoproject.common.security.entity.UserDetailCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

//    private final CustomUserDetailService customUserDetailService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand() || StompCommand.SEND == accessor.getCommand()) {
            String jwt = accessor.getFirstNativeHeader("jwt");
            JwtTokenProvider.validateToken(jwt);
//            String userSeq = JwtTokenProvider.getUserSeqFromJWT(jwt);
//            UserDetailCustom user = customUserDetailService.loadUserByUserSeq(Long.parseLong(userSeq));
//            UserAuthentication userAuthentication = new UserAuthentication(user, null, user.getAuthorities());
//            userAuthentication.setDetails(user);
//            SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        }
        return message;
    }
}

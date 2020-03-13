package com.devglan.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

@Controller
public class WebSocketController {

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/message/{pushedMsgTo}/{pushingMsgFrom}")
	@SendToUser("/queue/reply/{pushedMsgTo}")
	public String processMessageFromClient(@Payload String message,@DestinationVariable String pushedMsgTo,@DestinationVariable String pushingMsgFrom ) throws Exception {
		System.out.println("pushedMsgTo:"+pushedMsgTo);
		System.out.println("pushingMsgFrom:"+pushingMsgFrom);
		String name = new Gson().fromJson(message, Map.class).get("name").toString();
		//System.out.println("principal.getName()");
		//messagingTemplate.convertAndSendToUser("user", "/queue/reply", name);
		messagingTemplate.convertAndSend("/queue/reply/"+pushedMsgTo, "its done");
		return name;
	}
	
	@MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}

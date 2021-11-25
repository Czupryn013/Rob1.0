package pl.czupryn.rob.chat;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.czupryn.rob.chat.ChatMessage;

@Controller
public class ChatMessageController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")

    public ChatMessage get(ChatMessage chatMessage){
        return chatMessage;
    }

    @GetMapping("/chatRoom")
    public String getChat(Model model) {
        return "main/chat_room_1";
    }


}

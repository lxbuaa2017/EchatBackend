package com.example.echatbackend.service;

import com.example.echatbackend.dao.ConversationRepository;
import com.example.echatbackend.entity.Conversation;
import com.example.echatbackend.entity.Group;
import com.example.echatbackend.entity.User;
import com.example.echatbackend.handler.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ConversationService extends BaseService<Conversation, Integer, ConversationRepository> {

    @Autowired
    private ConversationRepository conversationRepository;

    public Conversation addConversation(String type) {
        Conversation conversation = new Conversation();
        conversation.setType(type);
        baseRepository.saveAndFlush(conversation);
        return conversation;
    }

    public Map<String, UUID> getOnlineUser(Integer conversationId){
        Conversation conversation = conversationRepository.getOne(conversationId);
        List<User> conversationUsers = conversation.getUsers();
        Map<String, UUID> onlineUsers = new HashMap<>();
        for (User user:conversationUsers) {
            Map<String, UUID> clientMap = SocketHandler.getClientMap();
            if (clientMap.containsKey(user.getUserName())){
                onlineUsers.put(user.getUserName(),clientMap.get(user.getUserName()));
            }
        }
        return onlineUsers;
    }

}

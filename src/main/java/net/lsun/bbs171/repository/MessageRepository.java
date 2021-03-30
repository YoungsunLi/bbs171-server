package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.Message;

import java.util.List;

public interface MessageRepository {
    void addMessage(Message message);

    int findMessageCount(int user_id);

    List<Message> findMessagesByUserId(int user_id);

    Message findMessageById(int id);

    void remove(int id);

    void removeAll(int user_id);
}

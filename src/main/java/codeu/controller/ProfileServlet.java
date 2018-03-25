package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileServlet extends HttpServlet {

    private ConversationStore conversationStore;
    private MessageStore messageStore;
    private UserStore userStore;

    @Override
    public void init() throws ServletException {
        super.init();
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
        setUserStore(UserStore.getInstance());
    }

    void setConversationStore(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }

    void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String requestUrl = request.getRequestURI();
        String ownerName = requestUrl.substring("/profile/".length());
        User owner = userStore.getUser(ownerName);
        if (owner == null) {
            // owner was not found, direct to homepage
            response.sendRedirect("/conversations");
            return;
        }

        // get conversations owner is in
        // TODO(Elle) optimize this
        List<Conversation> ownerConversations =
            conversationStore.getAllConversations()
                .stream()
                .filter(conversation ->
                    messageStore.getMessagesInConversation(conversation.id)
                        .stream()
                        .filter(message -> message.getAuthorId().equals(owner.getId()))
                        .findAny()
                        .isPresent())
                .collect(Collectors.toList());
        request.setAttribute("conversations", ownerConversations);
        request.setAttribute("owner", owner);
        request.getRequestDispatcher("/WEB-INF/view/profile.jsp")
            .forward(request, response);
    }
}

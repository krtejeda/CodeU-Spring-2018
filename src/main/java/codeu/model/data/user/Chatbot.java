package codeu.model.data.user;

public interface Chatbot {

  String respondToMessageFrom(User sender, String messageContent);

}

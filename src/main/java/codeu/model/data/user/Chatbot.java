package codeu.model.data.user;

public interface Chatbot extends UserInterface {

  String respondToMessageFrom(User sender, String messageContent);

}

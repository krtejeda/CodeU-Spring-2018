package codeu.model.dialogflow;


import com.google.cloud.dialogflow.v2beta1.Context;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.v2beta1.Intent;
import com.google.cloud.dialogflow.v2beta1.IntentsClient;
import com.google.cloud.dialogflow.v2beta1.ProjectAgentName;
import com.google.cloud.dialogflow.v2beta1.QueryInput;
import com.google.cloud.dialogflow.v2beta1.QueryResult;
import com.google.cloud.dialogflow.v2beta1.SessionName;
import com.google.cloud.dialogflow.v2beta1.SessionsClient;
import com.google.cloud.dialogflow.v2beta1.TextInput;
import com.google.cloud.dialogflow.v2beta1.TextInput.Builder;
import com.google.common.base.Strings;

/**
 * DialogFlow API Detect Intent sample with text inputs.
 */
public class DetectIntentTexts {

  /**
   * Returns the result of detect intent with texts as inputs.
   *
   * Using the same `session_id` between requests allows continuation of the conversation.
   * @param projectId Project/Agent Id.
   * @param text The text intents to be detected based on what a user says.
   * @param sessionId Identifier of the DetectIntent session.
   * @param languageCode Language code of the query.
   */
  public static String detectIntentTexts(
      String projectId,
      String text,
      String sessionId,
      String languageCode)
  throws Exception {
    // Instantiates a client
    try (SessionsClient sessionsClient = SessionsClient.create()) {
      // Set the session name using the sessionId (UUID) and projectID (my-project-id)
      SessionName session = SessionName.of(projectId, sessionId);
      System.out.println("Session Path: " + session.toString());

      // Detect intents for each text input
      // Set the text (hello) and language code (en-US) for the query
      Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

      System.out.println("finished textInputBuilder");

      // Build the query with the TextInput
      QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

      System.out.println("finished queryInput");

      // Performs the detect intent request
      DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

      System.out.println("finished response");

      // Display the query result
      QueryResult queryResult = response.getQueryResult();

      System.out.println("====================");
      System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
      System.out.format("Detected Intent: %s (confidence: %f)\n",
          queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
      System.out.format("Output context Text: '%s'\n", queryResult.getOutputContextsList());
      System.out.format("Response Text: '%s'\n", queryResult.getIntent().getDefaultResponsePlatformsList());
      System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());

      String fulfilmentText = queryResult.getFulfillmentText();
      return Strings.isNullOrEmpty(fulfilmentText) ?
          queryResult.getIntent().getDisplayName() : fulfilmentText;
    }
  }

  /**
   * List intents
   * @param projectId Project/Agent Id.
   */
  public static void listIntents(String projectId) throws Exception {
    // Instantiates a client
    try (IntentsClient intentsClient = IntentsClient.create()) {
      // Set the project agent name using the projectID (my-project-id)
      ProjectAgentName parent = ProjectAgentName.of(projectId);

      // Performs the list intents request
      for (Intent intent : intentsClient.listIntents(parent).iterateAll()) {
        System.out.println("====================");
        System.out.format("Intent name: '%s'\n", intent.getName());
        System.out.format("Intent display name: '%s'\n", intent.getDisplayName());
        System.out.format("Action: '%s'\n", intent.getAction());
        System.out.format("Root followup intent: '%s'\n", intent.getRootFollowupIntentName());
        System.out.format("Parent followup intent: '%s'\n", intent.getParentFollowupIntentName());

        System.out.format("Input contexts:\n");
        for (String inputContextName : intent.getInputContextNamesList()) {
          System.out.format("\tName: %s\n", inputContextName);
        }

        System.out.format("Output contexts:\n");
        for (Context outputContext : intent.getOutputContextsList()) {
          System.out.format("\tName: %s\n", outputContext.getName());
        }
      }
    }
  }
}

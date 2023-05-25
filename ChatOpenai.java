import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatOpenai {
    private static final String OPENAI_API_KEY = Key.OPENAI;

    public static Message getChatGPTResponse(List<Message> messages, String model, int maxTokens, double temperature) {
        try {
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
            connection.setDoOutput(true);

            String jsonInputString = createRequestJson(messages, model, maxTokens, temperature);

            connection.getOutputStream().write(jsonInputString.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return parseResponseJson(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new Message("system", "Oh no, the chatbot was unavailable. Please try again.");
        }
    }

    private static String createRequestJson(List<Message> messages, String model, int maxTokens, double temperature) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"model\": \"" + model + "\",");
        jsonBuilder.append("\"messages\": [");
        for (Message message : messages) {
            jsonBuilder.append("{");
            jsonBuilder.append("\"role\": \"" + message.getRole() + "\",");
            jsonBuilder.append("\"content\": \"" + message.getContent() + "\"");
            jsonBuilder.append("},");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.length() - 1); // Remove the last comma
        jsonBuilder.append("],");
        jsonBuilder.append("\"max_tokens\": " + maxTokens + ",");
        jsonBuilder.append("\"temperature\": " + temperature + ",");
        jsonBuilder.append("\"n\": 1");
        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    private static Message parseResponseJson(String jsonResponse) {
        Pattern pattern = Pattern.compile("\"role\":\"([^\"]+)\",\"content\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(jsonResponse);

        if (matcher.find()) {
            String role = matcher.group(1);
            String content = matcher.group(2);
            return new Message(role, content);
        } else {
            return new Message("system", "Unable to parse the chat response.");
        }
    }

    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }
}

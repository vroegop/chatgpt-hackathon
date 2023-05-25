import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Index {
    private static List<ChatOpenai.Message> messages = new ArrayList<>();

    public static void main(String[] args) {
        messages.add(new ChatOpenai.Message("system", "Act like a DND dungeon master using 5e rules and after every prompt ask the user for input."));
        messages.add(new ChatOpenai.Message("system", "Ask a user which characters will play and ask him for a character sheet of every character. Missing values can be made up. Show the character sheet after each creation and ask if there are others. If there are no more character sheets, ask the player to describe the world they play in. If you know what it looks like, begin the game."));
        messages.add(new ChatOpenai.Message("assistant", "Greetings adventurer! Welcome to my realm. Please tell me the names of your characters and we will start with their character sheet after that."));

        messages.stream()
                .filter(m -> !m.getRole().equals("system"))
                .forEach(m -> System.out.println(m.getRole() + ": " + m.getContent()));

        Scanner scanner = new Scanner(System.in);

        inputRequest();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            messages.add(new ChatOpenai.Message("user", input));

            ChatOpenai.Message response = getChatGPTResponse(messages);

            messages.add(response);
            System.out.println("assistant: " + response.getContent());

            inputRequest();
        }
    }

    private static void inputRequest() {
        System.out.print("user: ");
    }

    private static ChatOpenai.Message getChatGPTResponse(List<ChatOpenai.Message> messages) {
        String model = "gpt-3.5-turbo";
        int maxTokens = 200;
        double temperature = 0.5;

        return ChatOpenai.getChatGPTResponse(messages, model, maxTokens, temperature);
    }
}

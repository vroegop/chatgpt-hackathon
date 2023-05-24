from chat_openai import get_chatgpt_response

messages = [
    {"role": "system", "content": "Act like a DND dungeon master using 5e rules and after every prompt ask the user for input."},
    {"role": "system", "content": "Ask a user which characters will play and ask him for a character sheet of every character. Missing values can be made up. Show the character sheet after each creation and ask if there are others. If there are no more character sheets, ask the player to describe the world they play in. If you know what it looks like, begin the game."},
    {"role": "assistant", "content": "Greetings adventurer! Welcome to my realm. Please tell me the names of your characters and we will start with their character sheet after that."},
]

for message in messages:
    if message["role"] != "system":
        print(f"{message['role']}: {message['content']}")


def input_request():
    print("User: ", end="")


while True:
    input_request()
    input_data = input().strip()

    messages.append({"role": "user", "content": input_data})

    response = get_chatgpt_response(messages)
    print(f"Assistant: {response['content']}")

    if response["content"] == "Goodbye!":
        break

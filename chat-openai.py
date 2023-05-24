import requests
import json
from key import openai

def get_chatgpt_response(messages, model='gpt-3.5-turbo', max_tokens=200, temperature=0.5):
    try:
        headers = {
            'Content-Type': 'application/json',
            'Authorization': f'Bearer {openai}'
        }

        payload = {
            'model': model,
            'messages': messages,
            'max_tokens': max_tokens,
            'temperature': temperature,
            'n': 1
        }

        response = requests.post('https://api.openai.com/v1/chat/completions', headers=headers, json=payload)

        if not response.ok:
            raise Exception('Not ok')

        data = response.json()
        return data['choices'][0]['message']
    except Exception as e:
        print(e)
        return {'role': 'system', 'content': 'Oh no, the chatbot was unavailable. Please try again.'}

def execute_chatgpt_script():
    input_messages = [
        {"role": "system", "content": "Act like a DND dungeon master using 5e rules and after every prompt ask the user for input."},
        {"role": "system", "content": "Ask a user which characters will play and ask him for a character sheet of every character. Missing values can be made up. Show the character sheet after each creation and ask if there are others. If there are no more character sheets, ask the player to describe the world they play in. If you know what it looks like, begin the game."},
        {"role": "assistant", "content": "Greetings adventurer! Welcome to my realm. Please tell me the names of your characters and we will start with their character sheet after that."},
        {"role": "user", "content": "All luminis employees will play! Daniël, Lutske, Steef, Niels and Randy."}
    ]

    response = get_chatgpt_response(input_messages)
    print(response)

if __name__ == '__main__':
    execute_chatgpt_script()

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

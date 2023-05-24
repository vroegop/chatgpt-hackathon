import { openai } from './key.js';

// Connects to ChatGPT to get a chat response
export async function getChatGPTResponse(messages, { model, max_tokens, temperature } = { model: 'gpt-3.5-turbo', max_tokens: 200, temperature: 0.5 }) {
    try {
        const response = await fetch('https://api.openai.com/v1/chat/completions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${openai}`
            },
            body: JSON.stringify({
                model,
                messages,
                max_tokens,
                temperature,
                n: 1,
            })
        });

        if (!response.ok) {
            throw new Error('Not ok');
        }

        const data = await response.json();
        return data.choices[0].message;
    } catch (err) {
        console.error(err);
        return { role: 'system', content: 'Oh no, the chatbot was unavailable. Please try again.' }
    }
}

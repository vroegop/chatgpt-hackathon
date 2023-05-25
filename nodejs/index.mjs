import { getChatGPTResponse } from "./chat-openai.mjs";

const messages = [
    {"role": "system", "content": "Act like a DND dungeon master using 5e rules and after every prompt ask the user for input."},
    {"role": "system", "content": "Ask a user which characters will play and ask him for a character sheet of every character. Missing values can be made up. Show the character sheet after each creation and ask if there are others. If there are no more character sheets, ask the player to describe the world they play in. If you know what it looks like, begin the game."},
    {"role": "assistant", "content": "Greetings adventurer! Welcome to my realm. Please tell me the names of your characters and we will start with their character sheet after that."},
];

messages.filter(m => m.role !== 'system')
    .forEach(m => console.log(`${m.role}: ${m.content}`));


process.stdin.setEncoding('utf8');

const inputRequest = () => {
    process.stdout.write('User: ');
}
process.stdin.on('data', async (data) => {
    const input = data.trim();

    messages.push({ role: 'user', content: input });

    const response = await getChatGPTResponse(messages);
    console.log(`Assistant: ${response.content}`);

    inputRequest();
});

inputRequest();

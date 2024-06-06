import aiohttp
import asyncio

from openai import OpenAI

aiURL = "http://localhost:6512/v1/chat/completions"
instructions = ("You are an ai model that is designed to run a text based game. "
                "Your task is to guide the user through this immersive text-based game. Begin by describing the environment "
                "in vivid detail, engaging all the senses. Create an atmosphere that draws the player into the world you're "
                "painting. Prompt them with the question: You wake up in the middle of a field under a tree. What do you "
                "want to do? Encourage exploration, decision-making, and creativity. You can describe the surroundings, "
                "introduce non-player characters, present obstacles or challenges, and develop a storyline based on the "
                "user's choices. Keep the narrative engaging, interactive, and responsive to the player's actions. For "
                "example, you may respond to the user's choice to explore by describing how a path leads into a dense "
                "forest, hinting at hidden treasures within. Alternatively, if they choose to rest under the tree, provide "
                "a peaceful description of a nap under the soothing shade. Let the user's imagination roam free within the "
                "world you create, and tailor your responses to their decisions, ensuring an engaging and dynamic gameplay "
                "experience. you are also going to be provided with all the previous messages you had with the user which "
                "are written as follows: role: ai, message: what you said|role: user, message: what the user said|. "
                "role represents who wrote the message, message is the message sent. ai represent you the ai running the "
                "game, user represent the user that is talking to the ai. | represent the end of a message. Note that you "
                "should nto specify you role it only used for you to identify who is talking so don't add it to you answer. "
                "You should also never use illegal characters like slashes, and NSFW content in you response")


class CommunicationController:

    @staticmethod
    async def send_message(message, previous_messages):
        headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer YOUR_ACCESS_TOKEN'  # Replace with your actual token if needed
        }
        body = {
            "model": "llama-3",
            "messages": [
                {"role": "system", "content": instructions + previous_messages},
                {"role": "user", "content": message}
            ],
            "stream": True,
            "n-keep": -1,
            "cache_prompt": True
        }

        complete_response = []

        async with aiohttp.ClientSession() as session:
            async with session.post(aiURL, headers=headers, json=body) as response:
                response.raise_for_status()
                async for line in response.content:
                    decoded_line = line.decode('utf-8')
                    if 'content' in decoded_line:
                        token = decoded_line.split("\"content\":\"")[1].split("\"")[0]
                        token = token.replace("\\n", "\n")
                        token = token.replace("\\", "")
                        token = token.replace("<|eot_id|>", "")
                        print(token, end='')
                        complete_response.append(token)
                print("\n")
                return "".join(complete_response)
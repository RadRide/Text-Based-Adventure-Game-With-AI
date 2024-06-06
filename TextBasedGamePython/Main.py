import CommunicationController
import asyncio

from Message import Message
from CommunicationController import CommunicationController


def get_previous_messages(messages):
    return ''.join(str(message) for message in messages)

def clean_string(input_str):
    result = input_str.replace("\n", "**")
    result = result.replace("\\", "")
    return result

async def main():
    messages = []

    print("Welcome To This Text based AI Game.\nIt Will Start Shortly...\n\n\n")

    # messages.append()

    messages.append(Message("ai", clean_string(await CommunicationController.send_message("start the game", "This is a new Game so not previous conversation yet"))))

    while True:
        message = input(">>> ")
        messages.append(Message("user", message))
        messages.append(Message("ai", clean_string(await CommunicationController.send_message(message, get_previous_messages(messages)))))


asyncio.run(main())


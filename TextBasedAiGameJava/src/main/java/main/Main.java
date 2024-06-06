package main;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<Message> messages = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        String currentMessage;

        System.out.println("Welcome To This Text based AI Game.\nIt Will Start Shortly...\n\n\n");

        messages.add(new Message("ai", cleanString(CommunicationController.sendMessage("start the game",
                "This is a new Game so not previous conversation yet"))));

        while (true){
            System.out.print(">>> ");
            currentMessage = sc.nextLine();
            if(!currentMessage.isEmpty()){
                String response = CommunicationController.sendMessage(currentMessage, getPreviousMessages(messages));
                messages.add(new Message("user", currentMessage));
                messages.add(new Message("ai", cleanString(response)));
            }
        }
    }

    public static String getPreviousMessages(ArrayList<Message> messages){
        String previousMessage = "";
        for(Message message : messages){
            previousMessage += message.toString();
        }
        return previousMessage;
    }

    public static String cleanString(String input){
        String result = input.replaceAll("\n", "**");
        result = result.replaceAll("\\\\", "");
        return result;
    }
}
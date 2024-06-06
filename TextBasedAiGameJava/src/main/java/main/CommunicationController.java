package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommunicationController {

    /**
     * The link of AI api
     */
    public static String AI_URL = "http://localhost:6512/v1/chat/completions";

    public static String INSTRUCTIONS = "You are an ai model that is designed to run a text based game. " +
            "Your task is to guide the user through this immersive text-based game. Begin by describing the environment " +
            "in vivid detail, engaging all the senses. Create an atmosphere that draws the player into the world you're " +
            "painting. Prompt them with the question: You wake up in the middle of a field under a tree. What do you " +
            "want to do? Encourage exploration, decision-making, and creativity. You can describe the surroundings, " +
            "introduce non-player characters, present obstacles or challenges, and develop a storyline based on the " +
            "user's choices. Keep the narrative engaging, interactive, and responsive to the player's actions. For " +
            "example, you may respond to the user's choice to explore by describing how a path leads into a dense " +
            "forest, hinting at hidden treasures within. Alternatively, if they choose to rest under the tree, provide " +
            "a peaceful description of a nap under the soothing shade. Let the user's imagination roam free within the " +
            "world you create, and tailor your responses to their decisions, ensuring an engaging and dynamic gameplay " +
            "experience. you are also going to be provided with all the previous messages you had with the user which " +
            "are written as follows: role: ai, message: what you said|role: user, message: what the user said|. " +
            "role represents who wrote the message, message is the message sent. ai represent you the ai running the " +
            "game, user represent the user that is talking to the ai. | represent the end of a message. Note that you " +
            "should nto specify you role it only used for you to identify who is talking so don't add it to you answer, " +
            "you should also never use illegal characters like slashes, and NSFW content in you response";

    /**
     * Send The message of the user including all the previous conversation with the AI and streams the response in the
     * console then returns the entire message received
     * @param message The current message sent by the user
     * @param previousMessages The previous conversation with the AI
     * @return The complete response of the AI as a String
     */
    public static String sendMessage(String message, String previousMessages) {
        StringBuilder response = new StringBuilder();
        try{
            URL obj = new URL(AI_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            // Build request body
            String body = "{\n\"model\": \"llama-3\"," +
                    "\n\"messages\": [" +
                    "\n{\n    \"role\": \"system\",\n    \"content\": \"" + INSTRUCTIONS + previousMessages +"\"\n}," +
                    "\n{\n    \"role\": \"user\",\n    \"content\": \"" + message + "\"\n}\n],\n" +
                    "\"stream\": true\n," +
                    "\"n_keep\": -1," +
                    "\"cache_prompt\": true" +
                    "\n}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            //get httpRespnonse
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null){
                if(!inputLine.equals("")){
                    if(inputLine.contains("content")){
                        String token = inputLine.split("\"content\":\"")[1].split("\"")[0];
                        if(!token.equals("<|eot_id|>")){
                            token = token.replaceAll("\\\\n", "\n");
                            token = token.replaceAll("\\\\", "\n");
                            System.out.print(token);
                            response.append(token);
                        }
                    }else{
                        System.out.println("\n");
                        break;
                    }
                }
            }
            return response.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return response.toString();
    }
}



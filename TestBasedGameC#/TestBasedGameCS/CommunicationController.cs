using System.Net;
using System.Text;
using System.Text.RegularExpressions;
using Azure;
using Azure.AI.OpenAI;

namespace TextBasedAIGame;

public class CommunicationController
{
        private static string AI_URL = "http://localhost:6512/v1/chat/completions";
        private static string INSTRUCTIONS = "You are an ai model that is designed to run a text based game. " +
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
            "should nto specify you role it only used for you to identify who is talking so don't add it to you answer. " +
            "You should also never use illegal characters like slashes, and NSFW content in you response";

        public static async Task<string> SendMessage(string message, string previousMessages)
        {
            StringBuilder responseBuilder = new StringBuilder();
            
            HttpClient client = new HttpClient();
            
            string body = "{\n\"model\": \"mistral-7b-instruct-v0.2.Q5_K_M.gguf\"," +
                        "\n\"messages\": [" +
                        "\n{\n    \"role\": \"system\",\n    \"content\": \"" + INSTRUCTIONS + previousMessages + "\"\n}," +
                        "\n{\n    \"role\": \"user\",\n    \"content\": \"" + message + "\"\n}\n],\n" +
                        "\"stream\": true\n," +
                        "\"n_keep\": -1," +
                        "\"cache_prompt\": true" +
                        "\n}";
            
            using (var request = new HttpRequestMessage(HttpMethod.Post, AI_URL))
            {
                request.Content = new StringContent(body, Encoding.UTF8, "application/json");
                using (var response = await client.SendAsync(request, HttpCompletionOption.ResponseHeadersRead))
                {
                    response.EnsureSuccessStatusCode();

                    using (var stream = response.Content.ReadAsStream())
                    using (var reader = new System.IO.StreamReader(stream))
                    {
                        while (!reader.EndOfStream)
                        {
                            var line = await reader.ReadLineAsync();
                            if (!string.IsNullOrEmpty(line) && line.Contains("content"))
                            {
                                string token = line.Split(new[] { "\"content\":\"" }, StringSplitOptions.None)[1].Split('\"')[0];
                                if (!string.IsNullOrEmpty(token) && !token.Contains("<|eot_id|>"))
                                {
                                    token = token.Replace("\\n", "\n");
                                    token = token.Replace("\\", "");
                                    Console.Write(token);
                                    responseBuilder.Append(token);
                                }
                            }
                        }
                    }
                }
            }
            Console.WriteLine("\n");

            return responseBuilder.ToString();
        }
}
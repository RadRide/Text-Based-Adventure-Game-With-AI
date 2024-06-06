using System.Text;
using System.Text.RegularExpressions;

namespace TextBasedAIGame
{
    class Program
    {
        static void Main(string[] args)
        {
            List<Message> messages = new List<Message>();
            Console.WriteLine("Welcome To This Text based AI Game.\nIt Will Start Shortly...\n\n\n");

            messages.Add(new Message("ai", CleanString(CommunicationController.SendMessage("start the game",
                "This is a new Game so not previous conversation yet").Result)));

            while (true)
            {
                Console.Write(">>> ");
                string currentMessage = Console.ReadLine();
                if (!string.IsNullOrEmpty(currentMessage))
                {
                    string response =
                        CommunicationController.SendMessage(currentMessage, GetPreviousMessages(messages)).Result;
                    messages.Add(new Message("user", currentMessage));
                    messages.Add(new Message("ai", CleanString(response)));
                }
            }
        }

        public static string GetPreviousMessages(List<Message> messages)
        {
            StringBuilder previousMessages = new StringBuilder();
            foreach (Message message in messages)
            {
                previousMessages.Append(message.ToString());
            }

            return previousMessages.ToString();
        }

        public static string CleanString(string input)
        {
            string result = Regex.Replace(input, @"\n", "**");
            result = result.Replace("\\", "");
            return result;
        }
    }
}
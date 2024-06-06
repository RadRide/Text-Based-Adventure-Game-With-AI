namespace TextBasedAIGame;

public class Message
{
    public string Role { get; set; }
    public string Content { get; set; }

    public Message(string role, string content)
    {
        Role = role;
        Content = content;
    }

    public override string ToString()
    {
        return $"role: {Role}, message: {Content}|";
    }
}

public class Message {
    private Object content;
    private String hostName;
    private int sendPort;

    public Message(String hostName, int answerPort, Object content) {
        this.content = content;
        this.hostName = hostName;
        this.sendPort = answerPort;
    }

    public Object getContent() {
        return this.content;
    }

    public int getPort() {
        return sendPort;
    }
}

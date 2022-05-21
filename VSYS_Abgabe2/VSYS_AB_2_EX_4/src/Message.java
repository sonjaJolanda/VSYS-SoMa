import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 10;
    private Serializable content;
    private String hostName;
    private int sendPort;

    public Message(String hostName, int answerPort, Serializable content) {
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

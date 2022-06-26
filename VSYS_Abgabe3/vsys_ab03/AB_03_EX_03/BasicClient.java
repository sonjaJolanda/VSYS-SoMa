package AB_03_EX_03;

interface BasicClient {
	void connect(int port, String hostname);

	String receiveMessage(boolean blocking);

	void sendMessage(String message);

	void disconnect();
}

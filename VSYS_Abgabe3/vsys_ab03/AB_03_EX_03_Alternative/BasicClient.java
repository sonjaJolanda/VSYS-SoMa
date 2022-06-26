package AB_03_EX_03_Alternative;

interface BasicClient {
	void connect(int port, String hostname);

	String receiveMessage(boolean blocking);

	void sendMessage(String message);

	void disconnect();
}

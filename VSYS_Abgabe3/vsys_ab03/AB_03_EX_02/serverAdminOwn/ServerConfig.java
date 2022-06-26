package AB_03_EX_02.serverAdminOwn;

public class ServerConfig {
	private final String hostname;
	private final int receivePort;
	private final int sendPort;

	public ServerConfig(String hostname, int receivePort, int sendPort) {
		this.hostname = hostname;
		this.receivePort = receivePort;
		this.sendPort = sendPort;
	}

	public String getHostname() {
		return hostname;
	}

	public int getReceivePort() {
		return receivePort;
	}

	public int getSendPort() {
		return sendPort;
	}
}

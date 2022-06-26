package AB_03_EX_03;

class RMIClientConnection extends Thread implements BasicClientConnection, BasicConnection {
	private final BasicListener listener;
	private final Integer monitor;
	private String incomingMessage;
	private String outgoingMessage;
	private boolean isThereAMessageToReceive;
	private boolean isThereAMessageToSend;

	public RMIClientConnection(BasicListener listener) {
		this.listener = listener;
		isThereAMessageToReceive = false;
		isThereAMessageToSend = false;
		monitor = 0;
	}

	@Override
	public void run() {
		listener.connectionAccepted(this);
	}

	@Override
	public String message(String text) {
		if (isThereAMessageToReceive) {
			synchronized (monitor) {
				try {
					monitor.notify();
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		incomingMessage = text;
		isThereAMessageToReceive = true;

		if (!isThereAMessageToSend) {
			synchronized (monitor) {
				try {
					monitor.notify();
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		String outgoingMessageCopy = outgoingMessage;
		isThereAMessageToSend = false;
		synchronized (monitor) {
			monitor.notify(); // ToDo warum hier ohne wait()
		}

		return outgoingMessageCopy;
	}

	@Override
	public void disconnect() {
		// do nothing
	}

	@Override
	public String receiveMessage() {
		if (!isThereAMessageToReceive) {
			synchronized (monitor) {
				try {
					monitor.notify();
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		String incomingMessageCopy = incomingMessage; //kann man die nicht einfach direkt zurückgeben
		isThereAMessageToReceive = false;
		return incomingMessageCopy;
	}

	@Override
	public void sendMessage(String text) {
		outgoingMessage = text;
		isThereAMessageToSend = true;

		synchronized (monitor) {
			try {
				monitor.notify();
				monitor.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

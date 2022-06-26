package AB_03_EX_03_Alternative;

class RMIClientConnection extends Thread implements BasicClientConnection, BasicConnection {
	private final BasicListener primeServerBasicListener;
	private final Integer monitor;
	private String incomingMessage;
	private String outgoingMessage;
	private boolean isThereAMessageToReceive;
	private boolean isThereAMessageToSend;

	public RMIClientConnection(BasicListener primeServerBasicListener) {
		this.primeServerBasicListener = primeServerBasicListener;
		isThereAMessageToReceive = false;
		isThereAMessageToSend = false;
		monitor = 0;
	}

	@Override
	public void run() {
		primeServerBasicListener.connectionAccepted(this);
	}

	/**
	 * is called by the RMIClient.
	 * @param text The message to be sent.
	 * @return
	 */
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
	// aufgerufen vom PrimServer
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
		String incomingMessageCopy = incomingMessage; // ToDo kann man die nicht einfach direkt zurückgeben
		isThereAMessageToReceive = false;
		return incomingMessageCopy;
	}

	@Override
	// aufgerufen vom PrimServer
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

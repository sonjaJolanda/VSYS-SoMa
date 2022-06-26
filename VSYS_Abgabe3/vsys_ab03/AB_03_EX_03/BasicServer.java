package AB_03_EX_03;

interface BasicServer {
	/**
	 * Creates a new {@link java.rmi.registry.Registry} and registers the
	 * {@link BasicListener} in it.
	 *
	 * @param port The port on which to create the registry.
	 */
	void waitForConnection(int port);

	/**
	 * Set a {@link BasicListener} to be registered in the newly generated
	 * {@link java.rmi.registry.Registry}.
	 *
	 * @param primeServerBasicListener The listener to be registered.
	 */
	void setPrimeServerBasicListener(BasicListener primeServerBasicListener);
}

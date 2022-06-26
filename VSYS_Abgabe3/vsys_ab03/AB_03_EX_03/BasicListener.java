package AB_03_EX_03;

import java.util.concurrent.Future;

interface BasicListener {
	//void connectionAccepted(BasicConnection connection);
	Future<Boolean> primeService(long number);
}


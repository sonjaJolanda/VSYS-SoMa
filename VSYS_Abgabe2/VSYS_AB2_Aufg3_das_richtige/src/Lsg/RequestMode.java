package Lsg;

import java.util.Locale;

public enum RequestMode {
	SYNC, ASYNC, POLLING;

	@Override
	public String toString() {
		return super.toString().toLowerCase(Locale.ROOT);
	}

	public static RequestMode parse(String string) {
		return RequestMode.valueOf(string.replace(" ", "_").toUpperCase(Locale.ROOT));
	}
}

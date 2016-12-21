package controleRempes.control.freebox;

public class FreeboxException extends Exception {


	private static final long serialVersionUID = -1091364637055602578L;

	FreeboxException(final String message) {
		super(message);
	}
	FreeboxException(final Throwable th) {
		super(th);
	}
	FreeboxException(final String message, final Throwable cause) {
		super(message,cause);
	}

}

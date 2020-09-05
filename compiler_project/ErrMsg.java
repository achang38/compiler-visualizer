import java.util.*;
/**
 * ErrMsg
 *
 * This class is used to generate warning and fatal error messages.
 */
class ErrMsg {
	private static boolean err = false;
	private static ArrayList<String> listErrors = new ArrayList<String>();

	/**
	 * Generates a fatal error message.
	 * @param lineNum line number for error location
	 * @param charNum character number (i.e., column) for error location
	 * @param msg associated message for error
	 */
	static void fatal(int lineNum, int charNum, String msg) {
		err = true;
		listErrors.add(lineNum + ":" + charNum + " ***ERROR*** " + msg);
		System.err.println(lineNum + ":" + charNum + " ***ERROR*** " + msg);
	}

	/**
	 * Generates a warning message.
	 * @param lineNum line number for warning location
	 * @param charNum character number (i.e., column) for warning location
	 * @param msg associated message for warning
	 */
	static void warn(int lineNum, int charNum, String msg) {
		listErrors.add(lineNum + ":" + charNum + " ***WARNING*** " + msg);
		System.err.println(lineNum + ":" + charNum + " ***WARNING*** " + msg);
	}

	/**
	 * Returns the err flag.
	 */
	static boolean getErr() {
		return err;
	}

	static ArrayList<String> getList() {
		return listErrors;
	}

	static void clearErrors() {
		listErrors.clear();
	}

}

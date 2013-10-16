package gambler.commons.util.io;

public class PrintLineProcessor implements ILineProcessor {

	@Override
	public void process(int lineNumber, String line) {
		System.out.println("Line " + lineNumber + ": " + line);
	}

}

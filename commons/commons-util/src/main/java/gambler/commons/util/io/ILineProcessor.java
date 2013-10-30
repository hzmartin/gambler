package gambler.commons.util.io;

/**
 * processor for each text line from input file
 * 
 * @author Martin
 *
 */
public interface ILineProcessor {

	void process(int lineNumber, String line) throws Exception;
	
	void cleanUp();

}

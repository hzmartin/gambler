/*
 * @(#) IAppSupportService.java 2013-9-16
 * 
 */
package gabmler.tools.service;


/**
 * Class IAppSupportService
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public class ExitSysService implements IService {

	/**
     * 
     */
	public void exit() {
		System.out.println("Bye ... ...");
		System.out.println();
		System.exit(0);
	}

}

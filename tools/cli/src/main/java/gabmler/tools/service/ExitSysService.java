/*
 * @(#) IAppSupportService.java 2013-9-16
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
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

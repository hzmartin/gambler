package gabmler.tools.cli.cmd;

import gabmler.tools.service.IService;

/**
 * Class AbstractCommand
 * 
 * @author Martin
 */
public abstract class AbstractCommand implements ICommand {

	private final String name;

	private String[] parameter;

	protected final IService service;

	/**
	 * @param name
	 * @param handler
	 */
	public AbstractCommand(String name, IService service) {
		super();
		this.name = name;
		this.service = service;
	}

	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getAlias()
	 */
	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String[] getParameter() {
		return parameter;
	}

	@Override
	public void setParameter(String[] parameter) {
		this.parameter = parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#skipPrevCmdRecord()
	 */
	@Override
	public boolean isIgnorablePrevCommand() {
		return false;
	}
}

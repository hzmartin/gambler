package org.scoreboard.utils;

import org.apache.log4j.Logger;

public abstract class ISysConfigLoader {
    static Logger m_logger = Logger.getLogger(ISysConfigLoader.class);

    public void loadSysConfig() throws Exception {
        // 由下面的继承类来实现
        m_logger.warn("you should not call this function!");
    }

    // 得到重新读取配置的间隔时间
    public int getReloadInterval() {
        // 默认10分钟
        return 10 * 60 * 1000;
    }

}

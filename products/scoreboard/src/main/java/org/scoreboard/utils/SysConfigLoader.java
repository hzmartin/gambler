package org.scoreboard.utils;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class SysConfigLoader
{
    private static Logger m_logger = Logger.getLogger( SysConfigLoader.class );
    
    ISysConfigLoader m_loader;
    
    private static final Timer m_timer = new Timer( "ConfigTimer", true );
    
    public SysConfigLoader( ISysConfigLoader loader )
    {
        m_loader = loader;
    }
    
    public void start()
    {
        // 10分钟load一次
        int interal = m_loader.getReloadInterval();
        m_timer.schedule( new ConfigTimerTask(), interal, interal );
    }
    
    // 每隔一段时间，自动载入配置文件
    class ConfigTimerTask extends TimerTask
    {
        public ConfigTimerTask()
        {
            reload();
        }
        
        public void run()
        {
            reload();
        }
        
        private void reload()
        {
            try
            {
                m_loader.loadSysConfig();
            }
            catch( Exception ex )
            {
                m_logger.error( "load SysConfig error", ex );
            }
        }
    }
}

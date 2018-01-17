#!/bin/sh

#java -classpath .:lib/* gambler.quartz.QuartzJobScheduler  &
#该脚本为Linux下启动java程序的通用脚本。即可以作为开机自启动service脚本被调用，
#也可以作为启动java程序的独立脚本来使用。
#
#
#警告!!!：该脚本stop部分使用系统kill命令来强制终止指定的java程序进程。
#在杀死进程前，未作任何条件检查。在某些情况下，如程序正在进行文件或数据库写操作，
#可能会造成数据丢失或数据不完整。如果必须要考虑到这类情况，则需要改写此脚本，
#增加在执行kill命令前的一系列检查。
#
#
###################################
#环境变量及程序执行参数
#需要根据实际环境以及Java程序名称来修改这些参数
###################################

#JAVA_HOME=/usr/lib/jvm/java-6-openjdk-amd64

#Java程序所在的目录（classes的上一级目录）
GAMBLER_HOME=`pwd`
LOG_DIR_OPT="-DGAMBLER_HOME=\"${GAMBLER_HOME}\""
#需要启动的Java主程序（main方法类）
APP_MAINCLASS='gambler.quartz.QuartzJobScheduler'
 
#拼凑完整的classpath参数，包括指定lib目录下所有的jar
CLASSPATH=".:${GAMBLER_HOME}/lib/*"

#java虚拟机启动参数
JAVA_OPTS="-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:${GAMBLER_HOME}/log/quartz/gc-`date +%Y-%m-%d`.log -Xms128M -Xmx512M -XX:MaxPermSize=512M -XX:+UseParallelGC -XX:+UseParallelOldGC ${LOG_DIR_OPT}"

#jprofiler参数
JPROFILER_OPTS="-agentpath:/usr/local/jprofiler7/bin/linux-x64/libjprofilerti.so=port=8849,nowait"

###################################
#(函数)判断程序是否已启动
#
#说明：
#使用JDK自带的JPS命令及grep命令组合，准确查找pid
#jps 加 l 参数，表示显示java的完整包路径
#使用awk，分割出pid ($1部分)，及Java程序名称($2部分)
###################################
#初始化psid变量（全局）
psid_phone=0
checkpid() {
   javaps=`$JAVA_HOME/bin/jps -lv | grep $APP_MAINCLASS |grep $GAMBLER_HOME`
 
   if [ -n "$javaps" ]; then
      psid_phone=`echo $javaps | awk '{print $1}'`
   else
      psid_phone=0
   fi
}
 
###################################
#(函数)启动程序
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则提示程序已启动
#3. 如果程序没有被启动，则执行启动命令行
#4. 启动命令执行后，再次调用checkpid函数
#5. 如果步骤4的结果能够确认程序的pid,则打印[OK]，否则打印[Failed]
#注意：echo -n 表示打印字符后，不换行
#注意: "nohup 某命令 >/dev/null 2>&1 &" 的用法
###################################
start_phone() {
   checkpid
 
   if [ $psid_phone -ne 0 ]; then
      echo "================================"
      echo "warn: $APP_MAINCLASS already started! (pid=$psid_phone)"
      echo "================================"
   else
      echo "Starting $APP_MAINCLASS ..."
	  MY_CLASSPATH="$CLASSPATH"
	  DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,address=8882,server=y,suspend=n"     
	  if [ $1x = 'phone'x ];then
		JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS $DEBUG  -classpath $MY_CLASSPATH $APP_MAINCLASS >${GAMBLER_HOME}/log/quartz/log.out 2>&1 &"
	  else
        JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS  -classpath $MY_CLASSPATH $APP_MAINCLASS >/dev/null 2>&1 &"
	fi
	eval "$JAVA_CMD"
      checkpid
      if [ $psid_phone -ne 0 ]; then
         echo "(pid=$psid_phone) [OK]"
      else
         echo "[Failed]"
      fi
   fi
}
 
###################################
#(函数)停止程序
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则开始执行停止，否则，提示程序未运行
#3. 使用kill -9 pid命令进行强制杀死进程
#4. 执行kill命令行紧接其后，马上查看上一句命令的返回值: $?
#5. 如果步骤4的结果$?等于0,则打印[OK]，否则打印[Failed]
#6. 为了防止java程序被启动多次，这里增加反复检查进程，反复杀死的处理（递归调用stop）。
#注意：echo -n 表示打印字符后，不换行
#注意: 在shell编程中，"$?" 表示上一句命令或者一个函数的返回值
###################################
stop_phone() {
   checkpid
 
   if [ $psid_phone -ne 0 ]; then
      echo "Stopping $APP_MAINCLASS ...(pid=$psid_phone) "
      eval "kill -9 $psid_phone"
      if [ $? -eq 0 ]; then
         echo "[OK]"
      else
         echo "[Failed]"
      fi
 
      checkpid
      if [ $psid_phone -ne 0 ]; then
         stop_phone
      fi
   else
      echo "================================"
      echo "warn: $APP_MAINCLASS is not running"
      echo "================================"
   fi
}
 
###################################
#(函数)检查程序运行状态
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则提示正在运行并表示出pid
#3. 否则，提示程序未运行
###################################
status_phone() {
   checkpid
 
   if [ $psid_phone -ne 0 ];  then
      echo "$APP_MAINCLASS is running! (pid=$psid_phone)"
   else
      echo "$APP_MAINCLASS is not running"
   fi
}
 
###################################
#(函数)打印系统环境参数
###################################
info() {
   echo "System Information:"
   echo "****************************"
   echo `head -n 1 /etc/issue`
   echo `uname -a`
   echo
   echo "JAVA_HOME=$JAVA_HOME"
   echo `$JAVA_HOME/bin/java -version`
   echo
   echo "INSTALL_HOME=$GAMBLER_HOME"
   echo "APP_MAINCLASS=$APP_MAINCLASS"
   echo "****************************"
}

help() {
	echo "Usage: $0 {start|stop|restart|status|info}"
	echo "Server: {phone}"
}
 
###################################
#读取脚本的第一个参数($1)，进行判断
#参数取值范围：{start|stop|restart|status|info}
#如参数不在指定范围之内，则打印帮助信息
###################################
case "$1" in
   'start')
	  start_phone $2
      ;;
   'start_phone')
	  start_phone $2
      ;;
   'stop')
     stop_phone
     ;;
   'stop_phone')
     stop_phone
     ;;
   'restart')
     stop_phone
	 
	 start_phone
     ;;
   'restart_phone')
     stop_phone
	 
	 start_phone
     ;;
   'status_phone')
     status_phone
     ;;
   'status')
	 status_phone
     ;;
   'info')
     info
     ;;
   'help')
     help
     ;;
  *)
esac    
     exit 1
	 ;;

#!/bin/bash
#hadoop 停止脚本

hadoop_home=/data/soft/hadoop-2.7.2

if [ $(jps | grep -i historyserver | wc -l) -eq 1 ]
	then
		$hadoop_home/sbin/mr-jobhistory-daemon.sh stop historyserver
	else
		echo "stop JobHistoryServer failed!"
fi

if [ $(jps | grep -i nodemanager | wc -l) -eq 1 ]
	then
		$hadoop_home/sbin/yarn-daemon.sh stop nodemanager
	else
		echo "stop NodeManager failed!"
fi

if [ $(jps | grep -i resourcemanager | wc -l) -eq 1 ]
	then
		$hadoop_home/sbin/yarn-daemon.sh stop resourcemanager
	else
		echo "stop ResourceManager failed!"
fi

if [ $(jps | grep -i datanode | wc -l) -eq 1 ]
	then
		$hadoop_home/sbin/hadoop-daemon.sh stop datanode
	else
		echo "stop DataNode failed!"
fi

if [ $(jps | grep -i namenode | wc -l) -eq 1 ]
	then
		$hadoop_home/sbin/hadoop-daemon.sh stop namenode
	else
		echo "stop NameNode failed!"
fi

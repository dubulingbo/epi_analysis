#!/bin/bash
# hadoop启动脚本

hadoop_home=/data/soft/hadoop-2.7.2
if [ $(jps | wc -l) -eq 1 ]
	then
		$hadoop_home/sbin/hadoop-daemon.sh start namenode
		$hadoop_home/sbin/hadoop-daemon.sh start datanode
		if [ $(jps | wc -l) -eq 3 ]
       			then
                		$hadoop_home/sbin/yarn-daemon.sh start resourcemanager
                		$hadoop_home/sbin/yarn-daemon.sh start nodemanager
				if [ $(jps | wc -l) -eq 5 ]
        				then
                				$hadoop_home/sbin/mr-jobhistory-daemon.sh start historyserver
        				else
                				echo "start historyserver failed!"
				fi
        		else
                		echo "start resourcemanager and nodemanager failed!"
		fi
	else
		echo "start namenode and datanode failed!"
fi

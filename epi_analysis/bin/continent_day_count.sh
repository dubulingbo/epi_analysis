#!/bin/bash
# 周增幅任务：统计截止到每天全球的疫情情况
# 参数：[yyyy-MM-dd]
echo "[$(date +'%Y-%m-%d %H:%M:%S')] Executing continent day count job..."
# 判断用户是否输入日期参数（默认统计昨天的数据）
if [ "X$1" = "X" ]; then
	cur_date=$(date -d "yesterday" +"%Y-%m-%d")
else
	cur_date=$1
fi


# 定义一些变量
day_count_dir=hdfs://hadoop100:9000/user/epidemic/con_day_count_job
# 脚本与任务所在目录
jobs_home=/data/soft/jobs

# 导入统计的原始数据 (中国 + 其它国家)
sqoop import --connect jdbc:mysql://hadoop100:3306/epi_analysis_db?characterEncoding=UTF-8 \
--username root --password root \
--query "select update_time,continent,country,confirm,now_confirm from foreign_country JOIN(SELECT MAX(X.update_time) as maxTimeStamp FROM foreign_country AS X GROUP BY DATE(X.update_time)) AS T ON T.maxTimeStamp=update_time where CONVERT(update_time,date)=\"$cur_date\" and \$CONDITIONS;" \
--target-dir ${day_count_dir}/${cur_date}/raw_data/foreign --delete-target-dir --num-mappers 1 \
--fields-terminated-by "\t"

sqoop import --connect jdbc:mysql://hadoop100:3306/epi_analysis_db?characterEncoding=UTF-8 \
--username root --password root \
--query "select update_time,\"亚洲\" as continent,\"中国\" as country,confirm,now_confirm from new_china JOIN(SELECT MAX(X.update_time) as maxTimeStamp FROM new_china AS X GROUP BY DATE(X.update_time)) AS T ON T.maxTimeStamp=update_time where CONVERT(update_time,date)=\"$cur_date\" and \$CONDITIONS;" \
--target-dir ${day_count_dir}/${cur_date}/raw_data/china --delete-target-dir --num-mappers 1 \
--fields-terminated-by "\t"

# 判断导入的数据是否为空，为空则不执行
foreign_data=$(hadoop fs -text ${day_count_dir}/${cur_date}/raw_data/foreign/*00000* | head -1 | wc -l)
china_data=$(hadoop fs -text ${day_count_dir}/${cur_date}/raw_data/china/*00000* | head -1 | wc -l)
if [ ${foreign_data} != 0 -a ${china_data} != 0 ]
then
	# 删除输出目录，解决脚本重新执行问题
	hdfs dfs -rm -r ${day_count_dir}/${cur_date}/deal_data

	# 进行统计任务
	hadoop jar ${jobs_home}/epi_analysis-1.0-SNAPSHOT.jar edu.dublbo.global.DayCountJob ${day_count_dir}/${cur_date}/raw_data/foreign ${day_count_dir}/${cur_date}/raw_data/china ${day_count_dir}/${cur_date}/deal_data
	
	# 判断上一条命令是否执行成功
	if [ $? == 0 ]
	then
		# 导出数据
		sqoop export --connect "jdbc:mysql://hadoop100:3306/epi_analysis_db?characterEncoding=UTF-8" \
		--username root --password root --table count_continent --columns date,continent,confirm,now_confirm \
		--num-mappers 1 --export-dir ${day_count_dir}/${cur_date}/deal_data --input-fields-terminated-by "\t"
	else
		echo "Day Continent job execute failed!"
	fi
else
	echo "Raw data import failed!"
fi
echo "====================[$(date +'%Y-%m-%d %H:%M:%S')] Job end!===================="

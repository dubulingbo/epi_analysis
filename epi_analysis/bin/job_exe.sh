#!/bin/bash
# 判断用户是否输入日期参数，如果没有输入则默认获取昨天的日期
if [ "X$1" = "X" ]; then
	yes_time=$(date +"%Y-%m-%d")
else
	yes_time=$1
fi

# 因为主要数据存储在 MySQL 中，所以 HDFS 上只是暂存本次要处理的数据
summary_job_input=hdfs://hadoop100:9000/user/epidemic/"${yes_time}"/raw_data
summary_job_output=hdfs://hadoop100:9000/user/epidemic/"${yes_time}"/summary_data
topN_job_output=hdfs://hadoop100:9000/user/epidemic/"${yes_time}"/topN_data
jobs_home=/data/soft/jobs

# 删除输出目录，为了兼容脚本重跑的情况
hdfs dfs -rm -r "${summary_job_output}"
hdfs dfs -rm -r "${topN_job_output}"

# 判断原始数据是否已经导入
hdfs dfs -ls "${summary_job_input}"/_SUCCESS
if [ "$?" = "0" ]; then
	echo "Raw data has been imported!"
else
	# 从 MySQL 中导入原始数据到 HDFS
	sqoop import --connect jdbc:mysql://hadoop100:3306/epi_analysis_db \
	--username root --password root --table details \
	--where "update_time between \"$yes_time 00:00:00\" and \"$yes_time 23:59:59\"" \
	--target-dir "$summary_job_input" --delete-target-dir --num-mappers 1 \
	--fields-terminated-by "\t"
fi

# 执行数据汇总任务
hadoop jar ${jobs_home}/epi_analysis-1.0-SNAPSHOT.jar edu.dublbo.basicHandle.BasicHandleDriver "${summary_job_input}" "${summary_job_output}"

# 判断数据汇总任务是否执行成功
hdfs dfs -ls "${summary_job_output}"/_SUCCESS
if [ "$?" = "0" ]; then
	echo "Summary Job execute success!"
	# 执行topN任务：统计疫情严重的前 10 个省
	hadoop jar ${jobs_home}/epi_analysis-1.0-SNAPSHOT.jar edu.dublbo.topN.TopNDriver "${summary_job_output}" "${topN_job_output}"

	# 将处理后的数据从 HDFS 导出到 MySQL
	sqoop export --connect "jdbc:mysql://hadoop100:3306/epi_analysis_db?characterEncoding=UTF-8" \
	--username root --password root --table top_ten \
	--num-mappers 1 --export-dir "${topN_job_output}" --input-fields-terminated-by "\t"

	echo "All jobs execute finished!"
else
	echo "Summary Job execute failed! date time is ${yes_time}"
fi

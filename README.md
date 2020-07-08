# visualize-outbreak
可视化疫情数据

## GuanJinping-branch-01
环境搭建：centos6.x + jdk1.8 + hadoop2.7.2 + mysql5.7 + sqoop1.4.6

sqoop : 用于将在HDFS和MySQL之间传递数据
Hadoop mapreduce : 用于数据处理

## 文件说明
1. 环境搭建												搭建环境文件
2. epi_analysis/bin										存放shell脚本文件
3. epi_analysis/target/epi_analysis-1.0-SNAPSHOT.jar	mapreduce任务jar包
4. epi_analysis/src										mapreduce任务源码

## 部署运行
1. 搭建环境，参照<<环境搭建.docx>>
2. 将[mapreduce任务jar包]和[continent_day_count.sh脚本文件]上传到 /data/soft/jobs 目录下
3. 执行 continent_day_count.sh 脚本文件就能运行
	bash -x /data/soft/jobs/continent_day_count.sh
4. 也可以定义 crontab 定时任务
	crontab -e
	3 0 * * * /data/soft/jobs/continent_day_count.sh





#!/bin/bash
# 疫情分析定时脚本文件：爬虫任务 + hadoop mapreduce任务

job_home=/data/soft/jobs
cur_date=$(date +"%Y-%m-%d")

# 爬虫任务
/usr/local/bin/python3 /data/soft/opat/cov/spider.py all >> /data/soft/logs/${cur_date}.log

# 启动hadoop
bash -x ${job_home}/epi_hadoop_start.sh >> /data/soft/logs/${cur_date}.log

# 执行周增幅任务
bash -x /data/soft/jobs/continent_day_count.sh >> /data/soft/logs/epi_analysis_job.log


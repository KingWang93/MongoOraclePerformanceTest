# MongoOraclePerformanceTest

- [MongoOraclePerformanceTest](https://github.com/KingWang93/MongoOraclePerformanceTest)
- [TomcatLogAla](https://github.com/KingWang93/TomcatLogAla)
- [LogAlaHive](https://github.com/KingWang93/LogAlaHive)

上述三个项目都是关于Tomcat日志访问分析的项目，逐渐递进

MongoOraclePerformanceTest是基于Oracle做的日志分析，同时该项目还对MondoDb和Oracle做了插入性能测试和查询测试（分别在无索引和有索引的情况下）
TomcatLogAla是基于纯HDFS（没有Hive,也没有用到Hbase）开发的
LogAlaHive是基于Hbase，同时上层利用Hive做查询使用功能

本项目主要功能：
（1）基于Tomcat访问日志进行分析，分析了各个时段/每天的访问量统计，以及各个IP访问量的统计
（2）对比分析MongoDb和Oracle的插入性能和查询性能

具体细节，详看：
http://blog.csdn.net/wk1134314305/article/details/76436903

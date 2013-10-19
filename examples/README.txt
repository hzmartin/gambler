Notes:
1. 通过hibernate自动生成ddl
2. 通过derby dblook导出已有数据库的ddl

derby工具：
1. 设置DERBY_HOME, 把bin目录加到PATH中即可
2. ij> connect 'jdbc:derby:example-derby'; run 'example-derby.sql';
3. dblook -d 'jdbc:derby:example-derby' -o out.sql
4. startNetworkServer/stopNetworkServer
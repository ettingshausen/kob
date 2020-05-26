> 为了方便部署这里将 `console` 与 `processor` 模块合并为一个，如果有条件，建议两个模块分开独立部署。

### MySQL

使用 MySQL 作为数据库端，如果使用 JPA 需要先创建数据库，注意数据库编码。参考以下脚本：
或者使用 根目录的 `kob_table.sql` 创建数据库。
```mysql
create database kob;
set names utf8mb4;
ALTER SCHEMA kob DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;
```

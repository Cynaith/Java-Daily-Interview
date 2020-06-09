## Cassandra

### 创建keyspace
```cassandraql
CREATE KEYSPACE iFile WITH replication = 
{'class':'SimpleStrategy', 'replication_factor' : 3};

use iFile;

CREATE TABLE user_log(
    logid BIGINT,
    userid  BIGINT,
    username text,
    action text,
    createtime date,
    ip text,
    PRIMARY KEY (logid)
);

CREATE TABLE file(
    fileid BIGINT,
    userid BIGINT,
    filetype TEXT,
    createtime DATE,
    filedata BLOB,
    filename TEXT,
    filesuffix TEXT,
    PRIMARY KEY ( fileid )
);

CREATE TABLE user(
    userid BIGINT,
    username TEXT,
    password TEXT,
    kind TEXT,
    PRIMARY KEY ( userid )
)
```
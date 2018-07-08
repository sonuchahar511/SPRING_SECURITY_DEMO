
##########RememberMe token saved in persistent_logins table by JdbcTokenRepositoryImpl
CREATE TABLE persistent_logins(
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


########### table for sessions of web applications inside tomcat.
## tomcat internally manage sessions of web applications.

CREATE TABLE tomcat_sessions (
  session_id     varchar(100) not null primary key,
  valid_session  char(1) not null,
  max_inactive   int not null,
  last_access    bigint not null,
  app_name       varchar(255),
  session_data   mediumblob,
  KEY kapp_name(app_name)
);
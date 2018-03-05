CREATE SCHEMA `requests` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE t_request (
  _ip VARCHAR(16) NOT NULL,
  _method VARCHAR(32) NOT NULL,
  _clientdes VARCHAR(128),
  _startdate VARCHAR(23) NOT NULL, /*start date format: YYYY-MM-DD HH:mm:ss.SSS*/
  _httpcode  smallint,
  primary key (_ip, _method, _startdate)
) engine=InnoDB;

CREATE TABLE t_block (
  _ip VARCHAR(16) NOT NULL,
  _startdate VARCHAR(23) NOT NULL, /*start date format: YYYY-MM-DD HH:mm:ss.SSS*/
  _numrequest  int(11),
  primary key (_ip, _startdate)
) engine=InnoDB;
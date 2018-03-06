CREATE SCHEMA `requests` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE t_request (
  _ip VARCHAR(16) NOT NULL,
  _method VARCHAR(64) NOT NULL,
  _clientdes VARCHAR(256),
  _startdate VARCHAR(23) NOT NULL, /*start date format: YYYY-MM-DD HH:mm:ss.SSS*/
  _httpcode  int,
  primary key (_ip, _method, _startdate)
) engine=InnoDB;

CREATE TABLE t_block (
  _ip VARCHAR(16) NOT NULL,
  _startdate VARCHAR(23) NOT NULL, /*start date format: YYYY-MM-DD HH:mm:ss.SSS*/
  _numrequest  int,
  primary key (_ip, _startdate)
) engine=InnoDB;
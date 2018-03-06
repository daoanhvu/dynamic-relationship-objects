-- Fetch all requests
SELECT _ip, _startdate, _method, _clientdes, _httpcode
FROM t_request;

--Fetch request made by a specific client
SELECT _ip, _startdate, _method, _clientdes, _httpcode
FROM t_request WHERE _ip = 'some_ip_address';

--Count number of client
SELECT count(DISTINCT _ip) as num_of_client FROM t_request;

--List client's ip and the number of request it have made
SELECT _ip, count(_ip) as num_of_request FROM t_request GROUP BY _ip;

--List ip and number of request made by it in a specific period
SELECT _ip, COUNT(*) as num_of_request
FROM t_request
WHERE DATE_FORMAT(_startdate, '%Y-%m-%d %H:%i:%s.%f')  
	BETWEEN DATE_FORMAT('2017-01-01 00:00:00.000', '%Y-%m-%d %H:%i:%s.%f')
	AND DATE_FORMAT('2017-01-01 23:00:00.000', '%Y-%m-%d %H:%i:%s.%f')
GROUP BY _ip

--Get number of request made by an IP in a specific period
SELECT _ip, COUNT(*) as num_of_request
FROM t_request
WHERE DATE_FORMAT(_startdate, '%Y-%m-%d %H:%i:%s.%f')  
	BETWEEN DATE_FORMAT('2017-01-01 00:00:00.000', '%Y-%m-%d %H:%i:%s.%f')
	AND DATE_FORMAT('2017-01-01 23:00:00.000', '%Y-%m-%d %H:%i:%s.%f')
	AND _ip = 'SOMEIP'

-- Fetch all block IPs
select _ip, _startdate, _numrequest from t_block;

SELECT _ip, _startdate, _numrequest 
FROM t_block
WHERE _ip = 'SOME_IP';
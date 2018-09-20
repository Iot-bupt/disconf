INSERT INTO `app` (`app_id`, `name`, `description`, `create_time`, `update_time`)
VALUES
    (2, 'disconf_demo', 'disconf demo', '99991231235959', '99991231235959');

INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`)
VALUES
    (4, 0, 'coefficients.properties', 'coe.baiFaCoe=1.3\ncoe.yuErBaoCoe=1.3\n', 2, '1_0_0_0', 1, '99991231235959', '20141205154137'),
    (5, 1, 'moneyInvest', '10000', 2, '1_0_0_0', 1, '99991231235959', '20140902183514'),
    (6, 0, 'remote.properties', 'remoteHost=127.0.0.1\r\nremotePort=8081', 2, '1_0_0_0', 1, '20140729174707', '20140804233309'),
    (7, 1, 'discountRate', '0.5', 2, '1_0_0_0', 1, '20140801142833', '20140905130141'),
    (12, 0, 'redis.properties', 'redis.host=127.0.0.1\nredis.port=6379', 2, '1_0_0_0', 1, '20140811172327', '20141011154244'),
    (16, 0, 'static.properties', 'staticVar=147', 2, '1_0_0_0', 1, '20140814202654', '20140905145616'),
    (17, 1, 'staticItem', '30', 2, '1_0_0_0', 1, '20140814210709', '20140814211054'),
    (29, 0, 'empty.properties', 'redis.host=127.0.0.1\r\nredis.port=8310', 2, '1_0_0_0', 1, '20140909164001', '20140909164125'),
    (48, 0, 'myserver.properties', 'server=127.0.0.1:16600,127.0.0.1:16602,127.0.0.1:16603\nretry=5\n', 2, '1_0_0_0', 1, '20140911223117', '20141117153116'),
    (119, 0, 'myserver_slave.properties', '#online\r\nserver=127.0.0.1:16700,127.0.0.1:16700,127.0.0.1:16700,127.0.0.1:16700\r\nretry=3', 2, '1_0_0_0', 1, '20141103163302', '20141103163302'),
    (122, 0, 'testXml.xml', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<routes>\n    <route sourceHost=\"*\" sourcePort=\"*\"\n        proxyHost=\"127.0.0.1\" proxyPort=\"8081\">\n        <rule>\n            <from method=\"get\">/tradeMap</from>\n            <to method=\"get\">/tradeMap</to>\n        </rule>\n    </route>\n</routes>', 2, '1_0_0_0', 1, '20141103202829', '20141110193440'),
    (143, 0, 'testXml2.xml', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<routes>\n    <route sourceHost=\"*\" sourcePort=\"*\"\n        proxyHost=\"127.0.0.1\" proxyPort=\"8081\">\n        <rule>\n            <from method=\"get\">/tradeMap</from>\n            <to method=\"get\">/tradeMap</to>\n        </rule>\n    </route>\n</routes>', 2, '1_0_0_0', 1, '20141110193605', '20141110193605'),
    (146, 0, 'code.properties', 'syserror.paramtype=\\u8bf7\\u6c42\\u53c2\\u6570\\u89e3\\u6790\\u9519\" + \"\\u8bef', 2, '1_0_0_0', 1, '20150107115835', '20150107115835'),
    (147, 0, 'testJson.json', '{\"message\": {}, \"success\": \"true\"}', 2, '1_0_0_0', 1, '20150121150626', '20150121153650'),
    (148, 0, 'autoconfig.properties', 'auto=bbdxxjdccd', 2, '1_0_0_0', 1, '20150320130619', '20150320224956'),
    (149, 0, 'autoconfig2.properties', 'auto2=cd', 2, '1_0_0_0', 1, '20150320130625', '20150320203808');

INSERT INTO `env` (`env_id`, `name`)
VALUES
    (1, 'rd'),
    (2, 'qa'),
    (3, 'local'),
    (4, 'online');
/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50614
Source Host           : localhost:3306
Source Database       : pgw_log

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2017-04-19 16:10:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pgw_basic_info
-- ----------------------------
DROP TABLE IF EXISTS `pgw_basic_info`;
CREATE TABLE `pgw_basic_info` (
  `pgw_name` varchar(255) DEFAULT NULL,
  `instance_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pgw_basic_info
-- ----------------------------
INSERT INTO `pgw_basic_info` VALUES ('pgw1', 'instance1,instance2,instance3');
INSERT INTO `pgw_basic_info` VALUES ('pgw2', 'instance1,instance2');

-- ----------------------------
-- Table structure for pgw_detail_data
-- ----------------------------
DROP TABLE IF EXISTS `pgw_detail_data`;
CREATE TABLE `pgw_detail_data` (
  `response_time` datetime DEFAULT NULL,
  `pgw_name` varchar(50) DEFAULT NULL,
  `instance_name` varchar(50) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `request_id` varchar(50) DEFAULT NULL,
  `result_type` varchar(20) DEFAULT NULL,
  `error_code` varchar(50) DEFAULT NULL,
  `error_message` varchar(255) DEFAULT NULL,
  `execution_time` int(10) DEFAULT NULL,
  `response_type` varchar(20) DEFAULT NULL,
  `execution_content` varchar(255) DEFAULT NULL,
  `user_number` bigint(50) DEFAULT NULL,
  `operation` varchar(255) DEFAULT NULL,
  KEY `response_time` (`response_time`),
  KEY `pgw_name` (`pgw_name`),
  KEY `instance_name` (`instance_name`),
  KEY `user_name` (`user_name`),
  KEY `request_id` (`request_id`),
  KEY `error_code` (`error_code`),
  KEY `response_type` (`response_type`),
  KEY `operation` (`operation`),
  KEY `user_number` (`user_number`),
  KEY `error_code_2` (`error_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pgw_detail_data
-- ----------------------------
INSERT INTO `pgw_detail_data` VALUES ('2017-04-18 17:22:31', 'pgw2', 'instance2', 'user1', 'qwertye', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-18 17:22:31', 'pgw2', 'instance2', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance2', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance2', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-18 17:22:31', 'pgw2', 'instance2', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 18:22:31', 'pgw2', 'instance2', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');
INSERT INTO `pgw_detail_data` VALUES ('2017-04-17 17:22:31', 'pgw1', 'instance1', 'user1', 'qwerty', 'success', '-', '-', '11', 'type1', 'DEFAULT', '123456', 'op1');

-- ----------------------------
-- Table structure for pgw_xml_log
-- ----------------------------
DROP TABLE IF EXISTS `pgw_xml_log`;
CREATE TABLE `pgw_xml_log` (
  `request_id` varchar(40) DEFAULT NULL,
  `response_log` text,
  `response_time` datetime DEFAULT NULL,
  KEY `id_index` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pgw_xml_log
-- ----------------------------
INSERT INTO `pgw_xml_log` VALUES ('qwertye', '<spml:modifyResponse executionTime=\"23\" requestID=\"-3e7205b5:153a41d0683:2595\" result=\"success\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n	<version>SUBSCRIBER_v10</version>\r\n	<objectclass>Subscriber</objectclass>\r\n	<identifier>460020011251104</identifier>\r\n	<modification name=\"hlr\" operation=\"addorset\" scope=\"uniqueTypeMapping\">\r\n		<valueObject xmlns:ns2=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns2:HLR\">\r\n			<odboc>1</odboc>\r\n			<odbic>1</odbic>\r\n			<odbssm>1</odbssm>\r\n			<odbgprs>2</odbgprs>\r\n		</valueObject>\r\n	</modification>\r\n</spml:modifyResponse>', '2017-04-18 17:28:58');
INSERT INTO `pgw_xml_log` VALUES ('qwerty', 'log', '2017-04-19 11:04:25');

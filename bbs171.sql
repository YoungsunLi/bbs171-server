/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : bbs171

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 12/01/2021 14:02:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int unsigned NOT NULL,
  `post_id` int(0) NOT NULL COMMENT '帖子id',
  `from_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论者手机',
  `to_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `content` varchar(2050) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `datetime` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '回复日期',
  `status` int(0) DEFAULT 1 COMMENT '1=正常, 2=已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts`  (
  `id` int unsigned NOT NULL,
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发帖用户手机',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` varchar(10050) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '正文',
  `datetime` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
  `category` int(0) DEFAULT 1 COMMENT '1=默认,2=学习,3=生活,4=表白墙',
  `views` int(0) DEFAULT 0 COMMENT '点击量',
  `status` int(0) DEFAULT 1 COMMENT '0=待审核,1=已审核,2=已删除',
  `report` int(0) DEFAULT 0 COMMENT '被举报次数',
  `comment` int(0) DEFAULT 0 COMMENT '评论数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `id` int unsigned NOT NULL,
  `post_id` int(0) NOT NULL COMMENT '被举报的帖子id',
  `user_id` int(0) NOT NULL COMMENT '举报该帖子的用户id',
  `content` varchar(10050) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报内容',
  `datetime` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
  `status` int(0) DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int unsigned NOT NULL,
  `type` int(0) DEFAULT 1 COMMENT '0=管理员，1=普通用户',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户手机',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `gender` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '性别',
  `sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '签名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `datetime` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`, `phone`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (0, 0, '18677434737', '李太阳', '123456', '男', NULL, NULL, '2020-12-05 22:54:09');
INSERT INTO `user` VALUES (1, 0, '18888888888', '测试账号', '123456', '', NULL, NULL, '2021-01-12 13:48:26');

SET FOREIGN_KEY_CHECKS = 1;

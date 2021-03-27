/*
 Navicat Premium Data Transfer

 Source Server         : bbs171
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : bbs171.lsun.net:3306
 Source Schema         : bbs171

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 27/03/2021 20:20:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `post_id` int(11) UNSIGNED NOT NULL COMMENT '帖子id',
  `from_id` int(11) UNSIGNED NOT NULL COMMENT '评论者id',
  `to_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '回复谁的',
  `content` varchar(2050) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回复日期',
  `status` int(11) NULL DEFAULT 1 COMMENT '1=正常, 2=已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `comment_post_id`(`post_id`) USING BTREE,
  INDEX `form_id`(`from_id`) USING BTREE,
  INDEX `to_id`(`to_id`) USING BTREE,
  CONSTRAINT `comment_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `form_id` FOREIGN KEY (`from_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `to_id` FOREIGN KEY (`to_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12530 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '发帖用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` varchar(10050) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '正文',
  `datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
  `category` int(11) UNSIGNED NOT NULL DEFAULT 1 COMMENT '1=默认, 2=学习, 3=生活',
  `views` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '点击量',
  `status` int(11) UNSIGNED NOT NULL DEFAULT 1 COMMENT '0=待审核, 1=已审核, 2=已删除',
  `report` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '被举报次数',
  `comment` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数量',
  `highlight` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '精华帖, 0=不是, 非0=是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `post_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `post_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12530 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `post_id` int(11) UNSIGNED NOT NULL COMMENT '被举报的帖子id',
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '举报该帖子的用户id',
  `content` varchar(10050) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报内容',
  `datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
  `status` int(11) UNSIGNED NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `report_post_id`(`post_id`) USING BTREE,
  INDEX `report_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `report_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `report_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12530 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stars
-- ----------------------------
DROP TABLE IF EXISTS `stars`;
CREATE TABLE `stars`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL,
  `post_id` int(11) UNSIGNED NOT NULL,
  `datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `post_id`(`post_id`) USING BTREE,
  CONSTRAINT `post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12530 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `role` int(11) UNSIGNED NOT NULL DEFAULT 1 COMMENT '0=管理员，1=普通用户',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户手机',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `gender` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12530 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

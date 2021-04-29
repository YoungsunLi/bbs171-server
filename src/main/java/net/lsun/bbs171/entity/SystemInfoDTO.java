package net.lsun.bbs171.entity;

import lombok.Data;

@Data
public class SystemInfoDTO {
    private int totalUsers;
    private int totalOnlineUsers;
    private int totalPosts;
    private int totalNewPosts;
}

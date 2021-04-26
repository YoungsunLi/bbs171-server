package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.Notice;

public interface NoticeRepository {
    void submit(Notice notice);

    Notice getLatest();
}

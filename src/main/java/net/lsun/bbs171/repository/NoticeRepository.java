package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.Notice;

import java.util.List;

public interface NoticeRepository {
    void submit(Notice notice);

    Notice getLatest();

    List<Notice> getAll();
}

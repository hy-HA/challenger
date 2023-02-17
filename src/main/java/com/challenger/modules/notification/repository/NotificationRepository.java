package com.challenger.modules.notification.repository;

import com.challenger.modules.account.domian.Account;
import com.challenger.modules.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countByAccountAndChecked(Account account, boolean checked);
    @Transactional
    List<Notification> findByAccountAndCheckedOrderByCreatedDateTimeDesc(Account account,
                                                                         boolean checked);

    @Transactional
    void deleteByAccountAndChecked(Account account, boolean checked);
}
package ru.dynamika.data;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.ssugt.drones.jpa.entities.common.BaseEntity;

import java.time.Instant;

@Component
public class MyEntityListener extends AuditingEntityListener {

    private final @Nullable ObjectFactory<AuditingHandler> handler;

    /**
     * Configures the {@link AuditingHandler} to be used to set the current auditor on the domain types touched.
     *
     * @param auditingHandler must not be {@literal null}.
     */

    @Autowired
    public MyEntityListener(@Nullable @Qualifier("jpaAuditingHandler") ObjectFactory<AuditingHandler> auditingHandler) {
        Assert.notNull(auditingHandler, "AuditingHandler must not be null!");
        this.handler = auditingHandler;
    }

    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link Auditable} on
     * persist events.
     *
     * @param target
     */
    @PrePersist
    @Override
    public void touchForCreate(Object target) {

        Assert.notNull(target, "Entity must not be null!");

        if (handler != null) {

            AuditingHandler object = handler.getObject();
            if (object != null) {
                object.markCreated(target);
            }
        }
    }

    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link Auditable} on
     * update events.
     *
     * @param target
     */
    @PreUpdate
    @Override
    public void touchForUpdate(Object target) {
        Assert.notNull(target, "Entity must not be null!");
        Instant updateDate = ((BaseEntity) target).getUpdateDate();
        if (updateDate != null) {
            return;
        }

        if (handler != null) {
            AuditingHandler object = handler.getObject();
            if (object != null) {
                object.markModified(target);
            }
        }
    }
}
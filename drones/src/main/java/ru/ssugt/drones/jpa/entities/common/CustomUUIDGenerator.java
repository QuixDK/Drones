package ru.ssugt.drones.jpa.entities.common;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class CustomUUIDGenerator extends UUIDGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Serializable id = (Serializable) session.getEntityPersister(null, object)
                .getClassMetadata().getIdentifier(object, session);
        if(id !=null){
            return id;
        }else{
            return (Serializable) super.generate(session, object);
        }
    }



}

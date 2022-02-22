package com.hermes.hermestock.repository;

import com.hermes.hermestock.domain.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChannelRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Channel channel){

        em.persist(channel);
    }

    public Channel find(Long id){
        return em.find(Channel.class, id);
    }

    public List<Channel> getChannelList(){

        return em.createQuery("select c From Channel c ", Channel.class).getResultList();
    }

    public List<Channel> findByName(String name){
        return em.createQuery("select c from Channel c where c.name = :name", Channel.class).setParameter("name", name).getResultList();
    }

    public List<Channel> findByUrl(String url){
        return em.createQuery("select c from Channel c where c.url = :url", Channel.class).setParameter("url", url).getResultList();
    }

}

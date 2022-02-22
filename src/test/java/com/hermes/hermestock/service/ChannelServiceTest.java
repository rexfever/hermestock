package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.Channel;
import com.hermes.hermestock.repository.ChannelRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ChannelServiceTest {

    @Autowired ChannelService channelService;
    @Autowired ChannelRepository channelRepository;

    @Test
    public void 채널등록() throws Exception{
        //given
        Channel channel = new Channel();
        channel.setName("aaa");
        channel.setUrl("www.bbb.com");

        //when
        Long savedId = channelService.register(channel);

        //then
        assertEquals(channel, channelRepository.find(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복채널확인() throws Exception{
        //given
        Channel channel = new Channel();
        channel.setName("aa");
        channel.setUrl("aaa.bbb.ccc");

        Channel channel1 = new Channel();
        channel1.setName("aa");
        channel1.setUrl("aaa.bbb.ccc");
        //when
        channelService.register(channel);
        channelService.register(channel1);

        //then
        fail("예외가 발생해야 함");
    }

}

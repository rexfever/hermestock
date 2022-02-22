package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.Channel;
import com.hermes.hermestock.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    /**
     * 채널 등록
     */
    @Transactional
    public Long register(Channel channel){
        validateDuplicateChannel(channel);
        channelRepository.save(channel);
        return channel.getId();
    }

    private void validateDuplicateChannel(Channel channel){
        List<Channel> channels = channelRepository.findByUrl(channel.getUrl());
        if(!channels.isEmpty()){
            throw new IllegalStateException("이미 존재하는 채널입니다");
        }
    }

    /**
     *채널 조회
     */
    public List<Channel> findChannelList(){
        return channelRepository.getChannelList();
    }
}

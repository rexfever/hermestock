package com.hermes.hermestock.controller;

import com.hermes.hermestock.domain.Channel;
import com.hermes.hermestock.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("/channel/new")
    public String createForm(Model model){
        log.info("Channel controller new");
        model.addAttribute("channelForm", new ChannelForm());
        return "channel/createChannelForm";
    }

    @PostMapping("/channel/new")
    public String create(@Valid ChannelForm channelForm, BindingResult result){

        if(result.hasErrors()){
            return "channel/createChannelForm";
        }
        log.info("Channel controller");
        Channel channel = new Channel();
        channel.setName(channelForm.getName());
        channel.setUrl(channelForm.getUrl());
        channelService.register(channel);
        return "redirect:/";
    }
}

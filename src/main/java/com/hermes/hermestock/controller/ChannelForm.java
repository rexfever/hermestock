package com.hermes.hermestock.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class ChannelForm {

    @NotEmpty(message = "채널 이름은 필수입니다.")
    private String name;

    private String url;
}

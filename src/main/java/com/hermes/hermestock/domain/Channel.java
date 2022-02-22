package com.hermes.hermestock.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Channel {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String url;

}

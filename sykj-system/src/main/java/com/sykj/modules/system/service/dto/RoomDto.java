package com.sykj.modules.system.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomDto {
    private RoomChildDto data;
    private String success;
    private String msg;
}

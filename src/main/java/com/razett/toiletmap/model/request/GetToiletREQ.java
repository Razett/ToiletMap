package com.razett.toiletmap.model.request;

import lombok.Data;

@Data
public class GetToiletREQ {
    private Double lat;
    private Double lng;
    private int page;
    private int count;
}

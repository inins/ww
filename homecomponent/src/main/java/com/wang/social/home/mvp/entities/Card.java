package com.wang.social.home.mvp.entities;

import lombok.Data;

@Data
public class Card {
    private int index;

    public Card(int index) {
        this.index = index;
    }
}

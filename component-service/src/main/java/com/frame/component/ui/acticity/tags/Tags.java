package com.frame.component.ui.acticity.tags;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Tags {
    private int current;
    List<Tag> list = new ArrayList<>();
}

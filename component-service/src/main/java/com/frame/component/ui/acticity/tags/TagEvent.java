package com.frame.component.ui.acticity.tags;

import com.frame.component.ui.acticity.tags.Tag;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagEvent {
    String tag;
    Tag object;
}

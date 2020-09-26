package com.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Data
public class ConversationTO {
    private String ID;
    private String key;
    private Long S;
}

package com.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryDisplayData {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    private String began;
    private String participants;

    public static HistoryDisplayData map(CurrentConversationTO currentConversationTO) {
        HistoryDisplayDataBuilder participants = HistoryDisplayData.builder()
                .began(sdf.format(new Date(currentConversationTO.getBegan())))
                .participants(currentConversationTO.getParticipants()
                        .stream()
                        .map(UserShortDAO::getUsername)
                        .reduce((allPrevious, next) -> allPrevious.concat("\n").concat(next))
                        .orElse("")
                );
        return participants.build();
    }
}

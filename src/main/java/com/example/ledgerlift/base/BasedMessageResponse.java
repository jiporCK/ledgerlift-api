package com.example.ledgerlift.base;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasedMessageResponse<T> {

    private T payload;

}

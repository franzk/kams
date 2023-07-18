package net.franzka.kams.authentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {

    String toAddress;

    String subject;

    String content;

}

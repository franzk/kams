package net.franzka.kams.email.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {

    String toAddress;

    String subject;

    String content;

}

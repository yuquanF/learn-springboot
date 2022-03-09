package com.f.missyou.dto;

import com.f.missyou.dto.validators.PasswordEqual;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;

@Getter
@Setter
@PasswordEqual(message = "两次密码不一致")
public class PersonDTO {
    @NonNull
    @Length(min = 3, max = 10, message = "字符串长度3~10")
    private String name;
    @Range(min = 1, max = 120, message = "年龄在1~120岁之间")
    private Integer age;
    private String password1;
    private String password2;
    @Valid
    private SchoolDTO schoolDTO;
}

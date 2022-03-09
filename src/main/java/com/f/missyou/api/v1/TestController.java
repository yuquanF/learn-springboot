package com.f.missyou.api.v1;

import com.f.missyou.dto.PersonDTO;
import com.f.missyou.exception.http.HttpException;
import com.f.missyou.exception.http.NotFoundException;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/test")
@Validated
public class TestController {
    @GetMapping("")
    public String testException() throws Exception {
//        throw new Exception("ccc");
//        throw new NotFoundException(10002);
        return "Test!";
    }

    @GetMapping("/url/{id}")
    public String testUrlParamValid(@PathVariable @Range(min = 1, max = 10) Integer id){
        return "Test!";
    }

//    @PostMapping("/body")
//    public String testBodyParamValid(@RequestParam @Length(min = 3) String name,
//                                     @RequestParam @Range(min = 1, max = 100) Integer age) {
//        return "Test!";
//    }

    @PostMapping("/body")
    public String testBodyParamValid(@RequestBody @Validated PersonDTO personDTO) {
        return "Test!";
    }
}

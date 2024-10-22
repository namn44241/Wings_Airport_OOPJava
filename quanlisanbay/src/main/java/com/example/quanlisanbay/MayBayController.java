package com.example.quanlisanbay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/planes")
public class MayBayController {
    @Autowired
    private MayBayService mayBayService;

    @PostMapping("/them_mb")
    public ResponseEntity<?> addPlane(@RequestBody MayBayDTO mayBayDTO) {
        return mayBayService.addPlane(mayBayDTO);
    }
    
}

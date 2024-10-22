package com.example.quanlisanbay;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MayBayService {
    @Autowired
    private MayBayRepository mayBayRepository;

    @Autowired
    private LoaiMayBayRepository loaiMayBayRepository;

    @Transactional
    public ResponseEntity<?> addPlane(MayBayDTO mayBayDTO){
        if (mayBayDTO.getSoHieu().length()>10){
            return ResponseEntity.badRequest().body("Số hiệu máy bay không được vượt quá 10 kí tự");
        }

        if (mayBayRepository.existsById(mayBayDTO.getSoHieu())){
            return ResponseEntity.badRequest().body("Máy bay với số hiệu này đã tồn tại!");
        }
        Optional<LoaiMayBay> loaiMayBayOptional = loaiMayBayRepository.findById(mayBayDTO.getMaLoai());
        if (!loaiMayBayOptional.isPresent()){
            return ResponseEntity.badRequest().body("Loại máy bay không tồn tại!");
        }
        MayBay mayBay = new MayBay();
        mayBay.setSoHieu(mayBayDTO.getSoHieu());
        mayBay.setSoGheNgoi(mayBayDTO.getSoGheNgoi());
        
        LoaiMayBay loaiMayBay = loaiMayBayOptional.get();
        mayBay.setLoaiMayBay(loaiMayBay);

        mayBayRepository.save(mayBay);

        return ResponseEntity.badRequest().body("Thêm máy bay thành công");
    }

    
}

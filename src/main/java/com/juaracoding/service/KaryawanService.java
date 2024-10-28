package com.juaracoding.service;

import com.juaracoding.dto.validasi.KaryawanValidasiDTO;
import com.juaracoding.model.Karyawan;
import com.juaracoding.repo.KaryawanRepository;
import com.juaracoding.utils.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KaryawanService {

    private final KaryawanRepository karyawanRepository;

    @Autowired
    public KaryawanService(KaryawanRepository karyawanRepository) {
        this.karyawanRepository = karyawanRepository;
    }

    public ResponseEntity<Object> createKaryawan(KaryawanValidasiDTO karyawanDTO, HttpServletRequest request) {
        if (karyawanRepository.existsByEmail(karyawanDTO.getEmail())) {
            return GlobalFunction.validasiGagal("Email sudah terdaftar", "EMAIL_DUPLICATE", request);
        }

        Karyawan karyawan = new Karyawan();
        karyawan.setNamaLengkap(karyawanDTO.getNamaLengkap());
        karyawan.setJenisKelamin(karyawanDTO.getJenisKelamin());
        karyawan.setStatusPernikahan(karyawanDTO.getStatusPernikahan());
        karyawan.setTanggalLahir(karyawanDTO.getTanggalLahir());
        karyawan.setEmail(karyawanDTO.getEmail());
        karyawan.setNoHP(karyawanDTO.getNoHP());
        karyawan.setJabatan(karyawanDTO.getJabatan());
        karyawan.setHireDate(karyawanDTO.getHireDate());

        karyawanRepository.save(karyawan);
        return GlobalFunction.dataBerhasilDisimpan(request);
    }

    public ResponseEntity<Object> updateKaryawan(Long id, KaryawanValidasiDTO karyawanDTO, HttpServletRequest request) {
        Optional<Karyawan> karyawanOptional = karyawanRepository.findById(id);
        if (karyawanOptional.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        Karyawan karyawan = karyawanOptional.get();
        karyawan.setNamaLengkap(karyawanDTO.getNamaLengkap());
        karyawan.setJenisKelamin(karyawanDTO.getJenisKelamin());
        karyawan.setStatusPernikahan(karyawanDTO.getStatusPernikahan());
        karyawan.setTanggalLahir(karyawanDTO.getTanggalLahir());
        karyawan.setEmail(karyawanDTO.getEmail());
        karyawan.setNoHP(karyawanDTO.getNoHP());
        karyawan.setJabatan(karyawanDTO.getJabatan());
        karyawan.setHireDate(karyawanDTO.getHireDate());

        karyawanRepository.save(karyawan);
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    public ResponseEntity<Object> deleteKaryawan(Long id, HttpServletRequest request) {
        if (!karyawanRepository.existsById(id)) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        karyawanRepository.deleteById(id);
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    public ResponseEntity<Object> getKaryawanById(Long id, HttpServletRequest request) {
        Optional<Karyawan> karyawan = karyawanRepository.findById(id);
        if (karyawan.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return GlobalFunction.dataByIdDitemukan(karyawan.get(), request);
    }

    public ResponseEntity<Object> getAllKaryawan(HttpServletRequest request) {
        return GlobalFunction.customDataDitemukan("Data karyawan ditemukan", karyawanRepository.findAll(), request);
    }
}

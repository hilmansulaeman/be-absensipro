package com.juaracoding.service;

import com.juaracoding.dto.response.AbsenResponseDTO;
import com.juaracoding.dto.validasi.AbsenValidationDTO;
import com.juaracoding.dto.report.AbsenReportDTO;
import com.juaracoding.model.Absen;
import com.juaracoding.repo.AbsenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AbsenService {

    private final AbsenRepository absenRepository;

    @Autowired
    public AbsenService(AbsenRepository absenRepository) {
        this.absenRepository = absenRepository;
    }

    public AbsenResponseDTO saveAbsen(AbsenValidationDTO absenValidationDTO) {
        Absen absen = new Absen();
        // Assuming Userz is fetched and set here
        absen.setAbsenIn(absenValidationDTO.getAbsenIn());
        absen.setAbsenOut(absenValidationDTO.getAbsenOut());
        absen.setIsDelete((byte) 0);  // Defaulting to 0 until verified
        Absen savedAbsen = absenRepository.save(absen);
        return convertToResponseDTO(savedAbsen);
    }

    public List<AbsenResponseDTO> getAllAbsens() {
        return absenRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AbsenReportDTO> getReportByUserId(Long userId) {
        return absenRepository.findByUserzId(userId).stream()
                .map(this::convertToReportDTO)
                .collect(Collectors.toList());
    }

    public AbsenResponseDTO convertToResponseDTO(Absen absen) {
        AbsenResponseDTO dto = new AbsenResponseDTO();
        dto.setIdAbsen(absen.getIdAbsen());
        dto.setAbsenIn(absen.getAbsenIn());
        dto.setAbsenOut(absen.getAbsenOut());
        dto.setIsDelete(absen.getIsDelete());

        // Check if userz is null before accessing its properties
        if (absen.getUserz() != null) {
            dto.setUserId(absen.getUserz().getIdUser());
            dto.setUserName(absen.getUserz().getNamaLengkap());
        } else {
            dto.setUserId(null);  // or handle it according to your business logic
            dto.setUserName("Unknown User"); // or any other default value
        }

        return dto;
    }


    private AbsenReportDTO convertToReportDTO(Absen absen) {
        AbsenReportDTO reportDTO = new AbsenReportDTO();
        reportDTO.setIdAbsen(absen.getIdAbsen());
        reportDTO.setAbsenIn(absen.getAbsenIn());
        reportDTO.setAbsenOut(absen.getAbsenOut());
        reportDTO.setUserId(absen.getUserz().getId());
        reportDTO.setUserName(absen.getUserz().getName());
        reportDTO.setTotalHoursWorked(
                (absen.getAbsenOut().getTime() - absen.getAbsenIn().getTime()) / (1000 * 60 * 60));
        return reportDTO;
    }
}

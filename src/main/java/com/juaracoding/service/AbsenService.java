package com.juaracoding.service;

import com.juaracoding.dto.response.AbsenResponseDTO;
import com.juaracoding.dto.validasi.AbsenValidationDTO;
import com.juaracoding.dto.report.AbsenReportDTO;
import com.juaracoding.model.Absen;
import com.juaracoding.repo.AbsenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;
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
        List<Absen> absens = absenRepository.findByIsDelete((byte) 0);
        System.out.println("Absen list size from repository: " + absens.size()); // Debug statement

        return absens.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AbsenReportDTO> getReportByUserId(Long userId) {
        return absenRepository.findByUserzId(userId).stream()
                .map(this::convertToReportDTO)
                .collect(Collectors.toList());
    }

    public AbsenResponseDTO convertToResponseDTO(Absen absen) {
        if (absen == null) {
            return null;
        }

        AbsenResponseDTO dto = new AbsenResponseDTO();
        dto.setIdAbsen(absen.getIdAbsen());
        dto.setAbsenIn(absen.getAbsenIn());
        dto.setAbsenOut(absen.getAbsenOut());
        dto.setIsDelete(absen.getIsDelete());

        if (absen.getUserz() != null) {
            dto.setUserId(absen.getUserz().getIdUser());
            dto.setUserName(absen.getUserz().getNamaLengkap());
        } else {
            dto.setUserId(null);
            dto.setUserName("Unknown User"); // Provide a default value if necessary
        }

        return dto;
    }


    private AbsenReportDTO convertToReportDTO(Absen absen) {
        AbsenReportDTO reportDTO = new AbsenReportDTO();
        reportDTO.setIdAbsen(absen.getIdAbsen());
        reportDTO.setAbsenIn(absen.getAbsenIn());
        reportDTO.setAbsenOut(absen.getAbsenOut());

        // Null checks for Userz
        if (absen.getUserz() != null) {
            reportDTO.setUserId(absen.getUserz().getIdUser());
            reportDTO.setUserName(absen.getUserz().getNamaLengkap());
        } else {
            reportDTO.setUserId(null);  // or other appropriate default handling
            reportDTO.setUserName("Unknown User");
        }

        // Calculate total hours worked safely
        if (absen.getAbsenIn() != null && absen.getAbsenOut() != null) {
            reportDTO.setTotalHoursWorked(
                    (absen.getAbsenOut().getTime() - absen.getAbsenIn().getTime()) / (1000 * 60 * 60)
            );
        } else {
            reportDTO.setTotalHoursWorked(0L);
        }

        return reportDTO;
    }

    public AbsenResponseDTO findById(Long id) {
        Optional<Absen> optionalAbsen = absenRepository.findById(id);
        return optionalAbsen.map(this::convertToResponseDTO).orElse(null);
    }

    public List<AbsenResponseDTO> findAllAbsen(Pageable pageable) {
        List<Absen> absens = absenRepository.findAll(pageable).getContent();
        return absens.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public AbsenResponseDTO updateAbsen(Long id, AbsenValidationDTO absenDTO) {
        Optional<Absen> optionalAbsen = absenRepository.findById(id);
        if (optionalAbsen.isEmpty()) {
            return null; // Handle error appropriately
        }

        Absen absen = optionalAbsen.get();
        absen.setAbsenIn(absenDTO.getAbsenIn());
        absen.setAbsenOut(absenDTO.getAbsenOut());
        // Update other fields as necessary

        Absen updatedAbsen = absenRepository.save(absen);
        return convertToResponseDTO(updatedAbsen);
    }

}

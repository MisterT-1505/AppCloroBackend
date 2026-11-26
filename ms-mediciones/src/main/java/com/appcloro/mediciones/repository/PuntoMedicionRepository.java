package com.appcloro.mediciones.repository;

import com.appcloro.mediciones.entity.PuntoMedicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuntoMedicionRepository extends JpaRepository<PuntoMedicion, String> {

    List<PuntoMedicion> findAllByOrderByGranjaIdAscGalponAsc();

    List<PuntoMedicion> findByGranjaIdOrderByGalponAsc(String granjaId);

    long countByGranjaId(String granjaId);
}

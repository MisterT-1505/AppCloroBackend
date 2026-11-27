package com.appcloro.mediciones.repository;

import com.appcloro.mediciones.entity.Medicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicionRepository extends JpaRepository<Medicion, Long> {

    List<Medicion> findAllByOrderByFechaHoraDesc();

    List<Medicion> findByGranjaOrderByFechaHoraDesc(String granja);

    List<Medicion> findByPuntoIdOrderByFechaHoraDesc(String puntoId);

    List<Medicion> findByGranjaAndPuntoIdOrderByFechaHoraDesc(String granja, String puntoId);

    Optional<Medicion> findFirstByPuntoIdOrderByFechaHoraDesc(String puntoId);
}

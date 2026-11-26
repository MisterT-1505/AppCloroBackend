package com.appcloro.mediciones.repository;

import com.appcloro.mediciones.entity.Medicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicionRepository extends JpaRepository<Medicion, Long> {

    List<Medicion> findAllByOrderByFechaHoraDesc();

    List<Medicion> findByGranjaOrderByFechaHoraDesc(String granja);

    List<Medicion> findByPuntoIdOrderByFechaHoraDesc(String puntoId);

    List<Medicion> findByGranjaAndPuntoIdOrderByFechaHoraDesc(String granja, String puntoId);

    Optional<Medicion> findFirstByPuntoIdOrderByFechaHoraDesc(String puntoId);

    @Query("""
            select m from Medicion m
            where m.id in (
                select max(m2.id) from Medicion m2
                where m2.granja = :granja
                group by m2.puntoId
            )
            order by m.puntoId
            """)
    List<Medicion> findUltimasPorGranja(@Param("granja") String granja);
}

package com.caceres.libreria.service;

import com.caceres.libreria.entity.Modulo;
import com.caceres.libreria.entity.Permiso;
import com.caceres.libreria.entity.Rol;
import com.caceres.libreria.entity.Rolacceso;
import com.caceres.libreria.entity.dto.GuardarAccesosDto;
import com.caceres.libreria.entity.dto.RolAccesoDto;
import com.caceres.libreria.repository.ModuloRepository;
import com.caceres.libreria.repository.PermisoRepository;
import com.caceres.libreria.repository.RolAccesoRepository;
import com.caceres.libreria.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolAccesoService {

    private final RolAccesoRepository rolAccesoRepositorio;
    private final RolRepository rolRepositorio;
    private final ModuloRepository moduloRepositorio;
    private final PermisoRepository permisoRepositorio;

    public List<RolAccesoDto> listarPorRol(Integer idRol) {
        return rolAccesoRepositorio.findByIdrol_Id(idRol)
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void guardarAccesos(GuardarAccesosDto guardarAccesosDto) {
        rolAccesoRepositorio.deleteByIdrol_Id(guardarAccesosDto.getIdRol());

        Rol rol = rolRepositorio.findById(guardarAccesosDto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        for (GuardarAccesosDto.AccesoItemDto accesoItem : guardarAccesosDto.getAccesos()) {
            Modulo modulo = moduloRepositorio.findById(accesoItem.getIdModulo())
                    .orElseThrow(() -> new RuntimeException("Modulo no encontrado"));
            Permiso permiso = permisoRepositorio.findById(accesoItem.getIdPermiso())
                    .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

            Rolacceso nuevoAcceso = new Rolacceso();
            nuevoAcceso.setIdrol(rol);
            nuevoAcceso.setIdmodulo(modulo);
            nuevoAcceso.setIdpermiso(permiso);
            rolAccesoRepositorio.save(nuevoAcceso);
        }
    }

    private RolAccesoDto convertirADto(Rolacceso rolAcceso) {
        RolAccesoDto rolAccesoDto = new RolAccesoDto();
        rolAccesoDto.setIdRolAcceso(rolAcceso.getId());
        rolAccesoDto.setIdRol(rolAcceso.getIdrol().getId());
        rolAccesoDto.setIdModulo(rolAcceso.getIdmodulo().getId());
        rolAccesoDto.setNombreModulo(rolAcceso.getIdmodulo().getNombre());
        rolAccesoDto.setIdPermiso(rolAcceso.getIdpermiso().getId());
        rolAccesoDto.setNombrePermiso(rolAcceso.getIdpermiso().getNombre());
        return rolAccesoDto;
    }
}
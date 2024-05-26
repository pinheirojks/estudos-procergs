package estudos.procergs.mapper;

import org.modelmapper.ModelMapper;

import estudos.procergs.dto.PerfilUsuarioDTO;
import estudos.procergs.dto.UsuarioDTO;
import estudos.procergs.dto.UsuarioPesqDTO;
import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioMapper {

    private ModelMapper mapper = new ModelMapper();

    public UsuarioDTO paraDTO(Usuario usuario) {
        UsuarioDTO dto = mapper.map(usuario, UsuarioDTO.class);
        dto.setPerfil(new PerfilUsuarioDTO(usuario.getPerfil().name(), usuario.getPerfil().getDescricao()));
        return dto;
    }

    public Usuario paraUsuario(UsuarioDTO dto) {
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setPerfil(PerfilUsuarioEnum.parseByName(dto.getPerfil().getNome()));
        return usuario;
    }

    public Usuario paraUsuario(UsuarioPesqDTO dto) {
        Usuario usuario = mapper.map(dto, Usuario.class);
        return usuario;
    }
}

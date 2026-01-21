package br.com.academia.bibliotecalocacao.mapper;

import br.com.academia.bibliotecalocacao.dtos.request.LocatarioRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LocatarioResponse;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import org.springframework.stereotype.Component;

@Component
public class LocatarioMapper {

    public Locatario toEntity(LocatarioRequest request) {
        if (request == null) throw new IllegalArgumentException("LocatarioRequest não pode ser nulo");
        Locatario loc = new Locatario(); // exige @NoArgsConstructor na entidade
        loc.setNome(request.nome());
        loc.setCpf(request.cpf());
        loc.setTelefone(request.telefone());
        loc.setSexo(request.sexo());
        loc.setEmail(request.email());
        loc.setDataNascimento(request.dataNascimento());
        return loc;
    }

    public LocatarioResponse toResponse(Locatario entity) {
        if (entity == null) throw new IllegalArgumentException("A entidade Locatario não pode ser nula");
        return new LocatarioResponse(
                entity.getId(),
                entity.getNome(),
                entity.getSexo(),
                entity.getTelefone(),
                entity.getEmail(),
                entity.getDataNascimento(),
                entity.getCpf()

        );
    }

}



package br.com.academia.bibliotecalocacao.exception;


import org.springframework.data.crossstore.ChangeSetPersister;

public class LocatarioNotFoundException extends ChangeSetPersister.NotFoundException {
    private final String message;

    public LocatarioNotFoundException(Long id) {
        this.message = "Locatário não encontrado com id=" + id;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

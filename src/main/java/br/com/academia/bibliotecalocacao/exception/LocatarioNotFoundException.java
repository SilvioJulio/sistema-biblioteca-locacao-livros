package br.com.academia.bibliotecalocacao.exception;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
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

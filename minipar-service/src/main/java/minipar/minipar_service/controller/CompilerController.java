package minipar.minipar_service.controller;
import minipar.CompilerServiceFacade;
import minipar.CompilerResult; // Importe CompilerResult
import minipar.dto.RunRequest;
import minipar.dto.RunResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompilerController {

    private final CompilerServiceFacade facade;

    public CompilerController() {
        this.facade = new CompilerServiceFacade();
        System.out.println("CompilerController inicializado."); // Log de inicialização
    }

    @PostMapping("/run")
    public RunResponse handleRun(@RequestBody RunRequest request) {
        System.out.println(">>> Recebida requisição /run:"); // Log - Início
        System.out.println(">>> Código: " + request.getCode().substring(0, Math.min(request.getCode().length(), 50)) + "..."); // Mostra início do código
        System.out.println(">>> Variante: " + request.getVariant());

        CompilerResult result = null;
        try {
            result = facade.run(request.getCode(), request.getVariant());
            System.out.println(">>> Facade retornou. Sucesso: " + result.isSuccess()); // Log - Após facade
        } catch (Exception e) {
            System.err.println("!!! ERRO ao chamar facade.run(): " + e.getMessage()); // Log - Erro na facade
            e.printStackTrace(); // Imprime o stack trace do erro da facade
            // Retorna uma resposta de erro explícita
            return new RunResponse(CompilerResult.error("Erro interno no facade: " + e.getMessage()));
        }

        // Cria a resposta e loga antes de retornar
        RunResponse response = new RunResponse(result);
        System.out.println("<<< Enviando resposta. Sucesso: " + response.isSuccess()); // Log - Fim
        return response;
    }
}
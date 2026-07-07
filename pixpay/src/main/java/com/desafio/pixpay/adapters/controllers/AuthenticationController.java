package com.desafio.pixpay.adapters.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.AuthenticationDTO;
import com.desafio.pixpay.adapters.dtos.ResponseData;
import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.usecases.CreateAccountUseCase;
import com.desafio.pixpay.core.usecases.data.CreateAccountInput;
import com.desafio.pixpay.infra.security.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Tag(name = "Auth", description = "Authentication and signup endpoint")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    private final CreateAccountUseCase createAccountUseCase;
    private final AuthenticationManager authenticationManager;

    AuthenticationController(AuthenticationService authenticationService, CreateAccountUseCase createAccountUseCase, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.createAccountUseCase = createAccountUseCase;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("authenticate")
    @Operation(summary = "Perform login", description = "Perform login by body with authenticationDTO",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credentials for authentication",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationDTO.class),
                examples = {
                    @ExampleObject(
                        summary = "Personal",
                        name = "Authenticate with CPF",
                        value = "{\"username\": \"22833025017\", \"password\": \"Abcd1234?\"}"
                    ),
                    @ExampleObject(
                        summary = "Business",
                        name = "Authenticate with CNPJ",
                        value = "{\"username\": \"CAEH9506000177\", \"password\": \"Abcd1234?\"}"
                    )
                }
            )
        )
    )
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Authenticated successfully", content = @Content(schema = @Schema(allOf = ResponseData.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data / Data field missing", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized: wrong login or password", content = @Content)
    })
    public ResponseEntity<EntityModel<?>> authenticate(@RequestBody AuthenticationDTO auth, HttpServletResponse response) {
        Long durationInSeconds = 86400L;
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.username(), auth.password()));
        String token = authenticationService.authenticate(authentication);
        ResponseCookie cookie = ResponseCookie.from("jwt-token", token)
            .secure(false) // Em produção, deve ser true
            .httpOnly(true)
            .path("/")
            .maxAge(durationInSeconds)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        ResponseData<?> responseData = new ResponseData<>(null, "Authenticated", LocalDateTime.now());
        EntityModel<ResponseData<?>> model = EntityModel.of(responseData, 
            linkTo(methodOn(AccountController.class).getAccountDatails(null)).withRel("account details"),
            linkTo(methodOn(TransferController.class).listTransfersFromAccount(null, null,null)).withRel("account transfers"),
            linkTo(methodOn(TransferController.class).transferMoney(null, null)).withRel("request transfer"),
            linkTo(methodOn(TransferController.class).refund(null, null)).withRel("refund transfer made to you")
        );
        return ResponseEntity.ok().body(model);
    }
    
    @Operation(summary = "Perform signup", description = "Perform the creation of account and user", 
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credentials for registering",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SaveAccountDTO.class),
                examples = {
                    @ExampleObject(
                        summary = "Personal",
                        name = "Register with CPF",
                        value = "{\"identificationType\": \"CadastroDePessoaFisica\", \"identificationNumber\": \"22833025017\", \"fullName\": \"Marlon\", \"email\": \"marlon@email.com\", \"password\": \"Abcd1234?\"}"
                    ),
                    @ExampleObject(
                        summary = "Business",
                        name = "Register with CNPJ",
                        value = "{\"identificationType\": \"CadastroNacionalDePessoaJuridica\", \"identificationNumber\": \"CAEH9506000177\", \"fullName\": \"Zé\", \"email\": \"ze@email.com\", \"password\": \"Abcd1234?\"}"
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sign up performed successfully", content = @Content(schema = @Schema(allOf = ResponseData.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data / Data field missing", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
        @ApiResponse(responseCode = "422", description = "Unprocessable  / Business error", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("register")
    public ResponseEntity<EntityModel<?>> register(@RequestBody SaveAccountDTO createAccountDTO) {
        CreateAccountInput createAccountInput = new CreateAccountInput(
            createAccountDTO.identificationType(),
            createAccountDTO.identificationNumber(),
            createAccountDTO.fullName(),
            createAccountDTO.email(),
            createAccountDTO.password()
        );
        Account createdAccount = createAccountUseCase.execute(createAccountInput);
        ResponseData<?> responseData = new ResponseData<>(null, "Account " + createdAccount.getFullName().getFullName() + " created successfully.", LocalDateTime.now());
        EntityModel<ResponseData<?>> model = EntityModel.of(responseData, 
            linkTo(methodOn(AuthenticationController.class).authenticate(null, null)).withRel("authenticate")
        );
        return ResponseEntity.status(201).body(model);
    }
    

}

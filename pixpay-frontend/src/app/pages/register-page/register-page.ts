import { Component, ElementRef, inject, signal, viewChild } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { IdentificationValidator } from '../../validators/identification-validator';
import { AuthService } from '../../services/auth-service';
import { EqualityValidator } from '../../validators/equality-validator';
import { IdentificationType } from '../../models/account.model';
import { ApiResponseModel, ErrorResponseModel } from '../../models/api-response';

@Component({
  selector: 'app-register-page',
  imports: [ReactiveFormsModule],
  templateUrl: './register-page.html',
  styleUrl: './register-page.css',
})
export class RegisterPage {
  public readonly IdentificationType = IdentificationType;
  private validPasswordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$.,&%?_+]).+$/;
  private invalidPasswordRegex = /^(?!.*[\s\\;"'<>\/|\-\-\*\(\)\[\]{} ]).+$/;
  private onlyNumbersRegex = /^[a-zA-Z0-9]+$/;
  private authService = inject(AuthService);
  identificationType = signal<IdentificationType | null>(null);
  modalMessage = signal({ title: '', message: '' });
  private modal = viewChild.required<ElementRef<HTMLDialogElement>>('modal');

  abrir() {
    // Chamamos o signal usando parênteses: modal()
    this.modal().nativeElement.showModal();
  }

  fechar() {
    this.modal().nativeElement.close();
  }

  fullName = new FormControl('', {
    nonNullable: true,
    validators: [
      Validators.required,
      Validators.minLength(1),
      Validators.maxLength(40)
    ]
  });

  email = new FormControl('', {
    nonNullable: true,
    validators: [
      Validators.required,
      Validators.email,
      Validators.minLength(3),
      Validators.maxLength(254)
    ]
  });

  identificationNumberCpf = new FormControl('', {
    nonNullable: true,
    validators: [
      Validators.required,
      Validators.pattern(this.onlyNumbersRegex),
      IdentificationValidator.isValidCpf()
    ]
  });

  identificationNumberCnpj = new FormControl('', {
    nonNullable: true,
    validators: [
      Validators.required,
      Validators.pattern(this.onlyNumbersRegex),
      IdentificationValidator.isValidCnpj()
    ]
  });

  password = new FormControl('', {
    nonNullable: true,
    validators: [
      Validators.minLength(8),
      Validators.maxLength(18),
      Validators.pattern(this.validPasswordRegex),
      Validators.pattern(this.invalidPasswordRegex)
    ]
  });

  confirmPassword = new FormControl('', {
    nonNullable: true,
    validators: [
      Validators.required
    ]
  });

  identificationFields = {
    [IdentificationType.CadastroDePessoaFisica]: this.identificationNumberCpf,
    [IdentificationType.CadastroNacionalDePessoaJuridica]: this.identificationNumberCnpj
  }

  registerForm = new FormGroup({
    fullName: this.fullName,
    email: this.email,
    identificationNumberCpf: this.identificationNumberCpf,
    identificationNumberCnpj: this.identificationNumberCnpj,
    password: this.password,
    confirmPassword: this.confirmPassword
  }, {
    validators: [
      EqualityValidator.equals(this.password, this.confirmPassword)
    ]
  });

  unsetAccountType = () => {
    this.identificationType.set(null)
  }

  setAccountTypeAsPF = () => {
    const identificationType = IdentificationType.CadastroDePessoaFisica;
    this.identificationType.set(identificationType)

    Object.entries(this.identificationFields).map(([k, v]) => {
      k == identificationType ? v.enable() : v.disable()
    })
  }

  setAccountTypeAsPJ = () => {
    const identificationType = IdentificationType.CadastroNacionalDePessoaJuridica;
    this.identificationType.set(identificationType)

    Object.entries(this.identificationFields).map(([k, v]) => {
      k == identificationType ? v.enable() : v.disable();
    })
  }

  onSubmit(event: Event) {
    event.preventDefault();

    let isInvalid = this.registerForm.invalid || Object.values(this.registerForm.controls).some((control: FormControl) => control.invalid);

    if (isInvalid) {
      this.modalMessage.set({ title: 'algo deu errado', message: 'o formulário é inválido' });
      this.abrir();
      return;
    }

    if (this.registerForm.invalid) return;
    if (this.identificationType() == null) return;
    let registerCredentials: RegisterCredentials = {
      identificationType: this.identificationType()!,
      identificationNumber: this.identificationFields[this.identificationType()!].value!,
      fullName: this.fullName.value!,
      email: this.email.value!,
      password: this.password.value!
    };
    console.log(registerCredentials);
    this.authService.register(registerCredentials)?.subscribe({
      next: (data: ApiResponseModel | null) => {
        this.modalMessage.set({ title: 'Sucesso', message: data?.message || "Conta foi aberta" });
        this.abrir();
      },
      error: (erro: ErrorResponseModel) => {
        this.modalMessage.set({ title: 'Algo deu errado', message: erro.error });
        this.abrir();
      }
    });
  }
}

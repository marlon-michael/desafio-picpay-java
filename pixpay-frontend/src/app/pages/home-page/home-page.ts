import { Component, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { IdentificationValidator } from '../../validators/identification-validator';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.html',
  styleUrl: './home-page.css',
  imports: [ReactiveFormsModule],
})
export class HomePage {
  // JUNTAR TUDO EM UMA REGEX DE PASSWORD
  // .error PARA SIMBOLOS PERMITIDOS
  private validPasswordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$.,&%?_+]).+$/;
  private invalidPasswordRegex = /^(?!.*[\s\\;"'<>\/|\-\-\*\(\)\[\]{} ]).+$/;
  private onlyNumbersRegex = /^[a-zA-Z0-9]+$/;
  private authService = inject(AuthService);

  username = new FormControl('', [
    Validators.required,
    Validators.pattern(this.onlyNumbersRegex),
    IdentificationValidator.isValidIdentification()
  ]);
  password = new FormControl('', [
    Validators.required,
    Validators.pattern(this.validPasswordRegex),
    Validators.pattern(this.invalidPasswordRegex)
  ]);

  onSubmit(event: Event) {
    event.preventDefault();

    if (this.username.errors) {
      console.log(this.username.errors);
      return;
    }

    let auth: AuthenticationCredentials = { username: this.username.value!, password: this.password.value! };
    this.authService.authenticate(auth)?.subscribe();
  }
}

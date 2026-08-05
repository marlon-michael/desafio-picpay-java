import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { cpf, cnpj } from 'cpf-cnpj-validator';

export class IdentificationValidator {

  static isValidIdentification(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;

      return cpf.isValid(value) || cnpj.isValid(value) ? null : { invalidIdentification: true };
    };
  }

  static isValidCpf(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;

      return cpf.isValid(value) ? null : { invalidCpf: true };
    };
  }

  static isValidCnpj(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;

      return cnpj.isValid(value) ? null : { invalidCnpj: true };
    };
  }
}

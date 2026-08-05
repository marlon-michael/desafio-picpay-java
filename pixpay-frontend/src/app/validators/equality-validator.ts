import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class EqualityValidator {

  static equals = (valueA: AbstractControl, valueB: AbstractControl): ValidatorFn => {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!valueA.value || !valueB.value) return null;

      const same = valueA.value === valueB.value;
      return same ? null : { differentValue : true };
    };
  }

}

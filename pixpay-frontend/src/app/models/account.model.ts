
export enum IdentificationType {
  CadastroDePessoaFisica = 'CadastroDePessoaFisica',
  CadastroNacionalDePessoaJuridica = 'CadastroNacionalDePessoaJuridica'
}

export interface Account {
  id: String,
  accountType: String,
  identificationNumber: String,
  fullName: String,
  email: String,
  balanceInReal: number,
  createdAt: String,
  lastModifiedAt: String
}

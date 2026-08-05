
interface AuthenticationCredentials {
  username: string,
  password: string
}

interface RegisterCredentials {
  identificationType: string,
  identificationNumber: string,
  fullName: string,
  email: string,
  password: string
}

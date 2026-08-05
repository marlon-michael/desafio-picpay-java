import { Account } from "./account.model";

export interface ApiResponseModel {
  message: string,
  timestamp: string,
  content: Account
}

export interface ErrorResponseModel {
  status: number,
  error: string,
}

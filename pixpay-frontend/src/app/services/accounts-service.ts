import { HttpClient } from '@angular/common/http';
import { inject, PLATFORM_ID, Service } from '@angular/core';
import { environment } from '../../environment/environment';
import { catchError, map, of, throwError } from 'rxjs';
import { isPlatformServer } from '@angular/common';
import { ApiResponseModel } from '../models/api-response';

@Service()
export class AccountsService {
  private backendUrl = environment.apiUrl;
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);

  listAccount = () => {
    if (isPlatformServer(this.platformId)) {
      return of(null);
    }
    return this.http
      .get<ApiResponseModel>(`${this.backendUrl}/accounts`, {
        withCredentials: true,
        mode: 'cors',
        observe: 'response',
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .pipe(
        map((response) => {
          return response.body;
        }),
        catchError((erro) => {
          return throwError(() => erro);
        })
      );
  }
}

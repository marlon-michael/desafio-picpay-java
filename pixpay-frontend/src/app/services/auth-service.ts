import { HttpClient, HttpResponse } from '@angular/common/http';
import { inject, PLATFORM_ID, Service } from '@angular/core';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { environment } from '../../environment/environment';
import { Router } from '@angular/router';
import { isPlatformServer } from '@angular/common';
import { ApiResponseModel } from '../models/api-response';

@Service()
export class AuthService {
  private backendUrl = environment.apiUrl;
  private http = inject(HttpClient);
  private router = inject(Router);
  private platformId = inject(PLATFORM_ID);

  authenticate = (body: AuthenticationCredentials) => {
    if (isPlatformServer(this.platformId)) {
      return of(null);
    }
    return this.http
      .post<ApiResponseModel>(
        `${this.backendUrl}/authenticate`,
        body,
        {
          withCredentials: true,
          mode: 'cors',
          observe: 'response',
          headers: {
            'Content-Type': 'application/json'
          }
        }
      )
      .pipe(
        map((response: HttpResponse<ApiResponseModel>) => {
          if (response.body?.message == "Authenticated") {
            this.router.navigate(['/account'])
          }
          return response.body;
        }),
        catchError((erro) => {
          return throwError(() => erro);
        })
      );
  }

  register = (body: RegisterCredentials) => {
    if (isPlatformServer(this.platformId)) {
      return;
    }
    return this.http.post<ApiResponseModel>(
      `${this.backendUrl}/register`,
      body,
      {
        withCredentials: true,
        mode: 'cors',
        observe: 'response',
        headers: {
          'Content-Type': 'application/json'
        }
      }).pipe(
        map((response) => {
          return response.body;
        })
        , catchError((erro) => {
          return throwError(() => erro);
        })
      );
  }

  unauthenticate = () => {
    if (isPlatformServer(this.platformId)) {
      return;
    }
    this.http.get(`${this.backendUrl}/unauthenticate`, {
      withCredentials: true,
      mode: 'cors',
      observe: 'response',
      headers: {
        'Content-Type': 'application/json'
      }
    }).subscribe(response => {
      this.router.navigate(['/'])
      return response.body;
    })
  }
}

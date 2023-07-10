import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserDto } from '../models/user-dto.model';
import { AuthTokenDto } from '../models/auth-token-dto.model';
import { EMPTY, Observable, catchError, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  isConnected$! : Observable<boolean>;

  constructor(private http:HttpClient) { }

  public login(user: UserDto) {
    return this.http.post<AuthTokenDto>('http://localhost:8090/auth/login', user).pipe(
      tap((token) => {
        if (token.authToken) {
          this.setToken(token.authToken);
          this.isConnected$ = of(true);
        }
        else {

        }
      }),
      catchError(err => {
        this.logout();
        console.log("Login error !!");
        return EMPTY;
      })
    );
  }

  public setToken(authToken : string) {
    localStorage.setItem('authToken', authToken);
  }

  public getToken() {
    let token = localStorage.getItem('authToken');
    console.log('get token : ' + token);
    if (token) {
      this.isConnected$ = of(true);
    }
    else {
      this.isConnected$ = of(false);
    }
    return token;
  }

  public logout() {
    console.log("logout");
    localStorage.removeItem('authToken');
    this.isConnected$ = of(false);
  }


}

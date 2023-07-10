import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserDto } from '../models/user-dto.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http:HttpClient) { }

  public login(user: UserDto) {
    return this.http.post<UserDto>('http://localhost:8090/auth/login', user);
  }

}

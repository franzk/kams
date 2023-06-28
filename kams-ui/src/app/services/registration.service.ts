import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserDto } from '../models/user-dto.model';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http:HttpClient) { }

  public register(newUser: UserDto) {
    return this.http.post<UserDto>('http://localhost:8090/register', newUser);
  }

}

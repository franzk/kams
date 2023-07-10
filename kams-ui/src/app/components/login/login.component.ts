import { LoginService } from './../../services/login.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EMPTY, catchError, tap } from 'rxjs';
import { UserDto } from 'src/app/models/user-dto.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email?: string;
  password?: string;
  emailRegExp?: RegExp;
  displayForm: boolean = true;
  loading: boolean = false;
  error?: string;

  loginForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private loginService: LoginService) { }

  ngOnInit(): void {
    this.initform();
  }

  private initform() {
    this.emailRegExp = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;

    this.loginForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.pattern(this.emailRegExp)]],
      password: [null, Validators.required],
    }, {
      updateOn: 'change'
    }
    );
  }

  public login() {
    this.loading = true;
    this.error = "";

    const user: UserDto = {
      ...this.loginForm.value
    }
    this.loginService.login(user).pipe(
      tap(() => {
          console.log('login tap');
          this.loading = false;
          this.displayForm = false;
        }),
        catchError(err => {
          console.log('error login');
          this.error = err.error;
          return EMPTY;
        })
    ).subscribe();
  }

}

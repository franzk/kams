import { RegistrationService } from './../../services/registration.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EMPTY, catchError, tap } from 'rxjs';
import { UserDto } from 'src/app/models/user-dto.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  email?: string;
  password?: string;
  emailRegExp?: RegExp;
  displayForm: boolean = true;
  registerOK: boolean = false;
  loading: boolean = false;
  error?: string;

  registrationForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private registrationService: RegistrationService) { }

  ngOnInit(): void {
    this.initform();
  }

  private initform() {
    this.emailRegExp = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;

    this.registrationForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.pattern(this.emailRegExp)]],
      password: [null, Validators.required],
    }, {
      updateOn: 'change'
    }
    );
  }

  public register() {
    this.loading = true;
    this.error = "";

    const newUser: UserDto = {
      ...this.registrationForm.value
    }
    this.registrationService.register(newUser).pipe(
      tap(() => {
          console.log('register tap');
          this.loading = false;
          this.registerOK = true;
          this.displayForm = false;
        }),
        catchError(err => {
          console.log('error register');
          this.error = err.error;
          return EMPTY;
        })
    ).subscribe();
  }

}

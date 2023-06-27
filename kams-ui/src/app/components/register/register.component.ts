import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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

  constructor(private formBuilder: FormBuilder) { }

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

  }

}

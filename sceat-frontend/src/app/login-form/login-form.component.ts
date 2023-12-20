import {Component, EventEmitter, Output} from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import {AuthserviceService} from "../services/authservice.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
    loginform: FormGroup;

    @Output() loginSuccess = new EventEmitter<void>();

    constructor(private authService: AuthserviceService, private fb: FormBuilder) {
        this.loginform = this.fb.group({
            email: ['', Validators.required],
            password: ['', Validators.required]
        });

    }

    onSubmit() {

        this.authService.login(this.loginform.value.email, this.loginform.value.password);
        this.loginSuccess.emit();
        console.log('BELÉPÉS SIKERES');
        console.log(this.authService.login(this.loginform.value.email, this.loginform.value.password));

    }
}

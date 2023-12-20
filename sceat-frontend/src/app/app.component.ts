import { Component } from '@angular/core';
import { NgOptimizedImage} from "@angular/common";
import {AuthserviceService} from "./services/authservice.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'sceat-frontend';
    isloggedin: boolean = false;
    constructor(private authService: AuthserviceService) { }

    onLoginSuccess() {
        this.isloggedin = true;
    }

    onLogout()
    {
        this.isloggedin = false;
    }

}

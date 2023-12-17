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

    constructor(private authService: AuthserviceService) { }


}
